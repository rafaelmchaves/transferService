package transfer.service.ingenico.conf.springfox.RabbitMQ;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQTransferServiceConfig {

    @Value("${rabbit.transferService.queue:ha.transfer.service.event.queue}")
    private String transferServiceQueue;

    @Value("${rabbit.transferService.routingKey:transfer.service.event}")
    private String transferServiceRoutingKey;

    @Value("${rabbit.transferService.exchange:transfer-service-event}")
    private String transferServiceExchange;

    @Value("${rabbit.transferService.concurrentConsumers:1}")
    private Integer transferServiceConcurrentConsumers;

    @Value("${rabbit.transferService.maxConcurrentConsumers:20}")
    private Integer transferServiceMaxConcurrentConsumers;

    @Value("${rabbit.transferService.defaultRequeueRejected:false}")
    private Boolean transferServiceDefaultRequeueRejected;

    @Bean
    public SimpleRabbitListenerContainerFactory transferServiceListenerContainerFactory(final ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory listenerFactory = new SimpleRabbitListenerContainerFactory();
        listenerFactory.setConnectionFactory(connectionFactory);
        listenerFactory.setConcurrentConsumers(transferServiceConcurrentConsumers);
        listenerFactory.setMaxConcurrentConsumers(transferServiceMaxConcurrentConsumers);
        listenerFactory.setDefaultRequeueRejected(transferServiceDefaultRequeueRejected);
        return listenerFactory;
    }

    @Bean
    public TopicExchange transferServiceExchange() {
        return new TopicExchange(transferServiceExchange);
    }

    @Bean
    public Queue transferServiceQueue() {
        return new Queue(transferServiceQueue, true);
    }

    @Bean
    public Binding transferServiceBinding() {
        return BindingBuilder.bind(transferServiceQueue()).to(transferServiceExchange()).with(transferServiceRoutingKey);
    }
}
