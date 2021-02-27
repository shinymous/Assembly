package com.assembly.assembly.event.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Getter;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

public abstract class EventConfig {

    @Getter
    @Value("${spring.application.name}")
    private String application;

    public abstract String getEventTopicName();

    @Bean
    Declarables topicBindings() {
        Exchange topicExchange = ExchangeBuilder.topicExchange( getEventTopicName() ).build();
        return new Declarables( topicExchange );
    }

    /**
     * Configuração do rabbitTemplate
     * (altera conversor de mensagem para o conversor criado
     * no método producerJackson2MessageConverter)
     * @param connectionFactory
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());
        return rabbitTemplate;
    }

    /**
     * Configura consersor de mensagem
     * ObjectMapper adicionado modulo do JavaTimeModule
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    Jackson2JsonMessageConverter producerJackson2MessageConverter() {
        ObjectMapper mapper = new ObjectMapper();

        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        return new Jackson2JsonMessageConverter(mapper);
    }

    @Bean
    EventProcessor eventProcessor(RabbitTemplate rabbitTemplate ) {
        return new EventProcessor(this, rabbitTemplate );
    }
}
