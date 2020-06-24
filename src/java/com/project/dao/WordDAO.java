/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.dao;

import com.project.dto.WordDTO;
import com.project.utils.DBUtils;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.naming.NamingException;

/**
 *
 * @author Poca
 */
public class WordDAO implements Serializable{
    
    public boolean insertNewWord(WordDTO dto) throws SQLException, NamingException {
        Connection con = null;
        PreparedStatement stm = null;
//        ResultSet rs = null;
        try {
            con = DBUtils.getConnection();
            if (con != null) {
                String sql = "INSERT INTO tblWord(unicodeString, word, "
                        + "isMainUse, noOfStroke) VALUES(?,?,?,?)";
                stm = con.prepareStatement(sql);
                stm.setString(1, dto.getUnicodeString());
                stm.setNString(2, dto.getWord());
                stm.setBoolean(3, dto.isIsMainUse());
                stm.setInt(4, dto.getNoOfStroke());
                int result = stm.executeUpdate();
                if (result > 0) {
                    return true;
                }
            }
        } finally {
//            if (rs != null) {
//                rs.close();
//            }
            if (stm != null) {
                stm.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return false;
    }
    
    public WordDTO getWord(String unicode) throws SQLException, NamingException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            con = DBUtils.getConnection();
            if (con != null) {
                String sql = "SELECT w.word, w.isMainUse, w.noOfStroke "
                        + "FROM tblWord w "
                        + "WHERE unicodeString = ?";
                stm = con.prepareStatement(sql);
                stm.setString(1, unicode);
//                stm.setNString(2, dto.getWord());
//                stm.setNString(3, dto.getWordDesc());
//                stm.setBoolean(4, dto.isIsMainUse());
//                stm.setNString(5, dto.getChinesePronounciation());
//                stm.setNString(6, dto.getVietnamesePronounciation());
                rs = stm.executeQuery();
                while (rs.next()) {
                    String word = rs.getNString("word");
                    boolean isMainUse = rs.getBoolean("isMainUse");
                    int noOfStroke = rs.getInt("noOfStroke");
                    WordDTO dto = new WordDTO(unicode, word, noOfStroke, isMainUse);
                    return dto;
                }
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stm != null) {
                stm.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return null;
    }
    
    public List<WordDTO> getAllWord() throws SQLException, NamingException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            con = DBUtils.getConnection();
            if (con != null) {
                String sql = "SELECT w.unicodeString, w.word, w.isMainUse, w.noOfStroke "
                        + "FROM tblWord w ";
                stm = con.prepareStatement(sql);
//                stm.setNString(2, dto.getWord());
//                stm.setNString(3, dto.getWordDesc());
//                stm.setBoolean(4, dto.isIsMainUse());
//                stm.setNString(5, dto.getChinesePronounciation());
//                stm.setNString(6, dto.getVietnamesePronounciation());
                rs = stm.executeQuery();
                List<WordDTO> listDTO = null;
                while (rs.next()) {
                    if (listDTO == null) {
                        listDTO = new ArrayList<>();
                    }
                    String unicode = rs.getString("unicodeString");
                    String word = rs.getNString("word");
                    boolean isMainUse = rs.getBoolean("isMainUse");
                    int noOfStroke = rs.getInt("noOfStroke");
                    WordDTO dto = new WordDTO(unicode, word, noOfStroke, isMainUse);
                    listDTO.add(dto);
                }
                return listDTO;
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stm != null) {
                stm.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return null;
    }
}
