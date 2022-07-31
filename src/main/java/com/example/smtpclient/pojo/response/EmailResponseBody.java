package com.example.smtpclient.pojo.response;

import com.example.smtpclient.entity.Email;
import lombok.Builder;
import lombok.Data;

import java.util.List;

public class EmailResponseBody {

  @Data
  @Builder
  public static class GetEmailRes {
    public long total;
    public List<Email> emailList;
  }

  @Data
  @Builder
  public static class GetDraftRes {
    public long total;
    public List<Email> draftList;
  }

  @Data
  @Builder
  public static class GetStaredRes {
    public long total;
    public List<Email> staredList;
  }

  @Data
  @Builder
  public static class GetDeletedRes {
    public long total;
    public List<Email> deletedList;
  }

  @Data
  @Builder
  public static class GetSendRes {
    public long total;
    public List<Email> sendList;
  }
}
