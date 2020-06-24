/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.servlet;

import com.project.constants.Constants;
import com.project.constants.HvdicURLConstants;
import com.project.constants.JishoURLConstants;
import com.project.constants.URLConstant;
import com.project.crawler.CrawlerSaveToDB;
import com.project.crawler.hvdic.HvdicPageCrawler;
import com.project.crawler.jisho.JishoCrawler;
import com.project.jaxb.Words.Word;
import com.project.utils.MyStringUtils;
import com.project.utils.XMLUtils;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author Poca
 */
public class CrawlOneWordServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String wordParam = request.getParameter("txtWord");
        String unicode = request.getParameter("txtUnicode");
        System.out.println("Unicode 1: " + unicode);
        try {
            if (wordParam != null && wordParam.trim().length() > 0) {
                unicode = MyStringUtils.getWordUnicode(MyStringUtils.urlDecoder(wordParam, Constants.UTF8_ENCODE));
            } else {
                unicode = unicode.toUpperCase();
                if (!unicode.contains("U+")) {
                    unicode = "U+" + unicode;
                }
            }
            
            System.out.println("Unicode 2: " + unicode);
            ServletContext context = this.getServletContext();
            String realPath = context.getRealPath("/");
            JishoCrawler jishoCrawler = new JishoCrawler();
            HvdicPageCrawler hvdicCrawler = new HvdicPageCrawler();
            
            Word hvdicWord = hvdicCrawler.crawlToWord(HvdicURLConstants.PAGE_BASE_URL + unicode /*"%E8%B6%8A"*/, realPath);
            
            Word jishoWord = jishoCrawler.crawlToWord(hvdicWord.getWordString()/*"%E8%B6%8A"*/, realPath);
            hvdicWord.setWordDetails(XMLUtils.eliminateDuplicateWordInfo(hvdicWord.getWordDetails()));
            System.out.println("J: " + jishoWord.getUnicodeString());
            Word united = XMLUtils.combineWords(hvdicWord, jishoWord);
            CrawlerSaveToDB.writeWordToDB(united);
        } catch (SQLException ex) {
            Logger.getLogger(CrawlOneWordServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NamingException ex) {
            Logger.getLogger(CrawlOneWordServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(CrawlOneWordServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JAXBException ex) {
            Logger.getLogger(CrawlOneWordServlet.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
