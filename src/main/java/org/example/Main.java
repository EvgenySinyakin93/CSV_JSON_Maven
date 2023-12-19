package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Main {
    //    Инфо о сотрудниках
    public static void main(String[] args) {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";
        List<Employee> list = parseCSV(columnMapping, fileName);
    }

    //    Далее получите список сотрудников, вызвав метод parseCSV():
    private static List<Employee> parseCSV(String[] columnMapping, String fileName) {
        try (CSVReader reader = new CSVReader(new FileReader(fileName))) {
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);
            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(reader)
                    .withMappingStrategy(strategy)
                    .build();
            List<Employee> list = csv.parse();
            return list;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    //    Полученный список преобразуйте в строчку в формате JSON
    private static List<Employee> jsonToList(String json) {
        List<Employee> list2 = new ArrayList<>();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        try {
            Object obj = new JSONParser().parse(json);
            JSONArray array = (JSONArray) obj;
            for (Object a : array) {
                list2.add(gson.fromJson(a.toString(), Employee.class));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return list2;
    }

    private static List<Employee> parseXML(String fileName) throws IOException, SAXException, ParserConfigurationException {
        List<Employee> list3 = parseXML("data.xml");

//        необходимо получить экземпляр класса Document с использованием DocumentBuilderFactory и DocumentBuilder через метод parse()
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new File("company.xml"));

//        получите из объекта Document корневой узел Node
        Node root = doc.getDocumentElement();
        System.out.println("Корневой элемент: " + root.getNodeName());

//        Получить список дочерних узлов можно при помощи метода getChildNodes():
        NodeList nodeList = root.getChildNodes();

//        Пройдитесь по списку узлов и получите из каждого из них Element
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            System.out.println("Teкyщий элeмeнт: " + node.getNodeName());

            //Проверяем, что текущий узел элемент
            if (node.ELEMENT_NODE == node.getNodeType()){
                Element element = (Element) node;
                //Проверяем имеет ли элемент id

            }
        }
        return list3;
    }
}