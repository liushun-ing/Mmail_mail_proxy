package com.example.smtpclient.exception;

public class UserInvalidException extends RuntimeException{

  private static final String msg = "user is invalid";

  public UserInvalidException() {
    super(msg);
  }
}
