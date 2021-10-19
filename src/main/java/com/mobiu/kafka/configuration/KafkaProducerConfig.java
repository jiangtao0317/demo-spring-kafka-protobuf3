package com.mobiu.kafka.configuration;

import com.mobiu.kafka.controller.DemoKafkaController;
import java.util.Map;
import lombok.Data;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

/**
 * @Author:jiangtao
 * @Date:2021/9/28 8:35 下午
 * @Desc:
 */
@Configuration
@Data
@ConfigurationProperties(prefix = "kafka.adx.log")
@EnableConfigurationProperties(KafkaProperties.class)
public class KafkaProducerConfig {

  //公用属性配置
  @Autowired
  private KafkaProperties properties;

  // 不同 kafka 源 特殊的属性
  KafkaCustomProperties simple ;

  @Bean
  public KafkaCustomProperties simpleKafkaCustomProperties(){
    return simple ;
  }

  @Bean
  public KafkaCustomProperties detailKafkaCustomProperties(){
    return detail ;
  }

  @Bean
  public KafkaTemplate<Integer, String> simpleKafkaTemplate(){
    return new KafkaTemplate(simpleProducerFactory());
  }


  private ProducerFactory<Integer, String> simpleProducerFactory() {
    return new DefaultKafkaProducerFactory<>(simpleProducerConfig());
  }

  private Map<String, Object> simpleProducerConfig() {
    Map<String, Object> props = producerProperties();
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,simple.getServers());
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    return props;
  }



  KafkaCustomProperties detail ;

  private Map<String, Object> detailProducerConfig() {
    Map<String, Object> props = producerProperties();
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,detail.getServers());
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class);
    return props;
  }

  @Bean
  public KafkaTemplate<String, byte[]> detailKafkaTemplate(){
    KafkaTemplate<String, byte[]> kafkaTemplate = new KafkaTemplate(detailProducerFactory());
    kafkaTemplate.setDefaultTopic(detail.getDefaultTopic());
    return kafkaTemplate ;
  }

  private ProducerFactory<String, byte[]> detailProducerFactory() {
    return new DefaultKafkaProducerFactory<>(detailProducerConfig());
  }

  @Data
  public static class KafkaCustomProperties {
    String servers ;
    String defaultTopic ;
  }

//  /**
//   * 默认 所有 topic （来源于 Spring 容器 中 NewTopic 的 Bean 实例）
//   * 会基于 kafkaAdmin 自动创建，
//   * 可以设置 kafkaAdmin autoCreate 属性为 false 禁止自动创建
//   * 默认的 kafkaAdmin 基于 spring boot 的 kafka 配置
//   * 这里由于有两个 kafka 连接源，所以重新定义
//   * @return
//   */
//  @Bean
//  public KafkaAdmin kafkaAdmin() {
//    KafkaAdmin kafkaAdmin = new KafkaAdmin(detailProducerConfig());
//    return kafkaAdmin;
//  }

//  @Bean
//  public NewTopic newTopic() {
//    // 参数1 ： topic 名称
//    // 参数2 ： 分区数量
//    // 参数3 ： 分区副本数量
//    return new NewTopic(DemoKafkaController.DEMO_TOPIC_PROTOBUF, 20, (short) 2);
//  }


  public Map<String, Object> producerProperties(){
    return properties.buildProducerProperties() ;
  }

}
