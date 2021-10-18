package com.mobiu.kafka.util;

import com.dayuwuxian.ad.adx.core.model.Context;
import com.dayuwuxian.ad.adx.core.model.rtb.RtbRequest;
import com.dayuwuxian.ad.adx.core.model.rtb.RtbResponse;
import com.dayuwuxian.ad.adx.core.storage.mysql.Placement;
import com.dayuwuxian.ad.adx.core.storage.mysql.Supply;
import com.dayuwuxian.ad.adx.core.storage.mysql.System;
import com.dayuwuxian.ad.common.model.AdRequest;
import com.monetization.adx.proto.base.AdxAppContextOuterClass.AdxAppContext;
import com.monetization.adx.proto.base.AdxDemandContextOuterClass.AdxDemandContext;
import com.monetization.adx.proto.base.AdxDeviceContextOuterClass.AdxDeviceContext;
import com.monetization.adx.proto.base.AdxDeviceContextOuterClass.Geo;
import com.monetization.adx.proto.base.AdxEventTypeOuterClass.AdxEventType;
import com.monetization.adx.proto.base.AdxExtraContextOuterClass.AdxExtraContext;
import com.monetization.adx.proto.base.AdxSupplyContextOuterClass.AdxSupplyContext;
import com.monetization.adx.proto.base.AdxUserContextOuterClass.AdxUserContext;
import com.monetization.adx.proto.body.AdxCreativeClickOuterClass.AdxCreativeClick;
import com.monetization.adx.proto.body.AdxCreativeImpressionOuterClass.AdxCreativeImpression;
import com.monetization.adx.proto.body.AdxDemandRequestOuterClass.AdxDemandRequest;
import com.monetization.adx.proto.body.AdxDemandResponseOuterClass.AdxDemandResponse;
import com.monetization.adx.proto.body.AdxDemandWinOuterClass.AdxDemandWin;
import com.monetization.adx.proto.body.AdxSupplyRequestOuterClass.AdxSupplyRequest;
import com.monetization.adx.proto.common.AdxRequestMessageLogOuterClass.AdxRequestMessageLog;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

/**
 * @Author:jiangtao
 * @Date:2021/9/26 10:47 上午
 * @Desc:
 */
public class ProtoBeanGenerator {

  public static AdxUserContext defaultUserContext = generateAdxUserContext() ;
  public static AdxCreativeClick defaultAdxCreativeClick = generateAdxCreativeClick() ;
  public static AdxSupplyRequest defaultAdxSupplyRequest = generateAdxSupplyRequest() ;
  public static AdxCreativeImpression defaultAdxCreativeImpression = generateAdxCreativeImpression() ;

  public static AdxRequestMessageLog.Builder generateAdxRequestMessageLog(AdxEventType event, Context context){
    return AdxRequestMessageLog.newBuilder()
        .setTimestamp(TimeUtils.localDateTimeToTimestamp(LocalDateTime.now(), ZoneOffset.UTC))
        .setGlobalId(context.getAdCommonRequest().getGlobalId())
        .setBidId(context.getAdCommonRequest().getRequestId())
        .setEvent(event)
        .setAppContext(generateAdxAppContext(context))
        .setSupplyContext(generateAdxSupplyContext(context.getSupply(),context.getPlacement()))
        .setDeviceContext(generateAdxDeviceContext(context.getAdCommonRequest()))
        .setUserContext(defaultUserContext)
        .setExtraContext(generateAdxExtraContext(context.getAbTestTags()));
  }

  public static AdxDemandRequest generateAdxDemandRequest(AdxDemandContext demandContext,
      RtbRequest rtbRequest,System system,String url){
    return AdxDemandRequest.newBuilder().setDemandContext(demandContext)
          .setMethod(fieldIfEmpty(system.getRequestMethod().name()))
          .setUrl(fieldIfEmpty(url))
          .build();
  }

  public static AdxDemandResponse generateAdxDemandResponse(AdxDemandContext demandContext, RtbResponse response){
    return AdxDemandResponse.newBuilder().setDemandContext(demandContext)
          .build();
  }

