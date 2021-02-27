package com.assembly.assembly.service;

import com.assembly.assembly.dto.ResponseDTO;
import com.assembly.assembly.dto.TopicDTO;
import com.assembly.assembly.model.Topic;
import com.assembly.assembly.repository.TopicRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;

import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TopicEndpointTest {

    @LocalServerPort
    private int port;
    @MockBean
    private TopicRepository repository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TestRestTemplate restTemplate;

    @TestConfiguration
    static class Config{
        @Bean
        public RestTemplateBuilder restTemplateBuilder(){
            return new RestTemplateBuilder();
        }
    }

    @Test
    public void shouldCreateTopic(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        TopicDTO topicDTO = TopicDTO.builder()
                .endLocalDateTime(LocalDateTime.now().plusMinutes(2))
                .description("Descrição teste")
                .name("Nome teste")
                .build();
        HttpEntity<?> httpEntity = new HttpEntity(topicDTO, headers);
        BDDMockito.when(repository.save(any(Topic.class))).thenReturn(Topic.builder()
                .name(topicDTO.getName())
                .description(topicDTO.getDescription())
                .endDate(topicDTO.getEndLocalDateTime())
                .closed(false)
                .id(1L)
                .build());
        ResponseEntity<ResponseDTO> forEntity = restTemplate.postForEntity("/v1/topic", httpEntity, ResponseDTO.class);
        Assertions.assertThat(forEntity.getStatusCode().value()).isEqualTo(201);
        Assertions.assertThat(((LinkedHashMap)forEntity.getBody().getData()).get("id")).isEqualTo(1);
    }

}
