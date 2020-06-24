/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.utils;

import com.project.constants.Constants;
import com.project.jaxb.WordDetail;
import com.project.jaxb.WordInfos;
import com.project.jaxb.Words;
import com.project.jaxb.Words.Word;
import com.project.jaxb.Words.Word.WordDetails;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author Poca
 */
public class XMLUtils {

    public static Word combineWords(Word hvdicWord, Word jishoWord) {
        if (hvdicWord == null) {
            return null;
        }
        if (jishoWord == null) {
            return null;
        }
        if (hvdicWord.getUnicodeString().equalsIgnoreCase("U+" + jishoWord.getUnicodeString())) {
            //compare noOfStrokes
            if (hvdicWord.getNoOfStroke() == 0 && jishoWord.getNoOfStroke() != 0) {
                hvdicWord.setNoOfStroke(jishoWord.getNoOfStroke());
            }
            //compare variants
            List<String> hvdicVariants = new ArrayList<>();
            for (String variant : hvdicWord.getVariants().getVariant()) {
                hvdicVariants.add(MyStringUtils.unescapeHTML4(variant));
            }
            List<String> jishoVariants = jishoWord.getVariants().getVariant();
            Set<String> unitedVariants = new HashSet<>(hvdicVariants);
            unitedVariants.addAll(jishoVariants);
            //compare word details
            WordDetails hvdicDetails = hvdicWord.getWordDetails();
            if (hvdicDetails != null) {
                List<WordDetail> hvdicDetailList = hvdicDetails.getWordDetail();
                WordDetails jishoDetails = jishoWord.getWordDetails();
                hvdicDetailList.addAll(jishoDetails.getWordDetail());
            }
        }
        return hvdicWord;
    }

    public static WordDetails eliminateDuplicateWordInfo(WordDetails wordDetails) {
        if (wordDetails == null) {
            return null;
        }
        if (wordDetails.getWordDetail() == null || wordDetails.getWordDetail().isEmpty()) {
            return null;
        }
        if (wordDetails.getWordDetail().get(0)
                .getPronounciation().getReading() == null
                || wordDetails.getWordDetail().get(0)
                        .getPronounciation().getReading().isEmpty()) {
            return null;
        }
        List<WordDetail> listWordDetail = wordDetails.getWordDetail();

        int currentMaxCount = listWordDetail.get(0).getWordInfos().getWordInfo().size();
        int currentMaxIndex = 0;

        //choosing the wordDetais that has the most number of wordInfo
        for (int j = 1; j < listWordDetail.size(); j++) {
            List<String> wordInfos = listWordDetail.get(j).getWordInfos().getWordInfo();
            if (wordInfos.size() > currentMaxCount) {
                currentMaxIndex = j;
            }
        }
//        System.out.println("Chosen base: " + currentMaxIndex);
        //base wordInfo to compare
        List<String> baseWordInfo = listWordDetail.get(currentMaxIndex).getWordInfos().getWordInfo();
        for (String baseInfo : baseWordInfo) {
//            System.out.println("Current base: " + baseInfo);
            //loop through every wordInfo in every wordDetails
            for (int j = 0; j < listWordDetail.size(); j++) {
                WordDetail wordDetail = listWordDetail.get(j);
                WordInfos currentWordInfo = wordDetail.getWordInfos();
//                System.out.println("---J: " + j);
                if (j != currentMaxIndex) {
                    List<String> wordInfos = currentWordInfo.getWordInfo();
                    Iterator<String> it = wordInfos.iterator();
                    while (it.hasNext()) {
                        String compareInfo = it.next();
                        int matchPoint = MyStringUtils.computeMatchingDensity(baseInfo, compareInfo);
                        if (matchPoint * 100 >= 99 * Math.min(baseInfo.length(), compareInfo.length())) {
                            it.remove();
                        }
                    }

                    currentWordInfo.setWordInfo(wordInfos);
                }
                listWordDetail.get(j).setWordInfos(currentWordInfo);
            }

        }
        wordDetails.setWordDetail(listWordDetail);
        return wordDetails;
    }

    public static void validateXML(InputSource xml, String schemaFilePath) {
        try {
//            JAXBContext context = JAXBContext.newInstance(Constants.JAXB_PACKAGE);
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new File(schemaFilePath));
            Validator validator = schema.newValidator();

            validator.validate(new SAXSource(xml));

        } catch (SAXException ex) {
            Logger.getLogger(XMLUtils.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {
            Logger.getLogger(XMLUtils.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static XMLEventReader getEventReaderFromStream(InputStream is)
            throws XMLStreamException {
        XMLInputFactory xif = XMLInputFactory.newFactory();
        XMLEventReader reader = xif.createXMLEventReader(is);
        return reader;
    }

    public static void writeJaxbObjToFile(Object object, String filePath)
            throws JAXBException, FileNotFoundException {
        if (!MyStringUtils.checkStringAvailable(filePath)) {
            return;
        }
        JAXBContext context = JAXBContext.newInstance(Constants.JAXB_PACKAGE);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_ENCODING, Constants.UTF8_ENCODE);
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);
        marshaller.marshal(object, new File(filePath));
    }

    public static Object writeXMLToJaxbObj(InputStream is)
            throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Constants.JAXB_PACKAGE);
        Unmarshaller u = context.createUnmarshaller();
        Words word = (Words) u.unmarshal(is);
        return word;
    }

    public static Transformer returnTransformerFromTemplate(StreamSource xslt)
            throws TransformerConfigurationException {
        TransformerFactory tf = TransformerFactory.newInstance();
        Templates template = tf.newTemplates(xslt);
        Transformer transformer = template.newTransformer();
        return transformer;
    }

    public static Document parseStringToDocument(String str)
            throws ParserConfigurationException, SAXException, IOException {
        if (str == null) {
            return null;
        }
        if (str.isEmpty()) {
            return null;
        }
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        byte[] bytes = str.getBytes(Constants.UTF8_ENCODE);
        InputStream is = new ByteArrayInputStream(bytes);
        Document doc = builder.parse(is);
        return doc;
    }

    public static InputStream parseStringToStream(String str)
            throws UnsupportedEncodingException {
        if (!MyStringUtils.checkStringAvailable(str)) {
            return null;
        }
        byte[] bytes = str.getBytes(Constants.UTF8_ENCODE);
        InputStream is = new ByteArrayInputStream(bytes);
        return is;
    }

    public static XPath getXPath() {
        XPathFactory factory = XPathFactory.newInstance();
        XPath xpath = factory.newXPath();

        return xpath;
    }

    public static Object getData(Node node, String expression, QName qname)
            throws XPathExpressionException {
        if (node == null) {
            return null;
        }
        if (expression == null) {
            return null;
        }
        if (expression.trim().length() == 0) {
            return null;
        }
        XPath xpath = getXPath();
        Object result = xpath.evaluate(expression, node, qname);

        return result;
    }
}
