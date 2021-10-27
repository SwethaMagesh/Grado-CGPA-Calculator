package com.example.cgpacalculator;

import android.widget.Toast;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;

public class DataHandling {
    public static JSONObject fileToHashMap(InputStream fd,String roll_no) {
        //readFile();
        try {
            String jsonFileContents = jsonToString(fd);
            System.out.println(jsonFileContents);
            JSONObject totalDetails = jsonToMap(jsonFileContents,roll_no);
            System.out.println(totalDetails);
            return totalDetails;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }

    public static JSONObject jsonToMap(String jstring,String roll_no) {
        try {
            System.out.println("Converting to hashmap");
            JSONObject json = (JSONObject) JSONValue.parse(jstring);
            return (JSONObject) json.get(roll_no);
//            System.out.println(json.size());
//            return json;
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

    public static String hashMapToJSON(HashMap totalDetails) {
        System.out.println("Debug 1:" + totalDetails);
        String jsonString = "";
        try {
            jsonString = JSONValue.toJSONString(totalDetails);
            System.out.println(jsonString);
        } catch (Exception e) {
            System.out.println("ERROR WHILE converting to json");
        }
        return jsonString;
    }

    public static void writeIntoFile(FileOutputStream fout, HashMap totalDetails) {
        try {

//            FileOutputStream fout = openFileOutput("scoreDetails.json", MODE_PRIVATE);

            String jsonString=hashMapToJSON(totalDetails);

            byte b[] = jsonString.getBytes();//converting string into byte array
            fout.write(b);
            fout.close();
            System.out.println("success...");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}

