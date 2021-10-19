package com.mobiu.kafka.configuration;

import com.mobiu.kafka.controller.DemoKafkaController;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

/**
 * @Author:jiangtao
 * @Date:2021/9/18 6:01 下午
 * @Desc:
 */
@Configuration
public class KafkaConfiguration {



//  @Bean
//  public NewTopic newTopic2() {
//    // 参数1 ： topic 名称
//    // 参数2 ： 分区数量
//    // 参数3 ： 分区副本数量
//    return new NewTopic(DemoKafkaController.DEMO_TOPIC_PROTOBUF+"-2", 1, (short) 1);
//  }

}
