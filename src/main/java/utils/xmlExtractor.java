/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author U124317
 */
public class xmlExtractor {
    
    public static void main(String[] args) throws IOException {
        
    
    File filePosSpa = new File("./resources/lists/working/pos_spanish.lst");
    File fileNegSpa = new File("./resources/lists/working/neg_spanish.lst");
    File filePosEn = new File("./resources/lists/working/pos_english.lst");
    File fileNegEn = new File("./resources/lists/working/neg_english.lst");
    FileOutputStream fOutPosSpa = new FileOutputStream(filePosSpa, true);
    FileOutputStream fOutNegSpa = new FileOutputStream(fileNegSpa, true);
    FileOutputStream fOutPosEn = new FileOutputStream(filePosEn, true);
    FileOutputStream fOutNegEn = new FileOutputStream(fileNegEn, true);
    OutputStreamWriter oswPosSpa = new OutputStreamWriter(fOutPosSpa);
    OutputStreamWriter oswNegSpa = new OutputStreamWriter(fOutNegSpa);
    OutputStreamWriter oswPosEn = new OutputStreamWriter(fOutPosEn);
    OutputStreamWriter oswNegEn = new OutputStreamWriter(fOutNegEn);

    
    DocumentBuilderFactory factorySpa = DocumentBuilderFactory.newInstance();
    try{
        DocumentBuilder builderSpa = factorySpa.newDocumentBuilder();
        Document documentSpa = builderSpa.parse(new File("./resources/dictionaries/senticon.es.xml"));
        
        NodeList nlSpa = documentSpa.getElementsByTagName("lemma");
        for(int i=0; i<nlSpa.getLength(); i++) {
            Element elem = (Element)nlSpa.item(i);
            float polarity = Float.parseFloat(elem.getAttribute("pol"));
            String newstring = elem.getTextContent().trim() ;
            if (polarity >= 0.5){
                oswPosSpa.write(newstring+"\n");
            }
            else if(polarity <= -0.5){
                oswNegSpa.write(newstring+"\n");             
            };
        }   
        
        
    }catch (Exception e) {
            e.printStackTrace();
        }
    
    oswPosSpa.flush();
    oswPosSpa.close();
    oswNegSpa.flush();
    oswNegSpa.close();
    
    DocumentBuilderFactory factoryEn = DocumentBuilderFactory.newInstance();
    try{
        DocumentBuilder builderEn = factoryEn.newDocumentBuilder();
        Document documentEn = builderEn.parse(new File("./resources/dictionaries/senticon.en.xml"));
        
        NodeList nlEn = documentEn.getElementsByTagName("lemma");
        for(int i=0; i<nlEn.getLength(); i++) {
            Element elem = (Element)nlEn.item(i);
            float polarity = Float.parseFloat(elem.getAttribute("pol"));
            String newstring = elem.getTextContent().trim() ;
            
            if (polarity >= 0.5){
                oswPosEn.write(newstring+"\n");
            }
            else if(polarity <= -0.5){
                oswNegEn.write(newstring+"\n");             
            };
        }
          
    }catch (Exception e) {
            e.printStackTrace();
        }
     
    }
}
