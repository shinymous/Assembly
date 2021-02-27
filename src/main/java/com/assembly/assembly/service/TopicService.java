package com.assembly.assembly.service;

import com.assembly.assembly.dto.ResponseDTO;
import com.assembly.assembly.dto.TopicDTO;
import com.assembly.assembly.dto.UserInfoDTO;
import com.assembly.assembly.dto.VoteDTO;
import com.assembly.assembly.exception.InvalidOperationException;
import com.assembly.assembly.exception.NotFoundException;
import com.assembly.assembly.model.Topic;
import com.assembly.assembly.model.TopicVote;
import com.assembly.assembly.repository.TopicRepository;
import com.assembly.assembly.repository.TopicVoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.assembly.assembly.util.CpfUtil.removeSpecialCharacters;
import static java.util.Objects.nonNull;

@RequiredArgsConstructor
@Service
public class TopicService {


    @Value("${user.info.url}")
    private String userInfoUrl;

    private final ApplicationEventPublisher applicationEventPublisher;
    private final SourceMessageService sourceMessageService;
    private final TopicRepository repository;
    private final TopicVoteRepository topicVoteRepository;

    @Transactional
    public ResponseDTO<TopicDTO> saveTopic(TopicDTO topicDTO){
        Topic topic = repository.save(Topic.builder()
                .name(topicDTO.getName())
                .description(topicDTO.getDescription())
                .endDate(topicDTO.getEndLocalDateTime())
                .build());
        return ResponseDTO.<TopicDTO>builder()
                .data(topicDTO.withId(topic.getId()))
                .message(sourceMessageService.getMessageFromUserLocale("success"))
                .build();
    }

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

    private void verifyIsAbleCpf(String cpf) throws InvalidOperationException{
        final RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<UserInfoDTO> responseEntity = restTemplate.getForEntity(userInfoUrl.concat("/").concat(cpf), UserInfoDTO.class);
        if(nonNull(responseEntity.getBody())
                && UserInfoDTO.StatusEnum.UNABLE_TO_VOTE.equals(responseEntity.getBody().getStatus()))
            throw new InvalidOperationException(sourceMessageService.getMessageFromUserLocale("unable_identifier"));
    }
}
