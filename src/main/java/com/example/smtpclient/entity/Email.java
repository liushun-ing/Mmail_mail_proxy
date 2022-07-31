package com.example.smtpclient.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author ls
 * @since 2022-04-30
 */
@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Email implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 邮件id
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String emailId;

    /**
     * 发件人昵称
     */
    private String fromName;

    /**
     * 发件人邮箱
     */
    private String fromEmail;

    /**
     * 收件人昵称
     */
    private String toName;

    /**
     * 收件人邮箱
     */
    private String toEmail;

    /**
     * 发件时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date sendTime;

    /**
     * 大小
     */
    private Integer size;

    /**
     * 整体内容
     */
    private String data;

    /**
     * 主题
     */
    private String subject;

    /**
     * 解析后的内容
     */
    private String content;

    /**
     * 是否标记
     */
    private Integer stared;

    /**
     * 是否删除
     */
    private Integer deleted;

    /**
     * 是否查看
     */
    private Integer seen;

    /**
     * 是否是草稿
     */
    private Integer draft;

    /**
     * 是发件还是收件 0收件 1发件
     */
    private Integer isSend;

    /**
     * 删除时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deleteTime;

}
