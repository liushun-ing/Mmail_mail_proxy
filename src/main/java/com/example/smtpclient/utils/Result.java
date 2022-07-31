package com.example.smtpclient.utils;

import lombok.Data;

@Data
public class Result<T> {
  private int code;
  private String msg;
  private T data;


  public static Result<Void> error(ResultEnum resultEnum) {
    final Result<Void> result = new Result<>();
    result.setCode(resultEnum.getCode());
    result.setMsg(resultEnum.getMsg());
    result.setData(null);
    return result;
  }

  public static Result<Void> success(ResultEnum resultEnum) {
    final Result<Void> result = new Result<>();
    result.setCode(resultEnum.getCode());
    result.setMsg(result.getMsg());
    result.setData(null);
    return result;
  }

  public static <T> Result<T> error(T data, ResultEnum resultEnum) {
    final Result<T> result = new Result<>();
    result.setCode(resultEnum.getCode());
    result.setMsg(resultEnum.getMsg());
    result.setData(data);
    return result;
  }

  public static <T> Result<T> success(T data, ResultEnum resultEnum) {
    final Result<T> result = new Result<>();
    result.setCode(resultEnum.getCode());
    result.setMsg(resultEnum.getMsg());
    result.setData(data);
    return result;
  }


  public static <T> Result<T> success(T data) {
    final Result<T> result = new Result<>();
    result.setCode(ResultEnum.SUCCESS.getCode());
    result.setMsg(ResultEnum.SUCCESS.getMsg());
    result.setData(data);
    return result;
  }

  public static <T> Result<T> error(T data) {
    final Result<T> result = new Result<>();
    result.setCode(ResultEnum.DEFAULT_ERROR.getCode());
    result.setMsg(ResultEnum.DEFAULT_ERROR.getMsg());
    result.setData(data);
    return result;
  }

  public static <T> Result<T> error(String msg) {
    final Result<T> result = new Result<>();
    result.setCode(ResultEnum.DEFAULT_ERROR.getCode());
    result.setMsg(msg);
    result.setData(null);
    return result;
  }

  public static <Void> Result<Void> success() {
    final Result<Void> result = new Result<>();
    result.setCode(ResultEnum.SUCCESS.getCode());
    result.setMsg(ResultEnum.SUCCESS.getMsg());
    result.setData(null);
    return result;
  }

  public static <Void> Result<Void> error() {
    final Result<Void> result = new Result<>();
    result.setCode(ResultEnum.DEFAULT_ERROR.getCode());
    result.setMsg(ResultEnum.DEFAULT_ERROR.getMsg());
    result.setData(null);
    return result;
  }

  public static <Void> Result<Void> error(int code, String msg) {
    final Result<Void> result = new Result<>();
    result.setCode(code);
    result.setMsg(msg);
    result.setData(null);
    return result;
  }
}

