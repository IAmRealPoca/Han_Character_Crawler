/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.dao;

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
public class WordToWordTypeDAO implements Serializable{
    public boolean insertNewWord(String unicodeString, int wordTypeId) 
            throws SQLException, NamingException {
        Connection con = null;
        PreparedStatement stm = null;
//        ResultSet rs = null;
        try {
            con = DBUtils.getConnection();
            if (con != null) {
                String sql = "INSERT INTO tblWordToWordType(unicodeString, wordTypeId) "
                        + "VALUES(?,?)";
                stm = con.prepareStatement(sql);
                stm.setString(1, unicodeString);
                stm.setInt(2, wordTypeId);
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
    
    public boolean checkIsExisted(String unicodeString, int wordTypeId)
            throws SQLException, NamingException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            con = DBUtils.getConnection();
            if (con != null) {
                String sql = "SELECT unicodeString, wordTypeId FROM tblWordToWordType "
                        + "WHERE unicodeString = ? AND wordTypeId = ?";
                stm = con.prepareStatement(sql);
                stm.setString(1, unicodeString);
                stm.setInt(2, wordTypeId);
                rs = stm.executeQuery();
                if (rs.next()) {
                    return true;
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
        return false;
    }
    
    public List<Integer> getWordToWordType(String unicodeString)
            throws SQLException, NamingException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            con = DBUtils.getConnection();
            if (con != null) {
                String sql = "SELECT unicodeString, wordTypeId "
                        + "FROM tblWordToWordType "
                        + "WHERE unicodeString = ?";
                stm = con.prepareStatement(sql);
                stm.setString(1, unicodeString);
                rs = stm.executeQuery();
                List<Integer> result = new ArrayList<>();
                while (rs.next()) {
//                    String unicodeResult = rs.getString("unicodeString");
                    int wordTypeId = rs.getInt("wordTypeId");
                    result.add(wordTypeId);
                }
                return result;
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
