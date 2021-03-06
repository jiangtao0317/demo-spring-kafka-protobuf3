package com.mobiu.kafka.consumer;

import com.mobiu.kafka.util.ProtoJsonUtils;
import com.monetization.adx.proto.common.AdxRequestMessageLogOuterClass.AdxRequestMessageLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * @Author:jiangtao
 * @Date:2021/9/18 3:20 下午
 * @Desc:
 */
@Service
@Slf4j
public class KafkaConsumerService {

//  @KafkaListener(clientIdPrefix = "protobuf-test",concurrency = "1",
//      topics = "staging-demo-protocol", groupId = "demo-vito-2")
//  public void listener(ConsumerRecord<String, byte[]> record) {
//    log.info("数据消费成功: {}", record);
//    try {
//      AdxSupplyRequest request = AdxSupplyRequest.parseFrom(record.value());
//      log.info("数据消费成功 supplyRequest: {}", request);
//    } catch (Exception e) {
//      log.error("【kafka消费服务】 消费发生异常，log异常原因：{}",e);
//    }
//  }

  @KafkaListener(clientIdPrefix = "protobuf-test",
      topics = "${kafka.adx.log.detail.default-topic}",
      containerFactory = "detailContainer")
  public void listener(ConsumerRecord<String, byte[]> record) {
    log.info("数据消费成功: {}", record);
    try {
//      String a = new String(record.value());

      AdxRequestMessageLog request = AdxRequestMessageLog.parseFrom(record.value());
      String json = ProtoJsonUtils.toJson(request);
      log.info("[{}] 事件数据消费成功 : {}",request.getEvent(), json.replaceAll(" ",""));
//      AdxRequestMessageLog log1 = (AdxRequestMessageLog) ProtoJsonUtils.toProtoBean(AdxRequestMessageLog.newBuilder(),json);
//      log.info("content:{}",log1.getContent().unpack(AdxDemandRequest.class));
    } catch (Exception e) {
      log.error("【kafka消费服务】 消费发生异常，log异常原因：{}",e);
    }
  }

//  @KafkaListener(clientIdPrefix = "simple-test",
//      topics = "${kafka.adx.log.simple.defaultTopic}",
//      containerFactory = "simpleContainer")
//  public void listener2(ConsumerRecord<Integer,String> record) {
//    log.info("数据消费成功: {}", record);
//  }


}
