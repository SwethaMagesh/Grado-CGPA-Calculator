package com.example.cgpacalculator;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.DataInputStream;
import java.io.InputStream;

public class DataHandling {
    public static JSONObject fileToHashMap(InputStream fd){
        //readFile();
        try {
            String jsonFileContents = jsonToString(fd);
            System.out.println(jsonFileContents);
            JSONObject  scoreDetailsFromFile = jsonToMap(jsonFileContents);
            System.out.println(scoreDetailsFromFile);
            return scoreDetailsFromFile;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }
    public static JSONObject jsonToMap(String jstring) {
        try {
            System.out.println("Converting to hashmap");
            JSONObject json = (JSONObject) JSONValue.parse(jstring);
            System.out.println(json.size());
            return json;
            //System.out.println(map.keySet());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }
    public static String jsonToString(InputStream fd) {
        byte[] fileContents;
        try {
            DataInputStream istream = new DataInputStream(fd);
            fileContents = new byte[istream.available()];
            istream.read(fileContents);
            String jsonContents = new String(fileContents);
            return jsonContents;
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return "";


    }


}
