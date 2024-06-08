package com.smlDimple.dimpleCompanionChain.model.domain;


import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 创建Excel的实体类
 * 这里提供一个Excel的实体类，用于读取Excel文件
 *
 * @author small-dimple
 */
@Data
public class ExcelEntity {


    // 设置excel表头名称
    @ExcelProperty(value = "姓名", index = 0)
    private String name;

    @ExcelProperty(value = "年龄", index = 1)
    private Integer age;

    @ExcelProperty(value = "生日", index = 2)
    private Date birthday;
}