  public static AdxDemandWin generateAdxDemandWin(AdxDemandContext demandContext){
    AdxDemandWin.Builder builder = AdxDemandWin.newBuilder() ;
    if(Optional.of(demandContext).isPresent()){
      builder.setDemandContext(demandContext);
    }
    return builder.build();
  }

  public static AdxDeviceContext generateAdxDeviceContext(AdRequest adRequest){
    AdxDeviceContext.Builder builder = AdxDeviceContext.newBuilder() ;
    if(Optional.of(adRequest).isPresent()){
      builder.setAndroidId(fieldIfEmpty(adRequest.getAndroidId()))
          .setBrand(fieldIfEmpty(adRequest.getBrand()))
          .setGaId(fieldIfEmpty(adRequest.getGaid()))
          .setGeo(generateGeo(adRequest))
          .setImei(fieldIfEmpty(adRequest.getImei()))
          .setLanguage(fieldIfEmpty(adRequest.getLang()))
          .setMake(fieldIfEmpty(adRequest.getModel()))
          .setOs(fieldIfEmpty(adRequest.getOs()))
          .setUdId(fieldIfEmpty(adRequest.getUdid()));
    }
    return builder.build();
  }

  private static String fieldIfEmpty(String key){
    return Optional.ofNullable(key).orElse("");
  }

  private static Geo generateGeo(AdRequest adRequest){
    Geo.Builder builder = Geo.newBuilder();
    if(Optional.of(adRequest).isPresent()){
      builder.setCountry(fieldIfEmpty(adRequest.getRegion()))
          .setLat(adRequest.getLatitude())
          .setLon(adRequest.getLongitude());
    }
    return builder.build() ;
  }

  public static AdxDemandContext generateAdxDemandContext(System system,String demandPlacement){
    AdxDemandContext.Builder builder = AdxDemandContext.newBuilder();
    if(Optional.of(system).isPresent()){
      builder.setDemandId(system.getTbDemandId())
          .setPlacement(demandPlacement)
          .setSystemId(system.getId());
    }
    return builder.build();
  }

  public static AdxAppContext generateAdxAppContext(Context context){
    AdxAppContext.Builder builder = AdxAppContext.newBuilder() ;
    if(Optional.of(context.getAppSite()).isPresent()){
       builder.setBundle(fieldIfEmpty(context.getAppSite().getBundle()))
              .setDomain(fieldIfEmpty(context.getAppSite().getDomain()))
              .setName(fieldIfEmpty(context.getAppSite().getName()));
    }
    if(Optional.of(context.getAdCommonRequest()).isPresent()){
      builder.setVersion(fieldIfEmpty(context.getAdCommonRequest().getVersion()));
    }
    return builder.build() ;
  }

  public static AdxExtraContext generateAdxExtraContext(String abTestTag){
    return AdxExtraContext.newBuilder().setAbTestTag(fieldIfEmpty(abTestTag))
          .build();
  }

  public static AdxSupplyContext generateAdxSupplyContext(
      Supply supply,
      Placement supplyPlacement){
    AdxSupplyContext.Builder builder = AdxSupplyContext.newBuilder();
    if(Optional.of(supply).isPresent()){
      builder.setSupplyId(supply.getId());
    }
    if(Optional.of(supplyPlacement).isPresent()){
      builder.setPlacement(supplyPlacement.getName());
      if(Optional.of(supplyPlacement.getAdFormat()).isPresent()){
        builder.setAdFormat(supplyPlacement.getAdFormat().name());
      }
    }
    return builder.build();
  }

  private static AdxUserContext generateAdxUserContext(){
    return AdxUserContext.newBuilder()
        //.setKeywords("足球,篮球")
        //.setGender("F").setYob(1970)
        .build();
  }


  private static AdxSupplyRequest generateAdxSupplyRequest(){
    return AdxSupplyRequest.newBuilder()
        .build();
  }

  private static AdxCreativeImpression generateAdxCreativeImpression(){
    return AdxCreativeImpression.newBuilder()
        .build();
  }

  public static AdxCreativeClick generateAdxCreativeClick(){
    return AdxCreativeClick.newBuilder()
        .build();
  }

}
