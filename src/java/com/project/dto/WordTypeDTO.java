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
public class WordTypeDTO implements Serializable{
    private int id;
    private String wordTypeName;

    public WordTypeDTO() {
    }

    public WordTypeDTO(int id, String wordTypeName) {
        this.id = id;
        this.wordTypeName = wordTypeName;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the wordTypeName
     */
    public String getWordTypeName() {
        return wordTypeName;
    }

    /**
     * @param wordTypeName the wordTypeName to set
     */
    public void setWordTypeName(String wordTypeName) {
        this.wordTypeName = wordTypeName;
    }
    
    
}
