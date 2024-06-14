package com.smlDimple.dimpleCompanionChain.model.request;

import lombok.Data;
import java.io.Serializable;

/**
 * 用户注册请求体
 *
 * @author: small-dimple
 */
@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 用户密码
     */
    private String userPassword;

    /**
     * 校验密码
     */
    private String checkPassword;

    /**
     * 个人编号
     */
    private String orderNum;
}
