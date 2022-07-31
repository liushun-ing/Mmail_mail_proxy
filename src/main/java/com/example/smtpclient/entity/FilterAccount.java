package com.example.smtpclient.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("filter_account")
@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class FilterAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Integer id;

    /**
     * 过滤的账号
     */
    private String account;

}
