package com.example.cgpacalculator;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

public class DisplaySGPA extends AppCompatActivity {
    HashMap<String, HashMap<String, Float>> scoreDetailsFromFile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_sgpa);
        InputStream fd = null;
        try {
            fd = openFileInput("scoreDetails.json");
            scoreDetailsFromFile = DataHandling.fileToHashMap(fd);
            System.out.println(scoreDetailsFromFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}