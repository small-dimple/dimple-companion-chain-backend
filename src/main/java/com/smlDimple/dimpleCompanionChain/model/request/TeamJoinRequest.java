package com.smlDimple.dimpleCompanionChain.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户加入队伍请求体
 *
 * @Author: small-dimple
 **/
@Data
public class TeamJoinRequest implements Serializable {

    private static final long serialVersionUID = -6393938879919389082L;

    /**
     * 队伍 id
     *
     */
    private Long teamId;


    /**
     * 密码
     */
    private String password;



}
