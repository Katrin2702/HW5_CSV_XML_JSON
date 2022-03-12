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
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        //задача 1
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";
        List<Employee> list = parseCSV(columnMapping, fileName);
        String json = listToJson(list);
        writeString(json, "data.json");

        //задача 2
        List<Employee> list1 = parseXML("data.xml");
        String json1 = listToJson(list1);
        writeString(json1, "data1.json");

        //задача 3
        String json2 = readString("data.json");
        List<Employee> list2 = jsonToList(json2);
        list2.forEach(System.out::println);

    }

    public static List<Employee> parseCSV(String[] columnMapping, String fileName) {
        List<Employee> staff = null;
        try (CSVReader csvReader = new CSVReader(new FileReader(fileName))) {
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);
            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(csvReader)
                    .withMappingStrategy(strategy)
                    .build();
            staff = csv.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return staff;
    }

    public static String listToJson(List<Employee> list) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Type listType = new TypeToken<List<Employee>>() {}.getType();
        String json = gson.toJson(list, listType);
        return json;
    }

    public static void writeString(String json, String fileName) {
        try (FileWriter file = new FileWriter(fileName)) {
            file.write(json);
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static List<Employee> parseXML(String fileName) {
    List<Employee> list = new ArrayList<>();
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse(fileName);
            Node root = document.getDocumentElement();
            NodeList nodeList = root.getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                try {
                    String[] dataEmployee = node.getTextContent().split("\n");
                    for (int j = 0; j < dataEmployee.length; j++) {
                        dataEmployee[j] = dataEmployee[j].replaceAll(" ", "");
                    }
                    list.add(new Employee(Long.parseLong(dataEmployee[1]), dataEmployee[2], dataEmployee[3],
                            dataEmployee[4], Integer.parseInt(dataEmployee[5])));
                } catch (Exception exception) {}
            }
        } catch(ParserConfigurationException ex){
            ex.printStackTrace();
            } catch(SAXException ex){
            ex.printStackTrace();
            } catch(IOException ex){
            ex.printStackTrace();
            }
        return list;
    }

    public static String readString(String fileName) {
        String s = null;
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
                s = br.readLine();
        } catch (IOException ex){
                System.out.println(ex.getMessage());
        }
        return s;
    }

    public static List<Employee> jsonToList(String json) {
        List<Employee> list = new ArrayList<>();
        JSONParser parser = new JSONParser();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        try {
            Object obj = parser.parse(json);
            JSONArray arr = (JSONArray) obj;
            for (Object object : arr) {
                list.add(gson.fromJson(object.toString(), Employee.class));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return list;
        }

}













