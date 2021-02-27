package com.assembly.assembly.event.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Getter;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Collection;

@Configuration
public class AssemblyEventConfig extends EventConfig {

    @Getter
    @Value("${assembly.event.config.topic-name:assembly.event}")
    private String eventTopicName;
    @Getter
    @Value("${vote.result.queue:consumer.queue}")
    private String topicResultQueueName;

    @Bean
    RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());
        return rabbitTemplate;
    }

    @Bean
    Jackson2JsonMessageConverter producerJackson2MessageConverter() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return new Jackson2JsonMessageConverter(mapper);
    }

    /**
     * Método que simula uma queue com binding na rota
     * "event.vote_result" (para visualizar melhor os resultados)
     * @return
     * @author Andrei Silva
     */
    @Bean
    Declarables createTopology() {
        Collection<Declarable> declarables = new ArrayList<Declarable>();
        Exchange topic = ExchangeBuilder.topicExchange(eventTopicName).durable(true).build();
        Queue voteResultQueue = QueueBuilder.durable(topicResultQueueName).build();
        declarables.add(topic);
        declarables.add(voteResultQueue);
        declarables.add(BindingBuilder.bind(voteResultQueue)
                .to(ExchangeBuilder.topicExchange(eventTopicName).build()).with("event.vote_result")
                .noargs());
        return new Declarables(declarables);
    }

}
