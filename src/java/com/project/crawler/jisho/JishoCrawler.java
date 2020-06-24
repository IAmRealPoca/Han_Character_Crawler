/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.crawler.jisho;

import com.project.constants.JishoURLConstants;
import com.project.constants.URLConstant;
import com.project.crawler.MainCrawler;
import com.project.jaxb.Words;
import com.project.jaxb.Words.Word;
import com.project.utils.XMLUtils;
import com.project.utils.wellformer.HTMLWellFormerUtils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.bind.JAXBException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 *
 * @author Poca
 */
public class JishoCrawler {

    public Word crawlToWord(String wordToGet, String filePath)
            throws IOException, TransformerConfigurationException, TransformerException, JAXBException {
        MainCrawler mainCrawler = new MainCrawler();
        String url = JishoURLConstants.PAGE_BASE_URL + wordToGet + JishoURLConstants.PAGE_BASE_URL_TAIL;
        System.out.println("Jisho url: " + url);
        String webContent = mainCrawler.getWebsiteContent(url);
        if (!webContent.isEmpty()) {
            webContent = HTMLWellFormerUtils.refineHTML(webContent);

            StreamSource crawledHtml = new StreamSource(XMLUtils.parseStringToStream(webContent));
            ByteArrayOutputStream parsedXmlOutputStream = new ByteArrayOutputStream();
            StreamResult parsedXml = new StreamResult(parsedXmlOutputStream);

            StreamSource xsltFile = new StreamSource(filePath + URLConstant.JISHO_FILTER_FILEPATH);
            Transformer transformer = XMLUtils.returnTransformerFromTemplate(xsltFile);
            transformer.transform(crawledHtml, parsedXml);

            InputStream is = new ByteArrayInputStream(parsedXmlOutputStream.toByteArray());
            Words words = (Words) XMLUtils.writeXMLToJaxbObj(is);
            if (words == null) {
                return null;
            }
            Word word = words.getWord().get(0);
            if (word != null) {
                System.out.println("Unicode: " + word.getUnicodeString());
                System.out.println("Word: " + word.getWordString());
                System.out.println("Variants: " + word.getVariants().getVariant().get(0));
                System.out.println("Pronounciations: " + word.getWordDetails().getWordDetail().get(0).getPronounciation().getReading().get(0).getType());
                System.out.println("Pronounciations: " + word.getWordDetails().getWordDetail().get(0).getPronounciation().getReading().get(0).getValue());
                System.out.println("NoOfStroke: " + word.getNoOfStroke());
                System.out.println("Word info: " + word.getWordDetails().getWordDetail().get(0).getWordInfos());
                return word;
            }//end word != null
        }//end if !webContent.isEmpty()
        return null;
    }

    public void crawl(String wordToGet, String filePath)
            throws IOException, TransformerConfigurationException, TransformerException, JAXBException {
        MainCrawler mainCrawler = new MainCrawler();
        String url = JishoURLConstants.PAGE_BASE_URL + wordToGet + JishoURLConstants.PAGE_BASE_URL_TAIL;
        String webContent = mainCrawler.getWebsiteContent(url);
        if (!webContent.isEmpty()) {
            webContent = HTMLWellFormerUtils.refineHTML(webContent);

            StreamSource crawledHtml = new StreamSource(XMLUtils.parseStringToStream(webContent));
            ByteArrayOutputStream parsedXmlOutputStream = new ByteArrayOutputStream();
            StreamResult parsedXml = new StreamResult(parsedXmlOutputStream);

            StreamSource xsltFile = new StreamSource(filePath);
            Transformer transformer = XMLUtils.returnTransformerFromTemplate(xsltFile);
            transformer.transform(crawledHtml, parsedXml);

            InputStream is = new ByteArrayInputStream(parsedXmlOutputStream.toByteArray());
            Words words = (Words) XMLUtils.writeXMLToJaxbObj(is);
            if (words == null) {
                return;
            }
            Word word = words.getWord().get(0);
            if (word != null) {
                System.out.println("Unicode: " + word.getUnicodeString());
                System.out.println("Word: " + word.getWordString());
                System.out.println("Variants: " + word.getVariants().getVariant().get(0));
                System.out.println("Pronounciations: " + word.getWordDetails().getWordDetail().get(0).getPronounciation().getReading().get(0).getType());
                System.out.println("Pronounciations: " + word.getWordDetails().getWordDetail().get(0).getPronounciation().getReading().get(0).getValue());
                System.out.println("NoOfStroke: " + word.getNoOfStroke());
            }//end word != null
        }//end if !webContent.isEmpty()
    }
}
