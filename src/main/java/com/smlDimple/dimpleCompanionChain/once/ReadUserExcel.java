package com.smlDimple.dimpleCompanionChain.once;

import com.alibaba.excel.EasyExcel;
import com.google.gson.Gson;
import com.smlDimple.dimpleCompanionChain.model.dto.ExcelQuery;
import com.smlDimple.dimpleCompanionChain.utils.ExcelListenerUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * @Author: small-dimple
 **/
@Slf4j
public class ReadUserExcel {


    /**
     * 方式一：
     * 下面是通过监听器的方式读取Excel文件
     */
    public void indexOrNameRead() {
//        String fileName = TestFileUtil.getPath() + "demo" + File.separator + "demo.xlsx";
        String fileName = "";//输入读取的文件路径
        // 这里默认读取第一个sheet
        EasyExcel.read(fileName, ExcelQuery.class, new ExcelListenerUtil()).sheet().doRead();
    }

    /**
     * 方式二：
     * 同步的返回，不推荐使用，如果数据量大会把数据放到内存里面
     */

    Gson gson = new Gson();
    public void synchronousRead() {
//        String fileName = TestFileUtil.getPath() + "demo" + File.separator + "demo.xlsx";
        String fileName = "";//输入读取的文件路径
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 同步读取会自动finish
        List<ExcelQuery> excelUtilList = EasyExcel.read(fileName).head(ExcelQuery.class).sheet().doReadSync();
        for (ExcelQuery data : excelUtilList) {
            log.info("读取到数据:{}", gson.toJson(data));
        }
        // 这里 也可以不指定class，返回一个list，然后读取第一个sheet 同步读取会自动finish
        List<Map<Integer, String>> listMap = EasyExcel.read(fileName).sheet().doReadSync();
        for (Map<Integer, String> data : listMap) {
            // 返回每条数据的键值对 表示所在的列 和所在列的值
            log.info("读取到数据:{}", gson.toJson(data));
        }
    }
}
