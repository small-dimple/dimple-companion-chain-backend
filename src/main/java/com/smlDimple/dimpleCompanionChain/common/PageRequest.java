package com.smlDimple.dimpleCompanionChain.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用分页请求
 *
 * @Author: small-dimple
 **/
@Data
public class PageRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 当前页码
     */
    private int pageNum = 1;

    /**
     * 每页条数
     */
    private int pageSize = 10;
}
