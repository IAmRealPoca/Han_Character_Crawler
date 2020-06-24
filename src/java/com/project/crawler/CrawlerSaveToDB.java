/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.crawler;

import com.project.constants.PronounciationTypeEnum;
import com.project.dao.JapanesePronounciationDAO;
import com.project.dao.VietnamesePronounciationDAO;
import com.project.dao.WordDAO;
import com.project.dao.WordToWordTypeDAO;
import com.project.dto.WordDTO;
import com.project.jaxb.Pronounciation;
import com.project.jaxb.Words.Word;
import com.project.jaxb.WordDetail;
import com.project.utils.MyStringUtils;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.naming.NamingException;

/**
 *
 * @author Poca
 */
public class CrawlerSaveToDB {

    public static void writeWordToDB(Word word)
            throws SQLException, NamingException {
        WordDTO wordDTO = new WordDTO();
        wordDTO.setUnicodeString(word.getUnicodeString());
        wordDTO.setWord(word.getWordString());
        wordDTO.setNoOfStroke(word.getNoOfStroke());
        Set<Integer> wordTypes = new HashSet<>();
        CrawlerSaveToDB.writeNewWordToDB(wordDTO);
        if (word.getWordDetails() != null) {
            for (WordDetail wordDetail : word.getWordDetails().getWordDetail()) {
                if (wordDetail.getWordType().toLowerCase().contains("phồn")) {
                    wordTypes.add(1);
                }
                if (wordDetail.getWordType().toLowerCase().contains("giản")) {
                    wordTypes.add(2);
                }
                String wordInfoString = "";
                for (String wordInfo : wordDetail.getWordInfos().getWordInfo()) {
//                System.out.println("wordInfoString: " + wordInfoString);
//                System.out.println("wordInfo: " + wordInfo);
                    wordInfoString += wordInfo;

                }
                List<Pronounciation.Reading> readings = wordDetail.getPronounciation().getReading();
                writeNewPronounciationToDB(word.getUnicodeString(), readings, wordInfoString);

            }//end WordDetail wordDetail : word.getWordDetails().getWordDetail()
        }

        writeNewWord2WordTypeToDB(word.getUnicodeString(), wordTypes);
    }

    private static boolean writeNewWordToDB(WordDTO wordDTO)
            throws SQLException, NamingException {
        if (wordDTO == null) {
            return false;
        }
        WordDAO wordDAO = new WordDAO();
        WordDTO wordExists = wordDAO.getWord(wordDTO.getUnicodeString());
        if (wordExists == null) {
            wordDAO.insertNewWord(wordDTO);
            return true;
        }
        return false;
    }

    private static boolean writeNewWord2WordTypeToDB(String unicodeString, Set<Integer> wordTypes)
            throws SQLException, NamingException {
        if (!MyStringUtils.checkStringAvailable(unicodeString)) {
            return false;
        }
        if (wordTypes == null || wordTypes.isEmpty()) {
            return false;
        }
        WordDAO wordDAO = new WordDAO();
        if (wordDAO.getWord(unicodeString) == null) {
            return false;
        }
        WordToWordTypeDAO wordToWordTypeDAO = new WordToWordTypeDAO();
        //one word may have many pronounciation. If the word is existed, only add
        //more pronounciation, ignore existed ones
        for (Integer wordType : wordTypes) {
            boolean word2WordTypeIsExisted = wordToWordTypeDAO.checkIsExisted(unicodeString, wordType);
            if (!word2WordTypeIsExisted) {
                wordToWordTypeDAO.insertNewWord(unicodeString, wordType);
            }
        }
        return true;
    }

    private static boolean writeNewPronounciationToDB(String unicodeString,
            List<Pronounciation.Reading> pronounciations, String wordDesc)
            throws SQLException, NamingException {
        if (!MyStringUtils.checkStringAvailable(unicodeString)) {
            return false;
        }
        WordDAO wordDAO = new WordDAO();
        if (wordDAO.getWord(unicodeString) == null) {
            return false;
        }
        for (Pronounciation.Reading reading : pronounciations) {
            PronounciationTypeEnum pronounciationType
                    = PronounciationTypeEnum.getProTypeFromStringName(reading.getType());
            if (pronounciationType == null) {
                return false;
            }
            String pronounValue = reading.getValue();
            switch (pronounciationType) {
                case VIE_HANVIET:
                    VietnamesePronounciationDAO vnProdao = new VietnamesePronounciationDAO();
                    if (!vnProdao.checkIsExisted(unicodeString, pronounValue)) {
                        vnProdao.insertNewPronounciation(unicodeString, pronounValue, wordDesc);
                    }
                    break;
                case JAP_KUNYOMI:
                    JapanesePronounciationDAO japProdao = new JapanesePronounciationDAO();
                    if (!japProdao.checkIsKunyomiExisted(unicodeString, pronounValue)) {
                        japProdao.insertNewKunyomiPronounciation(unicodeString, pronounValue, wordDesc);
                    }
                    break;
                case JAP_ONYOMI:
                    JapanesePronounciationDAO japProdao2 = new JapanesePronounciationDAO();
                    if (!japProdao2.checkIsOnyomiExisted(unicodeString, pronounValue)) {
                        japProdao2.insertNewOnyomiPronounciation(unicodeString, pronounValue, wordDesc);
                    }
                    break;
                case KOR_KOREAN:
//            default: return false;
                }//end switch (pronounciationType)

        }

        return true;
    }
}
