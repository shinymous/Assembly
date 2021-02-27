package com.assembly.assembly.service;

import com.assembly.assembly.dto.ResponseDTO;
import com.assembly.assembly.dto.TopicDTO;
import com.assembly.assembly.dto.UserInfoDTO;
import com.assembly.assembly.dto.VoteDTO;
import com.assembly.assembly.event.payload.TopicResultPayload;
import com.assembly.assembly.event.strategy.TopicResultEvent;
import com.assembly.assembly.exception.InvalidOperationException;
import com.assembly.assembly.exception.NotFoundException;
import com.assembly.assembly.model.Topic;
import com.assembly.assembly.model.TopicVote;
import com.assembly.assembly.repository.TopicRepository;
import com.assembly.assembly.repository.TopicVoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

import static com.assembly.assembly.util.CpfUtil.removeSpecialCharacters;
import static java.util.Objects.nonNull;

@Slf4j
@RequiredArgsConstructor
@Service
public class TopicService {


    @Value("${user.info.url}")
    private String userInfoUrl;

    private final ApplicationEventPublisher applicationEventPublisher;
    private final SourceMessageService sourceMessageService;
    private final TopicRepository repository;
    private final TopicVoteRepository topicVoteRepository;
    private final ThreadPoolTaskScheduler threadPoolTaskScheduler;

    /**
     * Salva Topic, adicionando
     * em uma thread para executar o fechamento do mesmo na data especificada
     * @param topicDTO
     * @return
     * @author Andrei Silva
     */
    @Transactional
    public ResponseDTO<TopicDTO> saveTopic(TopicDTO topicDTO){
        Topic topic = repository.save(Topic.builder()
                .name(topicDTO.getName())
                .description(topicDTO.getDescription())
                .endDate(topicDTO.getEndLocalDateTime())
                .build());
        this.schedulingToCloseTopic(topic);
        return ResponseDTO.<TopicDTO>builder()
                .data(topicDTO.withId(topic.getId()))
                .message(sourceMessageService.getMessageFromUserLocale("success"))
                .build();
    }

    /**
     * Processo de fechar Topic
     * - fecha
     * - publica no tópico
     * @param topic
     * @author Andrei Silva
     */
    @Transactional
    public void closeTopic(Topic topic){
        topic.setClosed(true);
        repository.save(topic);
        this.publishClosedTopic(topic);
    }

    /**
     * Publica TopicResultEvent com
     * informação do Topic e TopicVote
     * @param topic
     * @author Andrei Silva
     */
    public void publishClosedTopic(Topic topic) {
        log.info("Publicando no tópico, a entidade Topic com id: " + topic.getId());

        List<TopicVote> topicVoteList = topicVoteRepository.findAllByTopic_Id(topic.getId());
        long numberNoVotes = topicVoteList.stream()
                .filter(topicVote -> TopicVote.VOTE_NAO.equals(topicVote.getVote())).count();
        long numberYesVotes = topicVoteList.stream()
                .filter(topicVote -> TopicVote.VOTE_SIM.equals(topicVote.getVote())).count();
        TopicResultPayload.Result result = TopicResultPayload.Result.EMPATE;

        if (numberNoVotes > numberYesVotes)
            result = TopicResultPayload.Result.NAO;
        else if (numberYesVotes > numberNoVotes)
            result = TopicResultPayload.Result.SIM;

        applicationEventPublisher.publishEvent(TopicResultEvent.TOPIC_RESULT
                .newInstance(TopicResultPayload.builder()
                        .description(topic.getDescription())
                        .name(topic.getName())
                        .id(topic.getId())
                        .numberNoVotes(numberNoVotes)
                        .numberYesVotes(numberYesVotes)
                        .result(result)
                        .build()));
        log.info("Topic: " + topic.getId() + " publicada");
    }

    /**
     * Fecha Topic no horário fim em uma thread
     * @param topic
     * @author Andrei Silva
     */
    public void schedulingToCloseTopic(Topic topic){
        Runnable task = () -> closeTopic(topic);
        LocalDateTime endDate = topic.getEndDate();
        threadPoolTaskScheduler.schedule(task, endDate.toInstant(ZoneId.systemDefault().getRules().getOffset(endDate)));
    }

    /**
     * Busca lista de Topic pelo parametro name
     * e transforma em DTO (List<TopicDTO>)
     * @param name
     * @return
     * @author Andrei Silva
     */
    public ResponseDTO<List<TopicDTO>> findAllValidTopicByName(String name){
        return ResponseDTO.<List<TopicDTO>>builder()
                .status(HttpStatus.OK.value())
                .data(repository.findAllValidByLikeName(name).stream()
                        .map(topic -> TopicDTO.builder()
                                .id(topic.getId())
                                .name(topic.getName())
                                .description(topic.getDescription())
                                .endLocalDateTime(topic.getEndDate())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    /**
     * Adiciona um voto (SIM/NAO) no Topic especificado
     * Verifica se o cpf é válido, e se o voto é permitido
     * @param topicId
     * @param voteDTO
     * @return
     * @throws InvalidOperationException
     * @author Andrei Silva
     */
    @Transactional
    public ResponseDTO<?> vote(Long topicId, VoteDTO voteDTO) throws InvalidOperationException{
        voteDTO.setCpf(removeSpecialCharacters(voteDTO.getCpf()));
        Topic topic = repository.findById(topicId)
                .orElseThrow(() -> new NotFoundException(
                        sourceMessageService.getMessageFromUserLocale("something_not_found",
                                sourceMessageService.getMessageFromUserLocale("topic"))));
        if(topic.getEndDate().isBefore(LocalDateTime.now()) || topic.isClosed())
            throw new InvalidOperationException(sourceMessageService.getMessageFromUserLocale("closed_topic"));
        if(topicVoteRepository.findByAssociateIdentifierAndTopic_Id(voteDTO.getCpf(), topic.getId()).isPresent())
            throw new InvalidOperationException(sourceMessageService.getMessageFromUserLocale("user_already_voted", voteDTO.getCpf()));
        this.verifyIsAbleCpf(voteDTO.getCpf());
        topicVoteRepository.save(TopicVote.builder()
                .topic(topic)
                .vote(voteDTO.getVote().getValue())
                .associateIdentifier(voteDTO.getCpf())
                .build());
        return ResponseDTO.builder()
                .status(HttpStatus.OK.value())
                .message(sourceMessageService.getMessageFromUserLocale("computed_vote"))
                .build();
    }

    /**
     * Analisa se pessoa com cpf é apta a votar
     * @param cpf
     * @throws InvalidOperationException
     * @author Andrei Silva
     */
    private void verifyIsAbleCpf(String cpf) throws InvalidOperationException{
        final RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<UserInfoDTO> responseEntity = restTemplate.getForEntity(userInfoUrl.concat("/").concat(cpf), UserInfoDTO.class);
        if(nonNull(responseEntity.getBody())
                && UserInfoDTO.StatusEnum.UNABLE_TO_VOTE.equals(responseEntity.getBody().getStatus()))
            throw new InvalidOperationException(sourceMessageService.getMessageFromUserLocale("unable_identifier"));
    }
}
