package com.lhstudy;


import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;



public class XpathToXml {
    public static void main(String[] args) {
        try {
            File inputFile = new File("C:\\Users\\lh\\Documents\\Tencent Files\\3035419253\\FileRecv\\33.xml");
            DocumentBuilderFactory dbFactory
                    = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder;

            dBuilder = dbFactory.newDocumentBuilder();

            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            XPath xPath =  XPathFactory.newInstance().newXPath();

            String info = "/hqdrctdstrb/mst";

            NodeList nodeList = (NodeList) xPath.compile(info).evaluate(doc, XPathConstants.NODESET);


            for (int i = 0; i < nodeList.getLength(); i++){
                Node nNode = nodeList.item(i);
                Element element=(Element)nNode;
                System.out.println(element.getElementsByTagName("brief").item(0).getTextContent());
                String temp=element.getElementsByTagName("brief").item(0).getTextContent();
                String changeStr="8888-8888";
                String target="\\/(d{4}-d{4})\\/共";
                String resultStr=temp.replace(String.format(target),changeStr);
                System.out.println(resultStr);
            }

        }catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
    }

}
