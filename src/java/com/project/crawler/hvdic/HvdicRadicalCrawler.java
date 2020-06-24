/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.crawler.hvdic;

import com.project.constants.HvdicURLConstants;
import com.project.constants.URLConstant;
import com.project.crawler.MainCrawler;
import com.project.utils.MyStringUtils;
import com.project.utils.XMLUtils;
import com.project.utils.wellformer.HTMLWellFormerUtils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 *
 * @author Poca
 */
public class HvdicRadicalCrawler {

    public List<String> crawl(String url, String baseFilePath)
            throws IOException, TransformerException, XMLStreamException {
        if (!MyStringUtils.checkStringAvailable(url)) {
            return null;
        }
        if (!MyStringUtils.checkStringAvailable(baseFilePath)) {
            return null;
        }
        MainCrawler mainCrawler = new MainCrawler();
        String webContent = mainCrawler.getWebsiteContent(url);

        if (MyStringUtils.checkStringAvailable(webContent)) {
            webContent = HTMLWellFormerUtils.refineHTML(webContent);
            StreamSource crawledHTMLStreamSource = new StreamSource(XMLUtils.parseStringToStream(webContent));
            ByteArrayOutputStream parsedXmlOutputStream = new ByteArrayOutputStream();
            StreamResult parsedXmlResult = new StreamResult(parsedXmlOutputStream);
            StreamSource xsltFile = new StreamSource(baseFilePath + URLConstant.HVDIC_RADICAL_FILTER_FILEPATH);
            Transformer transformer = XMLUtils.returnTransformerFromTemplate(xsltFile);
            transformer.transform(crawledHTMLStreamSource, parsedXmlResult);

            InputStream is = new ByteArrayInputStream(parsedXmlOutputStream.toByteArray());
            XMLEventReader reader = XMLUtils.getEventReaderFromStream(is);

            List<String> results = new ArrayList<>();
            while (reader.hasNext()) {
                XMLEvent event = reader.nextEvent();
                String link = "";
                if (event.isStartElement()) {
                    StartElement element = (StartElement) event;
                    if ("link".equals(element.getName().getLocalPart())) {
                        XMLEvent textEvent = reader.nextEvent();
                        while (textEvent.isCharacters()) {
                            Characters chars = (Characters) textEvent;
                            if (!chars.isWhiteSpace()) {
                                link += chars.getData().trim();
                            }
                            textEvent = reader.nextEvent();
                        }
                        String encodedUrl = "";
                        if (link.contains("/rad-hv")) {
                            encodedUrl = MyStringUtils.buildHvdicURLWithURLEscapedWord(link);
                        } else if (link.contains("/whv")) {
                            encodedUrl = MyStringUtils.buildHvdicURLWithHTMLEscapedWord(link);
                        }
                        System.out.println("Encoded url:" + encodedUrl);
                        results.add(HvdicURLConstants.HOST_URL + encodedUrl);
                    }

                }
                if (event.isEndElement()) {
                    EndElement element = (EndElement) event;
//                    System.out.println("End element: " + element.getName());
                }
//                if (event.isCharacters()) {
//                    Characters chars = (Characters) event;
//                    if (!chars.isWhiteSpace()) {
//                        System.out.println("Text: " + chars.getData());
//                    }
//                }
            }// end while reader.hasNext
            return results;
        }//end if MyStringUtils.checkStringAvailable(webContent)
        return null;
    }

//    public List<String> crawl(String url)
//            throws IOException, ParserConfigurationException, SAXException, XPathExpressionException {
////        String url = HvdicURLConstants.RADICAL_PAGE_URL;
////        String url = "https://hvdic.thivien.net/rad-hv/%E4%B8%80";
//        if (url == null) {
//            return null;
//        }
//        if (url.trim().isEmpty()) {
//            return null;
//        }
//        MainCrawler mainCrawler = new MainCrawler();
//        String webContent = mainCrawler.getWebsiteContent(url);
//        if (webContent == null) {
//            return null;
//        }
//        webContent = HTMLWellFormerUtils.refineHTML(webContent);
//
//        Document doc = XMLUtils.parseStringToDocument(webContent);
//        if (doc == null) {
//            return null;
//        }
//        String xPathString = "/body/section/table/tr/td[@class='main-content']";
//        Node detailNode = (Node) XMLUtils.getData(doc, xPathString, XPathConstants.NODE);
//        List<String> links = null;
//        int i = 1;
//        boolean isWordGroupNodeNull = true;
//        do { //number of radicals
//            //TODO: make number of radicals dynamic
//            xPathString = ".//div[@class='hv-word-group']"
//                    + "[" + i + "]";
//            Node wordGroupNode = (Node) XMLUtils.getData(detailNode, xPathString, XPathConstants.NODE);
//            if (wordGroupNode == null) {
//                isWordGroupNodeNull = true;
//            } else {
//                isWordGroupNodeNull = false;
//                if (links == null) {
//                    links = new ArrayList<>();
//                }
//                xPathString = ".//h3";
//                String h3Value = (String) XMLUtils.getData(wordGroupNode, xPathString, XPathConstants.STRING);
//                System.out.println("h3Value: " + h3Value);
//
//                int j = 1;
//                boolean isHrefValueNull = true;
//                String hrefValue = "";
//                do {
//                    xPathString = ".//a[@class and @href][" + j + "]/@href";
//                    //add hv-word-item hv-word-item-variant
//                    hrefValue = (String) XMLUtils.getData(wordGroupNode, xPathString, XPathConstants.STRING);
//                    if (hrefValue == null || hrefValue.trim().isEmpty()) {
//                        isHrefValueNull = true;
//                    } else {
//                        isHrefValueNull = false;
//                        System.out.println("hrefValue: " + hrefValue);
//                        String encodedUrl = "";
//                        if (hrefValue.contains("/rad-hv")) {
//                            encodedUrl = MyStringUtils.buildHvdicURLWithURLEscapedWord(hrefValue);
//                        } else if (hrefValue.contains("/whv")) {
//                            encodedUrl = MyStringUtils.buildHvdicURLWithHTMLEscapedWord(hrefValue);
//                        }
////                        System.out.println("hrefValue with unicodeEnd: " + MyStringUtils.buildHvdicURLWithURLUnicode(hrefValue));
////                        System.out.println("hrefValue -----escapedEnd: " + MyStringUtils.buildHvdicURLWithURLEscapedWord(hrefValue));
////                        System.out.println("hrefValue --HTML4 escaped:" + MyStringUtils.buildHvdicURLWithHTMLEscapedWord(hrefValue));
//                        System.out.println("Built URL : " + encodedUrl);
//                        if (encodedUrl != null && !encodedUrl.trim().isEmpty()) {
//                            links.add(HvdicURLConstants.HOST_URL + encodedUrl);
//                        }
//                    }
//                    j++;
//                } while (!isHrefValueNull);
//
////                String encodedUrl = "";
////                if (hrefValue.contains("/rad-hv")) {
////                    encodedUrl = MyStringUtils.buildHvdicURLWithURLEscapedWord(hrefValue);
////                } else if (hrefValue.contains("/whv")) {
////                    encodedUrl = MyStringUtils.buildHvdicURLWithHTMLEscapedWord(hrefValue);
////                }
////                System.out.println("hrefValue with unicodeEnd: " + MyStringUtils.buildHvdicURLWithURLUnicode(hrefValue));
////                System.out.println("hrefValue -----escapedEnd: " + MyStringUtils.buildHvdicURLWithURLEscapedWord(hrefValue));
////                System.out.println("hrefValue --HTML4 escaped:" + MyStringUtils.buildHvdicURLWithHTMLEscapedWord(hrefValue));
////                links.add(HvdicURLConstants.HOST_URL + encodedUrl);
//                xPathString = ".//span[@class='word han']";
//                String hanWord = (String) XMLUtils.getData(wordGroupNode, xPathString, XPathConstants.STRING);
//                System.out.println("hanWord: " + hanWord);
//
//                xPathString = ".//span[@class='pron']";
//                String pronounciation = (String) XMLUtils.getData(wordGroupNode, xPathString, XPathConstants.STRING);
//                System.out.println("pronounciation: " + pronounciation);
//                System.out.println("--------------");
//                wordGroupNode = null;
//            }//end if wordGroupNode != null
//            i++;
//        }//end for i < wordGroupNodeList.getLength()
//        while (!isWordGroupNodeNull);
//        return links;
//    }
}
