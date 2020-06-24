/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.dto;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Poca
 */
public class WordDTO implements Serializable {

    private String unicodeString;
    private String word;
    private int noOfStroke;
    private boolean isMainUse;
    private List<Integer> wordTypes;

    public WordDTO() {
    }

    public WordDTO(String unicodeString, String word, int noOfStroke, boolean isMainUse) {
        this.unicodeString = unicodeString;
        this.word = word;
        this.noOfStroke = noOfStroke;
        this.isMainUse = isMainUse;
    }

    public WordDTO(String unicodeString, String word, boolean isMainUse, List<Integer> wordTypes) {
        this.unicodeString = unicodeString;
        this.word = word;
        this.isMainUse = isMainUse;
        this.wordTypes = wordTypes;
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
     * @return the word
     */
    public String getWord() {
        return word;
    }

    /**
     * @param word the word to set
     */
    public void setWord(String word) {
        this.word = word;
    }

    /**
     * @return the isMainUse
     */
    public boolean isIsMainUse() {
        return isMainUse;
    }

    /**
     * @param isMainUse the isMainUse to set
     */
    public void setIsMainUse(boolean isMainUse) {
        this.isMainUse = isMainUse;
    }

    /**
     * @return the wordTypes
     */
    public List<Integer> getWordTypes() {
        return wordTypes;
    }

    /**
     * @param wordTypes the wordTypes to set
     */
    public void setWordTypes(List<Integer> wordTypes) {
        this.wordTypes = wordTypes;
    }

    /**
     * @return the noOfStroke
     */
    public int getNoOfStroke() {
        return noOfStroke;
    }

    /**
     * @param noOfStroke the noOfStroke to set
     */
    public void setNoOfStroke(int noOfStroke) {
        this.noOfStroke = noOfStroke;
    }

}
