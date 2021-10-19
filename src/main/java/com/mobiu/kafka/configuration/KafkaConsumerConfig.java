package com.mobiu.kafka.configuration;

import com.mobiu.kafka.configuration.KafkaProducerConfig.KafkaCustomProperties;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

/**
 * @Author:jiangtao
 * @Date:2021/10/13 11:02 上午
 * @Desc:
 */
@Configuration
@EnableConfigurationProperties({KafkaProperties.class})
public class KafkaConsumerConfig {

  @Autowired
  private KafkaProperties properties ;

  @Resource(name = "detailKafkaCustomProperties")
  private KafkaCustomProperties detailKafkaCustomProperties ;

  @Resource(name = "simpleKafkaCustomProperties")
  private KafkaCustomProperties simpleKafkaCustomProperties ;

  @Bean("detailContainer")
  public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, byte[]>> kafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, byte[]> factory = new ConcurrentKafkaListenerContainerFactory<>();
    ConsumerFactory<String, byte[]> consumerFactory = new DefaultKafkaConsumerFactory<>(detailConsumerProperties());
    factory.setConsumerFactory(consumerFactory);
    factory.setConcurrency(5);
    return factory;
  }

//  @Bean("simpleContainer")
//  public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<Integer, String>> simpleKafkaListenerContainerFactory() {
//    ConcurrentKafkaListenerContainerFactory<Integer, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
//    ConsumerFactory<Integer, String> consumerFactory = new DefaultKafkaConsumerFactory<>(simpleConsumerProperties());
//    factory.setConsumerFactory(consumerFactory);
//    factory.setConcurrency(5);
//    return factory;
//  }

  public Map<String, Object> detailConsumerProperties(){
    Map<String,Object> map = properties.buildConsumerProperties() ;
    map.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,detailKafkaCustomProperties.getServers());
    map.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    map.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class);
    //map.put(ConsumerConfig.GROUP_ID_CONFIG,"demo-vito-5");
    return map ;
  }
//  public Map<String, Object> simpleConsumerProperties(){
//    Map<String,Object> map = properties.buildConsumerProperties() ;
//    map.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,simpleKafkaCustomProperties.getServers());
//    map.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class);
//    map.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//    //map.put(ConsumerConfig.GROUP_ID_CONFIG,"demo-vito-5");
//    return map ;
//  }
}
