package co.edu.javeriana.orders.configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.time.LocalDate;

@Configuration
public class EventsConfiguration {

    @Value("${events.dead-letter.exchange}")
    String deadExchange;

    @Value("${events.dead-letter.queue}")
    String deadQueue;

    @Value("${events.dead-letter.routing-key}")
    String deadRoutingKey;

    @Value("${events.amqp.exchange}")
    String orderExchange;

    @Value("${events.amqp.queue}")
    String orderQueue;

    @Value("${events.amqp.routing-key}")
    String orderRoutingKey;

    @Bean
    DirectExchange deadLetterExchange() {
        return new DirectExchange(deadExchange);
    }

    @Bean
    Queue dlq() {
        return QueueBuilder.durable(deadQueue).build();
    }

    @Bean
    Queue queue() {
        return QueueBuilder.durable(orderQueue)
                           .withArgument("x-dead-letter-exchange", deadExchange)
                           .withArgument("x-dead-letter-routing-key", deadRoutingKey)
                           .build();
    }

    @Bean
    DirectExchange exchange() {
        return new DirectExchange(orderExchange);
    }

    @Bean
    Binding DLQbinding(Queue dlq, DirectExchange deadLetterExchange) {
        return BindingBuilder.bind(dlq).to(deadLetterExchange).with(deadRoutingKey);
    }

    @Bean
    Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(orderRoutingKey);
    }

    @Bean
    MessageListenerContainer messageListenerContainer(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer();
        simpleMessageListenerContainer.setConnectionFactory(connectionFactory);
        simpleMessageListenerContainer.setQueues(queue());
        //simpleMessageListenerContainer.setMessageListener(new RabbitMQListener());
        return simpleMessageListenerContainer;
    }

    @Bean
    @Primary
    public ObjectMapper serializingObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        JavaTimeModule module = new JavaTimeModule();
        module.addSerializer(LocalDate.class, new LocalDateSerializer());
        module.addDeserializer(LocalDate.class, new LocalDateDeserializer());
        mapper.registerModule(module);
        return mapper;
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter(serializingObjectMapper());
    }

    @Bean
    public AmqpTemplate template(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

}
