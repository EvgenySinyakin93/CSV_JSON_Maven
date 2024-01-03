package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
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
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static java.nio.file.Files.writeString;
import static jdk.internal.org.jline.utils.InfoCmp.Capability.columns;


public class Main {

    //    Инфо о сотрудниках
    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";
        List<Employee> list = parseCSV(columnMapping, fileName);
        String json = listToJson(list);
        String fileNameToJSON = "data.json";
        writeString(json, fileNameToJSON);

        List<Employee> list2 = parseXML("data.xml");
        String json2 = listToJson(list2);
        String fileNameToJSON2 = "data2.json";
        writeString(json2, fileNameToJSON2);
    }

    //    Далее получите список сотрудников, вызвав метод parseCSV():
    public static List<Employee> parseCSV(String[] columns, String fileName) {
        List<Employee> result = new ArrayList<>();
        ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
        strategy.setType(Employee.class);
        strategy.setColumnMapping(columns);
        try (CSVReader reader = new CSVReader(new FileReader(fileName))) {
            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(reader)
                    .withMappingStrategy(strategy)
                    .build();
            result = csv.parse();
        } catch (IOException /*| CsvValidationException*/ e) {
            e.printStackTrace();
        }
        return result;
    }

    //    Полученный список преобразуйте в строчку в формате JSON
    private static String listToJson(List<Employee> list) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        return gson.toJson(list, listType);
    }

    private static void writeString(String json, String fileName) {
        try (FileWriter file = new FileWriter(fileName)) {
            file.write(json);
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static List<Employee> parseXML(String fileName) throws IOException, SAXException, ParserConfigurationException {
        List<Employee> staff = new ArrayList<>();

//        необходимо получить экземпляр класса Document с использованием DocumentBuilderFactory и DocumentBuilder через метод parse()
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(fileName);

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
            if (node.ELEMENT_NODE == node.getNodeType()) {
                Element element = (Element) node;
                //Проверяем имеет ли элемент id
                long id = Integer.parseInt(element.getElementsByTagName("id").item(0).getTextContent());
                String firstName = element.getElementsByTagName("firstName").item(0).getTextContent();
                String lastName = element.getElementsByTagName("lastName").item(0).getTextContent();
                String country = element.getElementsByTagName("country").item(0).getTextContent();
                int age = Integer.parseInt(element.getElementsByTagName("age").item(0).getTextContent());

                Employee employee = new Employee(id, firstName, lastName, country, age);
                staff.add(employee);
            }
        }
        return staff;
    }
}