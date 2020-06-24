/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.crawler;

import com.project.constants.Constants;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 *
 * @author Poca
 */
public class MainCrawler implements Serializable {

    private String userAgent;
    private String defaultUserAgent;

    public MainCrawler() {
        userAgent = "User-Agent";
        defaultUserAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.132 Safari/537.36";
    }

    public MainCrawler(String userAgent, String defaultUserAgent) {
        this.userAgent = userAgent;
        this.defaultUserAgent = defaultUserAgent;
    }

    private URLConnection getURLConnectionFromURL(String urlString)
            throws IOException {
        URL url = new URL(urlString);
        URLConnection con = url.openConnection();
        con.setRequestProperty(getUserAgent(), getDefaultUserAgent());
        return con;
    }

    public String getWebsiteContent(String urlString) throws IOException {
//        URLConnection con = this.getURLConnectionFromURL(urlString);
        trustCertTest();
        URL url = new URL(urlString);
        URLConnection con = url.openConnection();
        con.setRequestProperty(getUserAgent(), getDefaultUserAgent());
        InputStreamReader is = null;
        BufferedReader br = null;
        try {
            if (con != null) {
                is = new InputStreamReader(con.getInputStream(), Constants.UTF8_ENCODE);
                br = new BufferedReader(is);
                String body = "";
                String temp;
                while ((temp = br.readLine()) != null) {
                    body += temp + "\r\n";
                }
                return body;
            }
        } finally {
            if (br != null) {
                br.close();
            }
            if (is != null) {
                is.close();
            }
        }
        return null;
    }

    /**
     * @return the userAgent
     */
    public String getUserAgent() {
        return userAgent;
    }

    /**
     * @param userAgent the userAgent to set
     */
    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    /**
     * @return the defaultUserAgent
     */
    public String getDefaultUserAgent() {
        return defaultUserAgent;
    }

    /**
     * @param defaultUserAgent the defaultUserAgent to set
     */
    public void setDefaultUserAgent(String defaultUserAgent) {
        this.defaultUserAgent = defaultUserAgent;
    }

    public static void trustCertTest() {
        TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {

                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                    //No need to implement.
                }

                public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                    //No need to implement.
                }
            }
        };

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
