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
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String userId;

    /**
     * 用户名
     */
    private String account;

    /**
     * 密码
     */
    private String password;

    /**
     * 电话
     */
    private String phone;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 1 普通用户 0 管理员
     */
    private Integer type;

    /**
     * 权限
     */
    private Integer authority;

    /**
     * 头像图片的base64编码，可以直接在前端显示
     */
    private String avatar;

    /**
     * 是否禁用
     */
    private Integer isDisabled;

    /**
     * 注册时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date registerTime;

}
