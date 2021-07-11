package fr.alvisevenezia.utils.csv;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

public class CSVBuilder {

    public static void buildFile(String path, HashMap<String,String> values){

        File file = new File(path);

        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static  void addToFile(String path,String msg){

        File file = new File(path);

        if(!file.exists()){

            buildFile(path,null);

        }

        try {
            Files.writeString(Paths.get(path), msg);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
