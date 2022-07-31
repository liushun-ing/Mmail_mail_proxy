package com.example.smtpclient.pojo.request;

import lombok.Builder;
import lombok.Data;

public class EmailRequestBody {

  @Data
  @Builder
  public static class GetEmailReq {
    private long pageSize;
    private long currentPage;
    private String searchKey;
    private String account;
  }

  @Data
  @Builder
  public static class GetDraftReq {
    private long pageSize;
    private long currentPage;
    private String searchKey;
    private String account;
  }

  @Data
  @Builder
  public static class GetStaredReq {
    private long pageSize;
    private long currentPage;
    private String searchKey;
    private String account;
  }

  @Data
  @Builder
  public static class GetDeletedReq {
    private long pageSize;
    private long currentPage;
    private String searchKey;
    private String account;
  }

  @Data
  @Builder
  public static class GetSendReq {
    private long pageSize;
    private long currentPage;
    private String searchKey;
    private String account;
  }
}
