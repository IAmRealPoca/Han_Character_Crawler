/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.servlet;

import com.project.crawler.CrawlManager;
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
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author Poca
 */
public class CrawlCarefulServlet extends HttpServlet {

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
        String noOfPage = request.getParameter("txtNoOfPage");
//        String basePage = request.getParameter("txtBasePage");
        try {
            ServletContext context = this.getServletContext();
            String realPath = context.getRealPath("/");

//            JishoCrawler jishoCrawler = new JishoCrawler();
//            HvdicPageCrawler hvdicCrawler = new HvdicPageCrawler();
//            Word jishoWord = jishoCrawler.crawlToWord("%E8%B6%8A", realPath + URLConstant.JISHO_FILTER_FILEPATH);
//            Word hvdicWord = hvdicCrawler.crawlToWord("%E8%B6%8A", realPath);
//            hvdicWord.setWordDetails(XMLUtils.eliminateDuplicateWordInfo(hvdicWord.getWordDetails()));
//            Word united = XMLUtils.combineWords(hvdicWord, jishoWord);
//            CrawlerSaveToDB.writeWordToDB(united);
            CrawlManager crawlManager = new CrawlManager();
            if (noOfPage != null && !noOfPage.trim().isEmpty()) {
                crawlManager.setMaxPageToCrawl(Integer.parseInt(noOfPage));
            }
            crawlManager.crawlCareful("https://hvdic.thivien.net/pop-hv", realPath);
            
        } catch (TransformerException ex) {
            Logger.getLogger(CrawlCarefulServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JAXBException ex) {
            Logger.getLogger(CrawlCarefulServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(CrawlCarefulServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NamingException ex) {
            Logger.getLogger(CrawlCarefulServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (XMLStreamException ex) {
            Logger.getLogger(CrawlCarefulServlet.class.getName()).log(Level.SEVERE, null, ex);
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
