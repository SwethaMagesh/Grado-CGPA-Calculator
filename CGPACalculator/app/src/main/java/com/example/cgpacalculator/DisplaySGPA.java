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
        float sgpa, cgpa, credits;

        HashMap<String, Float> thisSem = scoreDetailsFromFile.get(Integer.toString(currentSemNumber));
        sgpa = thisSem.get("SGPA");
        cgpa = thisSem.get("CGPA");
        credits = thisSem.get("Credits");
        TextView textViewSGPA = (TextView) findViewById(R.id.sgpa_text2);
        textViewSGPA.setText(Float.toString(sgpa));
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.sgpaProgressBar);
        int percentSGPA = Math.round(sgpa*10);
        progressBar.setProgress(percentSGPA);
        progressBar.setMax(100);

        TextView textViewCGPA = (TextView) findViewById(R.id.cgpa_text);
        textViewCGPA.setText(Float.toString(cgpa));
        ProgressBar cgpaProgressbar = (ProgressBar) findViewById(R.id.cgpaProgressBar);
        int percentCGPA = Math.round(sgpa*10);
        cgpaProgressbar.setProgress(percentCGPA);
        cgpaProgressbar.setMax(100);


    }
}