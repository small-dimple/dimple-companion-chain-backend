package com.smlDimple.dimpleCompanionChain.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用的删除请求
 *
 * @Author: small-dimple
 **/
@Data
public class DeleteRequest implements Serializable {


    private static final long serialVersionUID = 5386056383624339501L;

    private long id;
}
