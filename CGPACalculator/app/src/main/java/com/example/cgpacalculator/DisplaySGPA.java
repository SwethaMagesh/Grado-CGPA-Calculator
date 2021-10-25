package com.example.cgpacalculator;

import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

public class DisplaySGPA extends AppCompatActivity {
    HashMap<String, HashMap<String, Float>> scoreDetailsFromFile;
    int currentSemNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_sgpa);
        currentSemNumber = getIntent().getExtras().getInt("Semester");
        InputStream fd = null;
        try {
            fd = openFileInput("scoreDetails.json");
            scoreDetailsFromFile = DataHandling.fileToHashMap(fd);
            System.out.println(scoreDetailsFromFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        setLayout();
    }
    void setLayout(){
        // set progress bar and text for sgpa
        Double sgpa=0.0, cgpa=0.0, credits=0.0;

        HashMap thisSem = scoreDetailsFromFile.get(Integer.toString(currentSemNumber));
        try {
            sgpa= (Double) thisSem.get("SGPA");
            cgpa=(Double) thisSem.get("CGPA");
            credits=(Double) thisSem.get("Credits");
        }
        catch(Exception e)
        {
            System.out.println(e);
        }

        TextView textViewSGPA = (TextView) findViewById(R.id.sgpa_text2);
        textViewSGPA.setText(""+sgpa);

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.sgpaProgressBar);
        int percentSGPA =  (int) Math.round(sgpa)*10;
        progressBar.setProgress(percentSGPA);
        progressBar.setMax(100);
        TextView textViewCGPA = (TextView) findViewById(R.id.cgpa_text);
        textViewCGPA.setText(""+cgpa);
        ProgressBar cgpaProgressbar = (ProgressBar) findViewById(R.id.cgpaProgressBar);

        int percentCGPA = (int) Math.round(cgpa*10);
        cgpaProgressbar.setProgress(percentCGPA);
        cgpaProgressbar.setMax(100);
        System.out.println("Updated");
    }
}