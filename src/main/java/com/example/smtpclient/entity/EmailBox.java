package com.example.smtpclient.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("email_box")
@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class EmailBox implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 邮件id
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 发送者邮箱
     */
    private String fromEmail;

    /**
     * 接收者邮箱
     */
    private String toEmail;

    /**
     * 发送时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date sendTime;

    /**
     * 大小，字节
     */
    private Integer size;

    /**
     * 内容
     */
    private String data;

    /**
     * 是否加载过，1已加载
     */
    private Integer state;

}
