package com.smlDimple.dimpleCompanionChain.model.enums;


import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 队伍状态枚举类
 *
 * @Author: small-dimple
 **/

@Getter
public enum TeamStatusEnum {


    PUBLICITY(0, "正常"),
    PRIVATIZE(1, "私有"),
    ENCRYPT(2, "加密");

    private final int value;
    private final String msg;

    // 获取枚举 判断枚举类中的参数是否包含传进的参数
    public static TeamStatusEnum getEnumByValue(Integer value) {
        if (value == null) {
            return null;
        }
        TeamStatusEnum[] values = TeamStatusEnum.values();
        for (TeamStatusEnum teamStatusEnum : values) {

            if (teamStatusEnum.getValue() == value) {
                return teamStatusEnum;
            }
        }
        return null;
    }


    TeamStatusEnum(int value, String msg) {
        this.value = value;
        this.msg = msg;
    }


}
