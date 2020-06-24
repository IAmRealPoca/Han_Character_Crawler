/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.dao;

import com.project.dto.JapPronounciationDTO;
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
public class JapanesePronounciationDAO implements Serializable {

    public boolean insertNewOnyomiPronounciation(String unicodeString, String pronounciation, String wordDesc)
            throws SQLException, NamingException {
        Connection con = null;
        PreparedStatement stm = null;
//        ResultSet rs = null;
        try {
            con = DBUtils.getConnection();
            if (con != null) {
                String sql = "INSERT INTO tblJapanesePronounciation("
                        + "onyomi, unicodeString, wordDesc) "
                        + "VALUES(?,?,?)";
                stm = con.prepareStatement(sql);
                stm.setNString(1, pronounciation);
                stm.setString(2, unicodeString);
                stm.setNString(3, wordDesc);
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

    public boolean insertNewKunyomiPronounciation(String unicodeString, String pronounciation, String wordDesc)
            throws SQLException, NamingException {
        Connection con = null;
        PreparedStatement stm = null;
//        ResultSet rs = null;
        try {
            con = DBUtils.getConnection();
            if (con != null) {
                String sql = "INSERT INTO tblJapanesePronounciation("
                        + "kunyomi, unicodeString, wordDesc) "
                        + "VALUES(?,?,?)";
                stm = con.prepareStatement(sql);
                stm.setNString(1, pronounciation);
                stm.setString(2, unicodeString);
                stm.setNString(3, wordDesc);
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

    public boolean checkIsOnyomiExisted(String unicodeString, String pronounciation)
            throws SQLException, NamingException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            con = DBUtils.getConnection();
            if (con != null) {
                String sql = "SELECT onyomi, unicodeString FROM tblJapanesePronounciation "
                        + "WHERE unicodeString = ? AND onyomi = ?";
                stm = con.prepareStatement(sql);
                stm.setString(1, unicodeString);
                stm.setNString(2, pronounciation);
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

    public boolean checkIsKunyomiExisted(String unicodeString, String pronounciation)
            throws SQLException, NamingException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            con = DBUtils.getConnection();
            if (con != null) {
                String sql = "SELECT kunyomi, unicodeString FROM tblJapanesePronounciation "
                        + "WHERE unicodeString = ? AND kunyomi = ?";
                stm = con.prepareStatement(sql);
                stm.setString(1, unicodeString);
                stm.setNString(2, pronounciation);
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

    public List<JapPronounciationDTO> getPronounciation(String unicodeString)
            throws SQLException, NamingException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            con = DBUtils.getConnection();
            if (con != null) {
                String sql = "SELECT kunyomi, onyomi, unicodeString, wordDesc "
                        + "FROM tblJapanesePronounciation "
                        + "WHERE unicodeString = ?";
                stm = con.prepareStatement(sql);
                stm.setString(1, unicodeString);
                rs = stm.executeQuery();
                List<JapPronounciationDTO> result = new ArrayList<>();
                while (rs.next()) {
//                    String unicodeResult = rs.getString("unicodeString");
                    String kunyomi = rs.getNString("kunyomi");
                    String onyomi = rs.getNString("onyomi");
                    String wordDesc = rs.getNString("wordDesc");
                    JapPronounciationDTO dto = new JapPronounciationDTO(unicodeString, onyomi, kunyomi, wordDesc);
                    result.add(dto);
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
