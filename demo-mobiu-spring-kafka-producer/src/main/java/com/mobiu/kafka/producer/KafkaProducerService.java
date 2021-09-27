package com.mobiu.kafka.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * @Author:jiangtao
 * @Date:2021/9/18 3:17 下午
 * @Desc:
 */
@Service
public class KafkaProducerService {
  @Autowired
  private KafkaTemplate kafkaTemplate ;

  public String sendMessage(String topic,String key,byte[] data){
    kafkaTemplate.send(topic,key,data);
    return "success";
  }

//  public String sendMessage(String topic,String key,String data){
//    kafkaTemplate.send(topic,key,data);
//    return "success";
//  }
}
