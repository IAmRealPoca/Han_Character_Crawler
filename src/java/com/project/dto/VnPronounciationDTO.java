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
public class VnPronounciationDTO implements Serializable{
    private String unicodeString;
    private String pronounciation;
    private String wordDesc;

    public VnPronounciationDTO() {
    }

    public VnPronounciationDTO(String unicodeString, String pronounciation, String wordDesc) {
        this.unicodeString = unicodeString;
        this.pronounciation = pronounciation;
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
     * @return the pronounciation
     */
    public String getPronounciation() {
        return pronounciation;
    }

    /**
     * @param pronounciation the pronounciation to set
     */
    public void setPronounciation(String pronounciation) {
        this.pronounciation = pronounciation;
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
