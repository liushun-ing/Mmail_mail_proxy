package com.example.smtpclient.utils;

import org.apache.commons.codec.binary.Base64;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Utils {

  /** 获取当前时间 */
  public static String getCurrentTime() {
    Date date = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    return sdf.format(date);
  }

  /**
   * 根据时间戳获取日期
   */
  public static Date getDate(String s) throws ParseException {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss" );
    long l = Long.parseLong(s);
    String format = sdf.format(l);
    return sdf.parse(format);
  }

  /**
   * 获取三十天之前的日期
   * @return
   */
  public static Date get30Date() {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(new Date());
    calendar.add(calendar.DATE, -30);
    return calendar.getTime();
  }


  /** 将base64编码转换为utf8编码的字符串 */
  public static String getBase64FromUtf8(String utf8String) {
    return Base64.encodeBase64String(utf8String.getBytes(StandardCharsets.UTF_8));
  }

  /** 将base64编码转换为utf8编码的字符串 */
  public static String getUtf8FromBase64(String base64String) throws Exception {
    return new String(
        Base64.decodeBase64(base64String.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
  }

  //    去除base64的前端识别字符串
  public static String removeTheHead(String base64){
    for (int i = 0; i < base64.length(); i++) {
      if (base64.charAt(i)==','){
        base64=base64.substring(i+1,base64.length());
        break;
      }
    }
    return base64;
  }
}
