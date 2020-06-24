/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.utils;

import com.project.constants.Constants;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import org.apache.commons.text.StringEscapeUtils;

/**
 *
 * @author Poca
 */
public class MyStringUtils {

    public static boolean checkStringAvailable(String str) {
        if (str == null) {
            return false;
        }
        if (str.trim().isEmpty()) {
            return false;
        }
        return true;
    }
    
    public static int computeMatchingDensity(String a, String b) {
        int n = a.length();
        int m = b.length();
        int dp[][] = new int[n+1][m+1];
        
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (a.charAt(i) == b.charAt(j)) {
                    dp[i+1][j+1] = dp[i][j] + 1;
                }
                else {
                    dp [i+1][j+1] = Math.max(dp[i+1][j], dp[i][j+1]);
                }
            }
        }
        return dp[n][m];
    }
    
    /*
     * https://en.wikipedia.org/wiki/Unicode
     * https://www.baeldung.com/java-char-encoding
     * https://stackoverflow.com/questions/2006533/how-can-i-get-a-unicode-characters-code#comment50544332_2006544
     * 
     * Apache Commons Text/Lang does provide methods to process with unicode and URL encode
     * but 
     */
    public static String getWordUnicode(String word) {
        if (word == null) {
            return null;
        }
        if (word.trim().isEmpty()) {
            return "";
        }
        if (word.length() > 1) {
            return word;
        }
        return getWordUnicode(word.charAt(0));
    }

    public static String getWordUnicode(char word) {
        return Integer.toHexString(word);
    }

    public static String urlEncoder(String url, String encode) throws UnsupportedEncodingException {
        if (url == null) {
            return null;
        }
        if (url.trim().isEmpty()) {
            return "";
        }
        if (encode == null || encode.trim().isEmpty()) {
            encode = Constants.UTF8_ENCODE;
        }
        return URLEncoder.encode(url, encode);
    }
    
    public static String urlDecoder(String url, String encode) throws UnsupportedEncodingException {
        if (url == null) {
            return null;
        }
        if (url.trim().isEmpty()) {
            return "";
        }
        if (encode == null || encode.trim().isEmpty()) {
            encode = Constants.UTF8_ENCODE;
        }
        return URLDecoder.decode(url, encode);
    }

    public static String unescapeXMLEntities(String str) {
        if (str == null) {
            return null;
        }
        if (str.trim().isEmpty()) {
            return "";
        }
        String value = str
                .replace("&amp;", "&")
                .replaceAll("&quot;", "\"")
                .replaceAll("&apos;", "'")
                .replaceAll("&lt;", "<")
                .replaceAll("&gt;", ">");
        return value;
    }

    public static String unescapeHTML4(String str) {
        if (str == null) {
            return null;
        }
        if (str.trim().isEmpty()) {
            return "";
        }
        String escapedEntities = unescapeXMLEntities(str);
        return StringEscapeUtils.unescapeHtml4(escapedEntities);
    }

    public static String buildHvdicURLWithURLUnicode(String url) {
        if (url == null) {
            return null;
        }
        if (url.trim().isEmpty()) {
            return "";
        }
        String lastWord = url.substring(url.lastIndexOf("/") + 1);
        String unicode = "U+" + getWordUnicode(lastWord);
        String result = url.substring(0, url.lastIndexOf("/") + 1) + unicode;
        return result;
    }

    public static String buildHvdicURLWithURLEscapedWord(String url) throws UnsupportedEncodingException {
        if (url == null) {
            return null;
        }
        if (url.trim().isEmpty()) {
            return "";
        }
//        String lastWordUnicode = MyStringUtils.getWordUnicode(url.charAt(url.length()-1));
        String lastWord = url.substring(url.lastIndexOf("/") + 1);
        String urlEncoded = urlEncoder(lastWord, Constants.UTF8_ENCODE);
        String result = url.substring(0, url.lastIndexOf("/") + 1) + urlEncoded;
        return result;
    }
    
    public static String buildHvdicURLWithHTMLEscapedWord(String url) throws UnsupportedEncodingException {
        if (url == null) {
            return null;
        }
        if (url.trim().isEmpty()) {
            return "";
        }
        String lastWord = url.substring(url.lastIndexOf("/") + 1);
        String htmlEscaped = unescapeHTML4(lastWord);
        String urlEncoded = urlEncoder(htmlEscaped, Constants.UTF8_ENCODE);
        String result = url.substring(0, url.lastIndexOf("/") + 1) + urlEncoded;
        return result;
    }
}
