/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.crawler.hvdic;

import com.project.constants.URLConstant;
import com.project.crawler.MainCrawler;
import com.project.crawler.CrawlerSaveToDB;
import com.project.jaxb.Words;
import com.project.jaxb.Words.Word;
import com.project.utils.MyStringUtils;
import com.project.utils.XMLUtils;
import com.project.utils.wellformer.HTMLWellFormerUtils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import javax.naming.NamingException;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPathExpressionException;
import org.xml.sax.SAXException;

/**
 *
 * @author Poca
 */
public class HvdicPageCrawler {

    public Word crawlToWord(String wordToGet, String baseFilePath)
            throws IOException, TransformerException, JAXBException {
        if (!MyStringUtils.checkStringAvailable(wordToGet)) {
            return null;
        }
        MainCrawler mainCrawler = new MainCrawler();
//        String pageUrl = HvdicURLConstants.PAGE_BASE_URL + wordToGet;
        String pageUrl = wordToGet;
        System.out.println("pageUrl: " + pageUrl);
        String webContent = mainCrawler.getWebsiteContent(pageUrl);
        if (!webContent.isEmpty()) {
            webContent = HTMLWellFormerUtils.refineHTML(webContent);
            StreamSource crawledHtml = new StreamSource(XMLUtils.parseStringToStream(webContent));
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            StreamResult parsedXml = new StreamResult(outputStream);
            StreamSource xsltFile = new StreamSource(baseFilePath + URLConstant.HVDIC_FILTER_FILEPATH);

            Transformer transformer = XMLUtils.returnTransformerFromTemplate(xsltFile);
            transformer.transform(crawledHtml, parsedXml);
            InputStream is = new ByteArrayInputStream(outputStream.toByteArray());
            InputStream isToValidate = is;

            Words words = (Words) XMLUtils.writeXMLToJaxbObj(is);
            if (words == null) {
                return null;
            }
            Word word = words.getWord().get(0);
            if (word != null) {
                return word;
            }// end if word != null
        }
        return null;
    }

    public void crawlAndSaveToDB(String wordToGet, String baseFilePath)
            throws TransformerConfigurationException, IOException, ParserConfigurationException, SAXException, TransformerException, JAXBException, XPathExpressionException, SQLException, NamingException {
        if (!MyStringUtils.checkStringAvailable(wordToGet)) {
            return;
        }
        MainCrawler mainCrawler = new MainCrawler();
//        String pageUrl = HvdicURLConstants.PAGE_BASE_URL + wordToGet;
        String pageUrl = wordToGet;
        System.out.println("pageUrl: " + pageUrl);
        String webContent = mainCrawler.getWebsiteContent(pageUrl);
        if (!webContent.isEmpty()) {
            webContent = HTMLWellFormerUtils.refineHTML(webContent);
//            webContent = webContent.replaceAll("<br/>", "&lt;br/&gt;");

            StreamSource crawledHtml = new StreamSource(XMLUtils.parseStringToStream(webContent));
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            StreamResult parsedXml = new StreamResult(outputStream);
//            String baseFilepath = filePath.substring(0, filePath.lastIndexOf("\\"));
            StreamSource xsltFile = new StreamSource(baseFilePath + URLConstant.HVDIC_FILTER_FILEPATH);

            Transformer transformer = XMLUtils.returnTransformerFromTemplate(xsltFile);
            transformer.transform(crawledHtml, parsedXml);
            InputStream is = new ByteArrayInputStream(outputStream.toByteArray());
            InputStream isToValidate = is;
//            XMLUtils.validateXML(new InputSource(isToValidate), baseFilePath + URLConstant.WORD_SCHEMA_FILEPATH);
//            System.out.println("Output: " + new String(outputStream.toByteArray(), "UTF-8"));
            Words words = (Words) XMLUtils.writeXMLToJaxbObj(is);
            if (words == null) {
                return;
            }
            Word word = words.getWord().get(0);
            if (word != null) {
                CrawlerSaveToDB.writeWordToDB(word);
            }// end if word != null
//            }// end if outputStream != null
        }
    }

