package com.example.smtpclient.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.*;

import java.io.Serializable;

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
public class Server implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Integer id;

    /**
     * 域名，初始值为hnu.com
     */
    private String domainName;

    /**
     * 邮箱大小，即服务器可以存放的邮件的数量
     */
    private Integer mailboxSize;

    /**
     * smtp服务端口
     */
    private Integer smtpPort;

    /**
     * server服务端口
     */
    private Integer pop3Port;

    /**
     * 一封邮件最大收件人数
     */
    private Integer maxRcpt;

}
