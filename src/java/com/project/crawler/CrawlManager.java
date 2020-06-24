/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.crawler;

import com.project.crawler.hvdic.HvdicPageCrawler;
import com.project.crawler.hvdic.HvdicRadicalCrawler;
import com.project.crawler.jisho.JishoCrawler;
import com.project.dao.WordDAO;
import com.project.dto.WordDTO;
import com.project.jaxb.Words.Word;
import com.project.utils.MyStringUtils;
import com.project.utils.XMLUtils;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.naming.NamingException;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author Poca
 */
public class CrawlManager {

    private Set<String> pageVisited;
    private List<String> pageToVisit;
    private int maxPageToCrawl;

    public CrawlManager() {
        this.pageVisited = new HashSet<>();
        this.pageToVisit = new ArrayList<>();
        this.maxPageToCrawl = 500;
    }

    public CrawlManager(Set<String> pageVisited, List<String> pageToVisit, int maxPageToCrawl) {
        this.pageVisited = pageVisited;
        this.pageToVisit = pageToVisit;
        this.maxPageToCrawl = maxPageToCrawl;
    }

    public void crawlAll(String baseRadicalLink, String baseFilePath)
            throws IOException, TransformerException, XMLStreamException, SQLException, NamingException, TransformerConfigurationException, JAXBException {
        HvdicPageCrawler hvdicPageCrawler = new HvdicPageCrawler();
        HvdicRadicalCrawler hvdicRadicalCrawler = new HvdicRadicalCrawler();
        WordDAO wordDAO = new WordDAO();
        JishoCrawler jishoCrawler = new JishoCrawler();

        //link of 214 base radicals
        List<String> baseRadicalLinks = hvdicRadicalCrawler.crawl(baseRadicalLink, baseFilePath);
        List<String> wordLinks = new ArrayList<>();
        for (String link : baseRadicalLinks) {
            wordLinks.addAll(hvdicRadicalCrawler.crawl(link, baseFilePath));
        }
        int i = 0;
        for (String link : wordLinks) {
            if (!link.contains("/rad-hv")) {
//                getPageVisited().add(link);
                String lastWord = link.substring(link.lastIndexOf("/") + 1);
                String unicode = "U+" + MyStringUtils.getWordUnicode(MyStringUtils.urlDecoder(lastWord, "UTF-8").charAt(0));
                System.out.println("Unicode in manager: " + unicode);
                WordDTO wordDTO = wordDAO.getWord(unicode);
                if (wordDTO == null) {
                    Word hvdicWord = hvdicPageCrawler.crawlToWord(link, baseFilePath);
                    Word jishoWord = jishoCrawler.crawlToWord(link.substring(link.lastIndexOf("/") + 1), baseFilePath);
                    hvdicWord.setWordDetails(XMLUtils.eliminateDuplicateWordInfo(hvdicWord.getWordDetails()));
                    Word unitedWord = XMLUtils.combineWords(hvdicWord, jishoWord);
                    CrawlerSaveToDB.writeWordToDB(unitedWord);
                    i++;
                }
            }
            if (i >= getMaxPageToCrawl()) {
                break;
            }
        }
    }

    public void crawlCareful(String baseRadicalLink, String baseFilePath)
            throws IOException, TransformerException, XMLStreamException, JAXBException, SQLException, NamingException {
        HvdicPageCrawler hvdicPageCrawler = new HvdicPageCrawler();
        HvdicRadicalCrawler hvdicRadicalCrawler = new HvdicRadicalCrawler();
        WordDAO wordDAO = new WordDAO();
        JishoCrawler jishoCrawler = new JishoCrawler();
        //build list of links to vist using radical crawler
        List<String> linkToVisit = hvdicRadicalCrawler.crawl(baseRadicalLink, baseFilePath);

        this.getPageToVisit().addAll(linkToVisit);
        int i = 0;
        Iterator<String> iterator = pageToVisit.iterator();
        List<String> wordInRadical = new ArrayList();
        while (iterator.hasNext()) {
            String link = iterator.next();
            if (link.contains("/rad-hv")) {
                wordInRadical.addAll(hvdicRadicalCrawler.crawl(link, baseFilePath));
                System.out.println("Link: " + link);
                pageToVisit.addAll(wordInRadical);
            }
        }
        Iterator<String> iterator2 = pageToVisit.iterator();
        while (iterator2.hasNext()) {
            String link = iterator2.next();
            iterator2.remove();
            //            try {
            //                Thread.sleep(1000);
            //            } catch (InterruptedException ex) {
            //                Logger.getLogger(CrawlManager.class.getName()).log(Level.SEVERE, null, ex);
            //            }
            if (!link.contains("/rad-hv")) {
                getPageVisited().add(link);
                String lastWord = link.substring(link.lastIndexOf("/") + 1);
                String unicode = "U+" + MyStringUtils.getWordUnicode(MyStringUtils.urlDecoder(lastWord, "UTF-8").charAt(0));
                System.out.println("Unicode in manager: " + unicode);
                WordDTO wordDTO = wordDAO.getWord(unicode);
                if (wordDTO == null) {
                    Word hvdicWord = hvdicPageCrawler.crawlToWord(link, baseFilePath);
                    Word jishoWord = jishoCrawler.crawlToWord(link.substring(link.lastIndexOf("/") + 1), baseFilePath);
                    hvdicWord.setWordDetails(XMLUtils.eliminateDuplicateWordInfo(hvdicWord.getWordDetails()));
                    Word unitedWord = XMLUtils.combineWords(hvdicWord, jishoWord);
                    CrawlerSaveToDB.writeWordToDB(unitedWord);
                    i++;
                }
            }
//            iterator.remove();
            if (i >= getMaxPageToCrawl()) {
                break;
            }
        }

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
     * @return the maxPageToCrawl
     */
    public int getMaxPageToCrawl() {
        return maxPageToCrawl;
    }

    /**
     * @param maxPageToCrawl the maxPageToCrawl to set
     */
    public void setMaxPageToCrawl(int maxPageToCrawl) {
        this.maxPageToCrawl = maxPageToCrawl;
    }

}
