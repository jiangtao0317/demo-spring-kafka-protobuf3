package com.mobiu.kafka;

import com.google.common.collect.Lists;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

/**
 * @Author:jiangtao
 * @Date:2021/10/14 7:09 下午
 * @Desc:
 */
@Slf4j
public class KafkaConsumerClient {

  public static void main(String[] args) {
    KafkaConsumer<String, byte[]> consumer = new KafkaConsumer<>(config());
    //参数形式一个集合，可以接受多个topic
    //设置消费的topic
    consumer.subscribe(Lists.newArrayList("prod-adx-detail-log"));

    Map map = consumer.listTopics();
    ConsumerRecords<String, byte[]> poll = consumer.poll(Duration.ofMillis(1000));
    toString(poll);
//    while(true) {
//      //  从服务器开始拉取数据
//
//    }
    //toString(poll);
  }

  public static void toString(ConsumerRecords<String,byte[]> records){
    records.forEach(record -> {
      //TODO 业务处理
      log.info("数据消费成功: {}", record);
    });

  }

  public static Properties config(){
    final Properties properties = new Properties();
    properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"monetization-ad-log-kafka-01.meta.aws:9092,monetization-ad-log-kafka-02.meta.aws:9092,monetization-ad-log-kafka-03.meta.aws:9092,monetization-ad-log-kafka-04.meta.aws:9092,monetization-ad-log-kafka-05.meta.aws:9092");
    properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class);
    properties.put("group.id","test_group1");
    properties.put("client.id","client-demo1");
    properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG,1000);
    properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,true);
    properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"latest");
    return properties;
  }

}
