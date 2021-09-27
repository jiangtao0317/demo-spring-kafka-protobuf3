package com.mobiu.kafka.util;

import com.google.protobuf.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * @Author:jiangtao
 * @Date:2021/9/24 6:01 下午
 * @Desc:
 */
public class TimeUtils {

  public static Timestamp localDateTimeToTimestamp(LocalDateTime localDateTime,ZoneOffset zoneOffset){
    Instant instant = localDateTime.toInstant(zoneOffset);
    Timestamp timestamp = Timestamp.newBuilder()
        .setSeconds(instant.getEpochSecond())
        .setNanos(instant.getNano())
        .build();
    return timestamp ;
  }

  public static LocalDateTime timestampToLocalDateTime(Timestamp timestamp,ZoneOffset zoneOffset){
    LocalDateTime localDateTime = LocalDateTime.ofEpochSecond(timestamp.getSeconds(),0,zoneOffset);
    return localDateTime ;
  }

  public static void main(String[] args) {
    LocalDateTime localDateTime = LocalDateTime.ofEpochSecond(1632511146,0,ZoneOffset.ofHours(0));
    System.out.println(localDateTime);
    Timestamp timestamp = localDateTimeToTimestamp(localDateTime,ZoneOffset.ofHours(0));
    System.out.println(timestamp);
  }

}
