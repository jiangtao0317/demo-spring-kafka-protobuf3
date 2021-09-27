package com.mobiu.kafka.util;

import com.google.common.collect.Lists;
import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;
import com.google.protobuf.util.JsonFormat.TypeRegistry;
import demo.BOuterClass.B;
import demo.COuterClass.C;
import java.io.IOException;
import java.util.List;
import mobiuspace.adx.log.detail.base.AdxDeviceContextOuterClass.AdxDeviceContext;
import mobiuspace.adx.log.detail.body.AdxDemandRequestOuterClass.AdxDemandRequest;

/**
 * @Author:jiangtao
 * @Date:2021/9/23 8:21 下午
 * @Desc:
 */
public class ProtoJsonUtils {

  public static List<Descriptor> descriptors ;
  public static List<Descriptor> adxDescriptors ;
  public static TypeRegistry typeRegistry ;
  public static JsonFormat.Parser parser ;
  public static JsonFormat.Printer printer ;

  static {
    descriptors = Lists.newArrayList(B.getDescriptor(), C.getDescriptor());
    adxDescriptors = Lists.newArrayList(
        AdxDeviceContext.getDescriptor(),
        AdxDemandRequest.getDescriptor());
    typeRegistry = TypeRegistry.newBuilder()
        .add(adxDescriptors)
        .add(descriptors)
        .build();
    printer = JsonFormat.printer().usingTypeRegistry(typeRegistry);
    parser = JsonFormat.parser().usingTypeRegistry(typeRegistry);
  }

  public static String toJson(Message message) throws InvalidProtocolBufferException {
    return printer.print(message);
  }

  public static Message toProtoBean(Message.Builder targetBuilder, String json) throws IOException {
    parser.merge(json, targetBuilder);
    return targetBuilder.build();
  }

}
