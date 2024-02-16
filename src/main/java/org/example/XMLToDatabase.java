package org.example;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLToDatabase {
    public static void main(String[] args) {
        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            // Step 1: Parse XML file
            File xmlFile = new File("C:/Users/lathu/OneDrive/Documents/XMLtoDatabase/src/main/java/org/example/data.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            // Step 2: Connect to database
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/xmltodatabase", "root", "1234");

            // Step 3: Prepare SQL statement
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO details (column1, column2, column3) VALUES (?, ?, ?)");

            // Step 4: Iterate through XML data and save to database
            NodeList nodeList = doc.getElementsByTagName("item");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String column1 = element.getElementsByTagName("column1").item(0).getTextContent();
                    String column2 = element.getElementsByTagName("column2").item(0).getTextContent();
                    String column3 = element.getElementsByTagName("column3").item(0).getTextContent();

                    pstmt.setString(1, column1);
                    pstmt.setString(2, column2);
                    pstmt.setString(3, column3);
                    pstmt.executeUpdate();
                }
            }

            // Step 5: Close resources
            pstmt.close();
            conn.close();

            System.out.println("Data inserted successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

