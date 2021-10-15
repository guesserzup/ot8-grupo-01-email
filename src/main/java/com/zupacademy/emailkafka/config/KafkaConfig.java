package com.zupacademy.emailkafka.config;

import com.zupacademy.emailkafka.consumidorResources.EntradaForm;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.Properties;

@Configuration
public class KafkaConfig {

    private final KafkaProperties kafkaProperties;

    public KafkaConfig(KafkaProperties kafkaProperties) {this.kafkaProperties = kafkaProperties;}

    public Properties consumerConfigurations() {
        Properties props = new Properties();

        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaProperties.getConsumer().getGroupId());
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafkaProperties.getConsumer().getAutoOffsetReset());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, kafkaProperties.getConsumer().getKeyDeserializer());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, kafkaProperties.getConsumer().getValueDeserializer());

        return props;
    }

    @Bean
    public ConsumerFactory<String, EntradaForm> transactionConsumerFactory() {
        StringDeserializer stringDeserializer = new StringDeserializer();
        JsonDeserializer<EntradaForm> jsonDeserializer = new JsonDeserializer<EntradaForm>(EntradaForm.class);

        return new DefaultKafkaConsumerFactory(consumerConfigurations(), stringDeserializer, jsonDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, EntradaForm> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, EntradaForm> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(transactionConsumerFactory());

        return factory;
    }
}
