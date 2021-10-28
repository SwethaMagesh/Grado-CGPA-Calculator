package com.example.cgpacalculator;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

public class DisplaySGPA extends AppCompatActivity {
    String roll_no;
    HashMap scoreDetailsFromFile;
    HashMap totalDetails;
    int currentSemNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_sgpa);

        //Trying to set back button
        ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.mipmap.home_button);
        actionBar.setDisplayHomeAsUpEnabled(true);

        currentSemNumber = getIntent().getExtras().getInt("Semester");
        roll_no=getIntent().getExtras().getString("Roll No");
        InputStream fd = null;
        try {
            fd = openFileInput("scoreDetails.json");
            totalDetails = DataHandling.fileToHashMap(fd);
            scoreDetailsFromFile= (HashMap) totalDetails.get(roll_no);
            System.out.println(scoreDetailsFromFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        setLayout();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem menuItem)
    {
        switch (menuItem.getItemId())
        {
            case android.R.id.home:
                Intent intent=new Intent(this,SelectSemester.class);
                intent.putExtra("Roll No",roll_no);
                startActivity(intent);
                return true;
            case R.id.logout:
                intent=new Intent(this,LoginActivity.class);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(menuItem);
    }
    void setLayout(){
        // set progress bar and text for sgpa
        Double sgpa=0.0, cgpa=0.0, credits=0.0;

        HashMap thisSem = (HashMap) scoreDetailsFromFile.get(Integer.toString(currentSemNumber));
        sgpa= (Double) thisSem.get("SGPA");

        TextView textViewSGPA = (TextView) findViewById(R.id.sgpa_text2);
        textViewSGPA.setText(String.format("%.2f",sgpa.floatValue()));

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.sgpaProgressBar);
        int percentSGPA =  (int) Math.round(sgpa)*10;
        progressBar.setProgress(percentSGPA);
        progressBar.setMax(100);
    }

    public void displayCGPA(View view)
    {
        Intent intent = new Intent(this, DisplayCGPA.class);
        intent.putExtra("Roll No",roll_no);
        intent.putExtra("Semester",currentSemNumber);
        startActivity(intent);
    }
}