/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.servlet;

import com.project.constants.HvdicURLConstants;
import com.project.constants.URLConstant;
import com.project.crawler.PageManager;
import com.project.crawler.hvdic.HvdicPageCrawler;
import com.project.crawler.hvdic.HvdicRadicalCrawler;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import org.xml.sax.SAXException;

/**
 *
 * @author Poca
 */
public class CrawlServlet extends HttpServlet {

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
//        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String txtUrl = request.getParameter("txtUrl");
        System.out.println("txtUrl: " + txtUrl);
        String url = URLConstant.ERROR_PAGE;
        try {
            HvdicPageCrawler hvdicPageCrawler = new HvdicPageCrawler();
            ServletContext context = this.getServletContext();
            String realPath = context.getRealPath("/");
            String filePath = realPath + "document/xsl/hvdic_filter.xsl";
            hvdicPageCrawler.crawlAndSaveToDB(txtUrl, realPath);

//            HvdicRadicalCrawler hvdicRadicalCrawler = new HvdicRadicalCrawler();
//            List<String> words = hvdicRadicalCrawler.crawl("https://hvdic.thivien.net/pop-hv", filePath);
//            String str = "";
//            for (String word : words) {
//                str += word + " ";
//            }
//            out.print(str);

//            Set<String> a = new HashSet<>();
//            List<String> visit = new ArrayList<>();
//            visit.add("https://hvdic.thivien.net/rad-hv");
//            PageManager pageManager = new PageManager(a, visit);
//            long startTime = System.nanoTime();
//            pageManager.crawlAll();
//            long endTime = System.nanoTime() - startTime;
//            System.out.println("Crawl time: " 
//                    + TimeUnit.SECONDS.convert(endTime, TimeUnit.NANOSECONDS) + " seconds");
//        } catch (ParserConfigurationException ex) {
//            Logger.getLogger(CrawlServlet.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (SAXException ex) {
//            Logger.getLogger(CrawlServlet.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (XPathExpressionException ex) {
//            Logger.getLogger(CrawlServlet.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (SQLException ex) {
//            Logger.getLogger(CrawlServlet.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (NamingException ex) {
//            Logger.getLogger(CrawlServlet.class.getName()).log(Level.SEVERE, null, ex);
//        } 
        } catch (Exception e) {
            Logger.getLogger(CrawlServlet.class.getName()).log(Level.SEVERE, null, e);
        } finally {
//            RequestDispatcher rd = request.getRequestDispatcher(url);
//            rd.forward(request, response);
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
