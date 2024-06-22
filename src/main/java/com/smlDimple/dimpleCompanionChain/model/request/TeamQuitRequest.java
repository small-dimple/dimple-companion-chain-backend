package com.smlDimple.dimpleCompanionChain.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户退出队伍请求体
 *
 * @Author: small-dimple
 **/
@Data
public class TeamQuitRequest implements Serializable {

    private static final long serialVersionUID = -6393938879919389082L;

    /**
     * 队伍 id
     *
     */
    private Long teamId;






}
