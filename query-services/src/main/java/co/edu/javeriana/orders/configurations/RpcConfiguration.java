package co.edu.javeriana.orders.configurations;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RpcConfiguration {

    @Value("${events.rpc.orders.exchange}")
    private String rpcExchange;

    @Value("${events.rpc.orders.queue}")
    private String rpcQueue;

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(rpcExchange);
    }

    @Bean
    public AsyncRabbitTemplate asyncRabbitTemplate(RabbitTemplate rabbitTemplate){
        return new AsyncRabbitTemplate(rabbitTemplate);
    }

    @Bean
    public Queue response(){
        return new Queue(rpcQueue);
    }
}
