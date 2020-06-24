/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.constants;

import com.project.utils.MyStringUtils;

/**
 *
 * @author Poca
 */
public enum PronounciationTypeEnum {
    VIE_HANVIET("hanviet"),
    JAP_ONYOMI("onyomi"),
    JAP_KUNYOMI("kunyomi"),
    KOR_KOREAN("korean");
    
    private String name;

    private PronounciationTypeEnum(String name) {
        this.name = name;
    }
    
    public static PronounciationTypeEnum getProTypeFromStringName(String name) {
        if (!MyStringUtils.checkStringAvailable(name)) {
            return null;
        }
        for (PronounciationTypeEnum type : PronounciationTypeEnum.values()) {
            if (type.name.equals(name)) {
                return type;
            }
        }
        return null;
    }
}
