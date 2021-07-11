package fr.alvisevenezia.utils.csv;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class CSVReader {

    public static HashMap<String,String> readStringFile(String path){

        HashMap<String,String>values = new HashMap<>();

        File file = new File(path);

        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;

            while((line = bufferedReader.readLine()) != null){

                String[] lines = line.split(",");

                for(int i = 0;i<lines.length;i+=2){

                    values.put(lines[i],lines[i+1]);

                }

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return values;

    }

}
