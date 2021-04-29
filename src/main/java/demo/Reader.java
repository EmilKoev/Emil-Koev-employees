package demo;

import com.google.gson.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Reader {

    static File chooseFile() throws Exception {
        Scanner scanner = new Scanner(System.in);
        File file = new File("txt_files");
        if (!file.exists()){
            file.mkdir();
        }
        if (file.listFiles().length == 0){
            throw new Exception("file missing");
        }
        int choice;
        int counter = 1;
        for (File f : file.listFiles()) {
            System.out.println(counter++ + " - " + f.getName());
        }
        System.out.print("Choose file : ");
        choice = scanner.nextInt();
        if (choice > file.listFiles().length || choice < 1){
            System.out.println("Dont cheat!");
            return chooseFile();
        }
        return file.listFiles()[choice-1];
    }

    static HashMap<String, ArrayList<Employee>> txtFormat(File file) throws FileNotFoundException {
        HashMap<String, ArrayList<Employee>> projects = new HashMap<>();
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()){
            String line = scanner.nextLine();
            String[] params = line.split(", ");
            LocalDate startAt = dateParser(params[2]);
            LocalDate endAt;
            if (params[3].equals("NULL")){
                endAt = LocalDate.now();
            }else {
                endAt = dateParser(params[3]);
            }

            Employee employee = new Employee(params[0],
                    params[1],
                    startAt,
                    endAt);
            if (!projects.containsKey(employee.getProjectId())){
                projects.put(employee.getProjectId(),new ArrayList<Employee>());
            }
            projects.get(employee.getProjectId()).add(employee);
        }
        return projects;
    }

    static HashMap<String, ArrayList<Employee>> jsonFormat(File file){
        HashMap<String, ArrayList<Employee>> projects = new HashMap<>();
        StringBuilder stringBuilder = new StringBuilder();
        try (Scanner scanner = new Scanner(file)){
            while (scanner.hasNextLine()){
                stringBuilder.append(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        JsonElement element = JsonParser.parseString(stringBuilder.toString());
        JsonObject object = element.getAsJsonObject();
        JsonArray array = object.get("employees").getAsJsonArray();
        for (int i = 0; i < array.size(); i++) {
            Employee employee = gson.fromJson(array.get(i).toString(),Employee.class);
            if (!projects.containsKey(employee.getProjectId())){
                projects.put(employee.getProjectId(),new ArrayList<>());
            }
            projects.get(employee.getProjectId()).add(employee);
        }

        return projects;
    }

    private static LocalDate dateParser(String text){
        //find separator
        char separator = text.replaceAll("[0-9]","").charAt(0);
        String[] params = text.split("" + separator);
        // check if dateformat starts with year or end with year
        LocalDate date;
        if (params[0].length() == 4){
            date = LocalDate.of(Integer.parseInt(params[0]),
                    Integer.parseInt(params[1]),
                    Integer.parseInt(params[2]));
        }else {
            date = LocalDate.of(Integer.parseInt(params[2]),
                    Integer.parseInt(params[1]),
                    Integer.parseInt(params[0]));
        }
        return date;
    }
}
