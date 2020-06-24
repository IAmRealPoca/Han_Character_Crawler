/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.servlet;

import com.project.constants.Constants;
import com.project.constants.URLConstant;
import com.project.dao.JapanesePronounciationDAO;
import com.project.dao.VietnamesePronounciationDAO;
import com.project.dao.WordDAO;
import com.project.dao.WordToWordTypeDAO;
import com.project.dao.WordTypeDAO;
import com.project.dto.JapPronounciationDTO;
import com.project.dto.VnPronounciationDTO;
import com.project.dto.WordDTO;
import com.project.dto.WordTypeDTO;
import com.project.jaxb.Pronounciation;
import com.project.jaxb.WordDetail;
import com.project.jaxb.WordInfos;
import com.project.jaxb.Words;
import com.project.jaxb.Words.Word;
import com.project.utils.MyStringUtils;
import com.project.utils.XMLUtils;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;

/**
 *
 * @author Poca
 */
public class SearchWordServlet extends HttpServlet {

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
        String unicode = request.getParameter("txtSearch");
        String wordParam = request.getParameter("txtWordSearch");
        try {
            WordDAO wordDAO = new WordDAO();

            ServletContext context = this.getServletContext();
            String realPath = context.getRealPath("/");
            String filePath = realPath + "WEB-INF/xml/output-hvdic-word.xml";
            if (wordParam != null) {
                unicode = MyStringUtils.getWordUnicode(MyStringUtils.urlDecoder(wordParam, Constants.UTF8_ENCODE));
            }
            if (unicode == null || unicode.trim().isEmpty()) {
                List<WordDTO> dtos = wordDAO.getAllWord();
                List<Words.Word> listWord = new ArrayList<>();
                for (WordDTO dto : dtos) {
                    Word word = wordDTOToWordJAXB(dto);
                    listWord.add(word);
                }
                Words words = new Words();
                words.setWord(listWord);
                XMLUtils.writeJaxbObjToFile(words, filePath);
            } else {
                unicode = unicode.toUpperCase();
                if (!unicode.contains("U+")) {
                    unicode = "U+" + unicode;
                }
                WordDTO dto = wordDAO.getWord(unicode);
                if (dto != null) {
                    Word word = wordDTOToWordJAXB(dto);
                    Words words = new Words();
                    words.setWord(Arrays.asList(word));
                    XMLUtils.writeJaxbObjToFile(words, filePath);
                }
                else {
                    Words words = new Words();
                    XMLUtils.writeJaxbObjToFile(words, filePath);
                }

            }

            response.sendRedirect(URLConstant.SEARCH_PAGE);
        } catch (SQLException ex) {
            Logger.getLogger(SearchWordServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NamingException ex) {
            Logger.getLogger(SearchWordServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JAXBException ex) {
            Logger.getLogger(SearchWordServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SearchWordServlet.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            out.close();
        }
    }

    private Word wordDTOToWordJAXB(WordDTO dto) throws SQLException, NamingException, JAXBException, FileNotFoundException {

        String unicode = dto.getUnicodeString();
        WordToWordTypeDAO w2wtDAO = new WordToWordTypeDAO();
        List<Integer> wordTypeIds = w2wtDAO.getWordToWordType(unicode);

        WordTypeDAO wordTypeDAO = new WordTypeDAO();
        List<String> wordTypes = new ArrayList<>();
        for (int wordTypeId : wordTypeIds) {
            WordTypeDTO wtDTO = wordTypeDAO.getWordType(wordTypeId);
            wordTypes.add(wtDTO.getWordTypeName());
        }
        VietnamesePronounciationDAO vnPronounciationDAO = new VietnamesePronounciationDAO();
        JapanesePronounciationDAO jaPronounciationDAO = new JapanesePronounciationDAO();
        List<JapPronounciationDTO> jaPronounciations = jaPronounciationDAO.getPronounciation(unicode);
        List<VnPronounciationDTO> vnPronounciations = vnPronounciationDAO.getPronounciation(unicode);
        if (dto != null) {
            Word word = new Word();
            word.setUnicodeString(dto.getUnicodeString());
            word.setWordString(dto.getWord());
            word.setNoOfStroke(dto.getNoOfStroke());
            List<WordDetail> wordDetailList = new ArrayList<>();
            for (VnPronounciationDTO vnPDTO : vnPronounciations) {
                WordDetail wordDetail = new WordDetail();
                Pronounciation.Reading reading = new Pronounciation.Reading();
                reading.setType("hanviet");
                reading.setValue(vnPDTO.getPronounciation());
                Pronounciation p = new Pronounciation();
                p.setReading(Arrays.asList(reading));
                wordDetail.setPronounciation(p);

                WordInfos wordInfos = new WordInfos();
                wordInfos.setWordInfo(Arrays.asList(vnPDTO.getWordDesc()
                //                            .replace("<Explain/>", "&lt;Explain/&gt;")
                //                            .replace("<br/>", "&lt;br/&gt;")
                ));
                wordDetail.setWordInfos(wordInfos);
                wordDetailList.add(wordDetail);
            }

            for (JapPronounciationDTO jaPDTO : jaPronounciations) {
                WordDetail wordDetail = new WordDetail();
                Pronounciation.Reading reading = new Pronounciation.Reading();
//                    reading.setType("hanviet");
                String kunyomi = jaPDTO.getKunyomi();
                String onyomi = jaPDTO.getOnyomi();
                if (kunyomi == null) {
                    reading.setType("onyomi");
                    reading.setValue(onyomi);
                }
                if (onyomi == null) {
                    reading.setType("kunyomi");
                    reading.setValue(kunyomi);
                }

                Pronounciation p = new Pronounciation();

                p.setReading(Arrays.asList(reading));
                wordDetail.setPronounciation(p);

//                WordInfos wordInfos = new WordInfos();
//                if (wordDetail.getWordInfos() == null) {
////                    System.out.println("Word info: " + wordInfos.getWordInfo().get(0));
//                    
//                    List<String> wordInfoList = new ArrayList<>();
//                    wordInfoList.add(jaPDTO.getWordDesc());
//                    wordInfos.setWordInfo(wordInfoList);
//                    //                            .replace("<Explain/>", "&lt;Explain/&gt;")
//                    //                            .replace("<br/>", "&lt;br/&gt;")
////                    );
//                }
//                wordDetail.setWordInfos(wordInfos);
                wordDetailList.add(wordDetail);
            }
            //                WordInfos wordInfos = new WordInfos();
//                if (wordDetail.getWordInfos() == null) {
////                    System.out.println("Word info: " + wordInfos.getWordInfo().get(0));
//                    
//                    List<String> wordInfoList = new ArrayList<>();
//                    wordInfoList.add(jaPDTO.getWordDesc());
//                    wordInfos.setWordInfo(wordInfoList);
//                    //                            .replace("<Explain/>", "&lt;Explain/&gt;")
//                    //                            .replace("<br/>", "&lt;br/&gt;")
////                    );
//                }
//                wordDetail.setWordInfos(wordInfos);
//                wordDetailList.add(wordDetail);
//                for (String wordType : wordTypes) {
//                    wordDetail.setWordType(wordType);
//                }

            Word.WordDetails wordDetails = new Word.WordDetails();
            wordDetails.setWordDetail(wordDetailList);
            word.setWordDetails(wordDetails);
//                System.out.println("Unicode: " + dto.getUnicodeString() + word.getUnicodeString());
            return word;
        }
        return null;
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
