/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.dto;

import java.io.Serializable;

/**
 *
 * @author Poca
 */
public class JapPronounciationDTO implements Serializable{
    private String unicodeString;
    private String onyomi;
    private String kunyomi;
    private String wordDesc;

    public JapPronounciationDTO() {
    }

    public JapPronounciationDTO(String unicodeString, String onyomi, String kunyomi, String wordDesc) {
        this.unicodeString = unicodeString;
        this.onyomi = onyomi;
        this.kunyomi = kunyomi;
        this.wordDesc = wordDesc;
    }

    /**
     * @return the unicodeString
     */
    public String getUnicodeString() {
        return unicodeString;
    }

    /**
     * @param unicodeString the unicodeString to set
     */
    public void setUnicodeString(String unicodeString) {
        this.unicodeString = unicodeString;
    }

    /**
     * @return the onyomi
     */
    public String getOnyomi() {
        return onyomi;
    }

    /**
     * @param onyomi the onyomi to set
     */
    public void setOnyomi(String onyomi) {
        this.onyomi = onyomi;
    }

    /**
     * @return the kunyomi
     */
    public String getKunyomi() {
        return kunyomi;
    }

    /**
     * @param kunyomi the kunyomi to set
     */
    public void setKunyomi(String kunyomi) {
        this.kunyomi = kunyomi;
    }

    /**
     * @return the wordDesc
     */
    public String getWordDesc() {
        return wordDesc;
    }

    /**
     * @param wordDesc the wordDesc to set
     */
    public void setWordDesc(String wordDesc) {
        this.wordDesc = wordDesc;
    }
    
    
}
