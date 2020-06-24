/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.crawler;

import com.project.constants.HvdicURLConstants;
import com.project.crawler.hvdic.HvdicPageCrawler;
import com.project.crawler.hvdic.HvdicRadicalCrawler;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;
import org.xml.sax.SAXException;

/**
 *
 * @author Poca
 */
public class PageManager {

    private static int MAX_PAGES_TO_SEARCH = 10;
    private int noOfPageToSearch;
    private Set<String> pageVisited;
    private List<String> pageToVisit;

    public PageManager() {
        this.pageVisited = new HashSet<>();
        this.pageToVisit = new ArrayList<>();
    }

    public PageManager(Set<String> pageVisited, List<String> pageToVisit) {
        this.pageVisited = pageVisited;
        this.pageToVisit = pageToVisit;
    }

    public void crawlAll()
            throws IOException, ParserConfigurationException, SAXException, XPathExpressionException, SQLException, NamingException, TransformerException, TransformerConfigurationException, JAXBException {

        HvdicPageCrawler pageCrawler = new HvdicPageCrawler();
        HvdicRadicalCrawler radicalCrawler = new HvdicRadicalCrawler();
        //build list of page to crawl
        while (pageVisited.size() < 20) {
            String currentUrl;
            if (pageToVisit.isEmpty()) {
                // "/rad-hv/" to "/rad-hv"
                currentUrl = HvdicURLConstants.RADICAL_PAGE_URL.substring(0, HvdicURLConstants.RADICAL_PAGE_URL.length() - 1);
                pageVisited.add(currentUrl);
            } else {
                currentUrl = this.nextUrl();
            }
            List<String> newLinks = null;
            if (currentUrl.contains("/rad-hv")) {
//                newLinks = radicalCrawler.crawl(currentUrl);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    Logger.getLogger(PageManager.class.getName()).log(Level.SEVERE, null, ex);
                }
                pageToVisit.addAll(newLinks);
            } else {
//                newLinks = new ArrayList<>();
            }
            System.out.println("URL : " + currentUrl);

            pageVisited.add(currentUrl);

        }// end while this.pageVisited.size < maxSize
        int j = 1;
        for (String link : pageToVisit) {
            try {
                if (link.contains("/whv")) {
                    pageCrawler.crawlAndSaveToDB(link, "");
                    j++;
                    Thread.sleep(100);
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(PageManager.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("J in crawl page: " + j);

            if (j >= 40) {
                break;
            }
        }
    }

    public String nextUrl() {
        String nextUrl;
        do {
            nextUrl = pageToVisit.remove(0);
        } while (this.pageVisited.contains(nextUrl));
        this.pageVisited.add(nextUrl);
        return nextUrl;
    }

    /**
     * @return the MAX_PAGES_TO_SEARCH
     */
    public static int getMAX_PAGES_TO_SEARCH() {
        return MAX_PAGES_TO_SEARCH;
    }

    /**
     * @param aMAX_PAGES_TO_SEARCH the MAX_PAGES_TO_SEARCH to set
     */
    public static void setMAX_PAGES_TO_SEARCH(int aMAX_PAGES_TO_SEARCH) {
        MAX_PAGES_TO_SEARCH = aMAX_PAGES_TO_SEARCH;
    }

    /**
     * @return the pageVisited
     */
    public Set<String> getPageVisited() {
        return pageVisited;
    }

    /**
     * @param pageVisited the pageVisited to set
     */
    public void setPageVisited(Set<String> pageVisited) {
        this.pageVisited = pageVisited;
    }

    /**
     * @return the pageToVisit
     */
    public List<String> getPageToVisit() {
        return pageToVisit;
    }

    /**
     * @param pageToVisit the pageToVisit to set
     */
    public void setPageToVisit(List<String> pageToVisit) {
        this.pageToVisit = pageToVisit;
    }

    /**
     * @return the noOfPageToSearch
     */
    public int getNoOfPageToSearch() {
        return noOfPageToSearch;
    }

    /**
     * @param noOfPageToSearch the noOfPageToSearch to set
     */
    public void setNoOfPageToSearch(int noOfPageToSearch) {
        this.noOfPageToSearch = noOfPageToSearch;
    }

}