    /*
    public void crawlAndSaveToDB(String wordToGet)
            throws ParserConfigurationException, IOException, SAXException, XPathExpressionException, SQLException, NamingException {
        if (wordToGet == null) {
            return;
        }
        if (wordToGet.trim().isEmpty()) {
            return;
        }
        MainCrawler mainCrawler = new MainCrawler();
//        String pageUrl = HvdicURLConstants.PAGE_BASE_URL + wordToGet;
        String pageUrl = wordToGet;
        System.out.println("pageUrl: " + pageUrl);
        String webContent = mainCrawler.getWebsiteContent(pageUrl);
        if (!webContent.isEmpty()) {
            webContent = HTMLWellFormerUtils.refineHTML(webContent);

            WordDTO wordDTO = new WordDTO();

            Document doc = XMLUtils.parseStringToDocument(webContent);
            if (doc != null) {
                //get word data (unicode, variants...)
                String xPathString
                        = "/body/section/table/tr/td/div[@class='hvres han-word']";
                Node wordData = (Node) XMLUtils.getData(doc, xPathString, XPathConstants.NODE);
                if (wordData != null) {
                    xPathString = ".//div/div[@class='hvres-meaning'][1]/a[@target='_blank']";
                    String unicodeString = (String) XMLUtils.getData(wordData, xPathString, XPathConstants.STRING);
                    xPathString = ".//div/div/a[contains(@href,'/whv/')]";
                    String word = (String) XMLUtils.getData(wordData, xPathString, XPathConstants.STRING);
                    wordDTO.setUnicodeString(unicodeString);
                    wordDTO.setWord(word);
                    System.out.println("Unicode String: " + unicodeString);
                    System.out.println("Word: " + word);
                }
                xPathString = "/body/section/table/tr"
                        + "/td[@class='main-content']";
//                            + "/div[@class and @name and @data-hvres-idx]";
                Node detailData = (Node) XMLUtils.getData(doc, xPathString, XPathConstants.NODE);
                boolean wordDetailNull = true;
                int i = 1;
                if (detailData != null) {
                    List<String> vnPronounciations = null;
                    do {
                        xPathString = ".//div[@class and @name and @data-hvres-idx='"
                                + i
                                + "']";
                        Node wordDetail = (Node) XMLUtils.getData(detailData, xPathString, XPathConstants.NODE);
                        if (wordDetail == null) {
                            wordDetailNull = true;
                        } else {
                            wordDetailNull = false;
                            List<Integer> wordTypes = new ArrayList<>();
                            xPathString = ".//a[contains(@href,'/hv/')]";
                            String vietnamesePronounciation = (String) XMLUtils.getData(wordDetail, xPathString, XPathConstants.STRING);
                            System.out.println("Pronounciation " + i + ": " + vietnamesePronounciation);
                            if (vnPronounciations == null) {
                                vnPronounciations = new ArrayList<>();
                            }
                            if (MyStringUtils.checkStringAvailable(vietnamesePronounciation)) {
                                vnPronounciations.add(vietnamesePronounciation);
                            }
                            xPathString = ".//p[@class='hvres-info gray small']";
                            String wordTypeString = (String) XMLUtils.getData(wordDetail, xPathString, XPathConstants.STRING);
                            System.out.println("Word type string: " + wordTypeString);
                            if (wordTypeString.toLowerCase().contains("phồn")) {
                                System.out.println("Phồn thể");
                                wordTypes.add(1);
                            }
                            if (wordTypeString.toLowerCase().contains("giản")) {
                                System.out.println("Giản thể");
                                wordTypes.add(2);
                            }

                            xPathString = ".//div[@class='hvres-meaning han-clickable'][1]"; //get first detail only
                            String wordDetailString = (String) XMLUtils.getData(wordDetail, xPathString, XPathConstants.STRING);
                            wordDTO.setWordDesc(wordDetailString);
                            writeNewWordToDB(wordDTO);
                            //unicode string already set to dto above
                            writeNewWord2WordTypeToDB(wordDTO.getUnicodeString(), wordTypes);
                            i++;
                        }// end if wordDetail != null
                        writeNewPronounciationToDB(wordDTO.getUnicodeString(), vnPronounciations);
                    } while (!wordDetailNull);
                }// end if detailData != null
            }// end if doc != null
        }
    }
     */
}
