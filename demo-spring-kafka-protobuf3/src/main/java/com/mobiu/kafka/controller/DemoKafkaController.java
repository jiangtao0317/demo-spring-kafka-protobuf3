package com.mobiu.kafka.controller;


import com.dayuwuxian.ad.adx.core.model.Context;
import com.dayuwuxian.ad.adx.core.model.enums.AdFormat;
import com.dayuwuxian.ad.adx.core.model.enums.RequestMethod;
import com.dayuwuxian.ad.adx.core.model.rtb.RtbRequest;
import com.dayuwuxian.ad.adx.core.storage.mysql.AppSite;
import com.dayuwuxian.ad.adx.core.storage.mysql.Placement;
import com.dayuwuxian.ad.adx.core.storage.mysql.Supply;
import com.dayuwuxian.ad.adx.core.storage.mysql.System;
import com.dayuwuxian.ad.common.model.AdRequest;
import com.dayuwuxian.ad.proto.AdxDetailLogOuterClass.AdxDetailLog;
import com.dayuwuxian.ad.proto.AdxDetailLogOuterClass.AdxDetailLogOrBuilder;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import com.google.protobuf.Any;
import com.google.protobuf.InvalidProtocolBufferException;
import com.mobiu.kafka.producer.KafkaProducerService;
import com.mobiu.kafka.util.ProtoBeanGenerator;
import com.mobiu.kafka.util.ProtoJsonUtils;
import com.monetization.adx.proto.base.AdxDemandContextOuterClass.AdxDemandContext;
import com.monetization.adx.proto.base.AdxEventTypeOuterClass.AdxEventType;
import com.monetization.adx.proto.body.AdxDemandRequestOuterClass.AdxDemandRequest;
import com.monetization.adx.proto.body.AdxDemandResponseOuterClass.ResponseBody;
import com.monetization.adx.proto.common.AdxRequestMessageLogOuterClass.AdxRequestMessageLog;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import nonapi.io.github.classgraph.json.JSONUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author:jiangtao
 * @Date:2021/9/18 11:40 上午
 * @Desc: 你好
 */
@RestController
@Slf4j
public class DemoKafkaController {

  @Autowired
  private KafkaProducerService producerService ;

  public static final String DEMO_TOPIC_PROTOBUF = "staging-demo-protocol-1-b";

//  @GetMapping("/send/kafka-message")
//  public String sendMessage(){
//    DemoInfo.Demo.Builder demoBuilder = DemoInfo.Demo.newBuilder();
//    demoBuilder.addHobbies("足球");
//    demoBuilder.addHobbies("篮球");
//    demoBuilder.setName("demo");
//    demoBuilder.setAge(12);
//    demoBuilder.setSex(SexType.F);
//    DemoInfo.Demo demo = demoBuilder.build();
//    producerService.sendMessage(DEMO_TOPIC_PROTOBUF,"vk",demo.toByteArray());
//    return "success";
//  }
//
//
//  @GetMapping("/send/kafka-message-2")
//  public String sendMessage2(){
//    JavaDemo javaDemo = new JavaDemo() ;
//    producerService.sendMessage(DEMO_TOPIC_PROTOBUF,"vk",javaDemo.toString().getBytes(StandardCharsets.UTF_8));
//    return "success";
//  }
//
//  private static class JavaDemo {
//    private String name = "JavaDemo";
//    private int age = 12;
//    private List<String> hobbies = Lists.newArrayList("足球","篮球") ;
//    private SexType sexType = SexType.F ;
//  }
//
//
//  @GetMapping("/send/kafka-message-3")
//  public String sendMessage3(){
//    B.Builder bBuilder = B.newBuilder() ;
//    bBuilder.setName("B");
//    bBuilder.setStatus(Status.DISABLE);
//
//    A.Builder builder = A.newBuilder() ;
//    builder.setId("B12345");
//    builder.setName("A");
//    builder.setTimestamp(TimeUtils.localDateTimeToTimestamp(LocalDateTime.now(), ZoneOffset.UTC));
//    builder.setData(Any.pack(bBuilder.build()));
//    producerService.sendMessage(DEMO_TOPIC_PROTOBUF,"vk",builder.build().toByteArray());
//    return "success";
//  }
//
//  @GetMapping("/send/kafka-message-4")
//  public String sendMessage4(){
//    D.Builder dBuilder = D.newBuilder() ;
//    dBuilder.setName("D");
//    dBuilder.setScore(98);
//
//    C.Builder cBuilder = C.newBuilder() ;
//    cBuilder.setName("C");
//    cBuilder.addHobbies("篮球");
//    cBuilder.addHobbies("足球");
//    cBuilder.setD(dBuilder);
//
//    A.Builder builder = A.newBuilder() ;
//    builder.setId("C12345");
//    builder.setName("A");
//    builder.setData(Any.pack(cBuilder.build()));
//    producerService.sendMessage(DEMO_TOPIC_PROTOBUF,"vk",builder.build().toByteArray());
//    return "success";
//  }

  @PostMapping("/send/kafka-message-5")
  public String sendMessage5(@RequestBody AdRequest adRequest)
      throws InvalidProtocolBufferException {
    Context context = new Context();
    context.setAdCommonRequest(adRequest);

    AppSite appSite = new AppSite();
    appSite.setBundle("com.snaptube.premium");
    appSite.setDomain("www.snaptubeapp.com");
    appSite.setName("Snaptube");
    context.setAppSite(appSite);

    context.setAbTestTags(Lists.newArrayList("V7"));

    Supply supply = new Supply() ;
    supply.setId(123L);
    context.setSupply(supply);

    Placement placement = new Placement();
    placement.setName("browse_banner");
    placement.setAdFormat(AdFormat.BANNER);
    context.setPlacement(placement);

    System system = new System() ;
    system.setId(12456L);
    system.setTbDemandId(34566L);
    system.setRequestMethod(RequestMethod.POST);

    AdxRequestMessageLog.Builder logBuilder = ProtoBeanGenerator.generateAdxRequestMessageLog(
        AdxEventType.ADX_DEMAND_REQUEST_EVENT,context);

//    AdxDemandContext demandContext = ProtoBeanGenerator.generateAdxDemandContext(system,"2344546565765767");
//    AdxDemandRequest demandRequest = ProtoBeanGenerator.generateAdxDemandRequest(demandContext
//        ,new RtbRequest(),system,"http://localhost:8888/send/kafka-message-5");
    //logBuilder.setContent(Any.pack(demandRequest));

    log.info(ProtoJsonUtils.toJson(logBuilder.build()));

    producerService.sendMessageToDefaultTopic(logBuilder.build().toByteArray());

    // AdxDetailLog.Builder builder =
    // AdxDetailLog.newBuilder().setJson(logBuilder.build()
    // .toString());
    //producerService.sendMessageToDefaultTopic(builder.build().toByteArray());
    //producerService.sendMessage(DEMO_TOPIC_PROTOBUF,"vk2",logBuilder.build().toString().getBytes(StandardCharsets.UTF_8));
    return "success";
  }

  public static void main(String[] args) throws InvalidProtocolBufferException {
    ResponseBody responseBody = ResponseBody.newBuilder()
            .setBundle("adsd")
                .setCur("usd").build();

    log.info(StringUtils.deleteWhitespace(ProtoJsonUtils.toJson(responseBody)));


  }

}
