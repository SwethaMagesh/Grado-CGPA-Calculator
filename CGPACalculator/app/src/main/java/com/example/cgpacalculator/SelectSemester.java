package com.example.cgpacalculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

public class SelectSemester extends AppCompatActivity {
    String roll_no;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_semester);
        roll_no=getIntent().getExtras().getString("Roll No");
        //Set home button
        ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.home_24);
        actionBar.setDisplayHomeAsUpEnabled(true);

        populateSpinner();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId()){
            case R.id.logout:
                Intent intent=new Intent(this,LoginActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    void populateSpinner(){
        Spinner spin = (Spinner) findViewById(R.id.spinner);
        String semesters[] = {"Select","1", "2","3","4","5","6","7","8"};
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,semesters);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(adapter);
    }
    public  void goToNextPage(View v){
        //GET the value of spinner
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        String semester = spinner.getSelectedItem().toString();

        // if invalid toast
        if(semester.equals("Select")){
            Toast.makeText(this, "Please select a valid semester number", Toast.LENGTH_LONG).show();
        }
        // if valid, go to next page and bundle the sem number along with intent
        else{
            Intent intent = new Intent(this, ScoreEntry.class);
            intent.putExtra("Semester",semester);
            intent.putExtra("Roll No",roll_no);
            startActivity(intent);
        }
    }
    public void viewReportPage(View view)
    {
        InputStream fd = null;
        HashMap totalDetails;
        try {
            System.out.println("Hi");
            fd = openFileInput("scoreDetails.json");
            totalDetails = DataHandling.fileToHashMap(fd);
            if(totalDetails!=null && totalDetails.containsKey(roll_no)) {
                Intent intent = new Intent(this, DisplayCGPA.class);
                intent.putExtra("Semester", 0);
                intent.putExtra("Roll No", roll_no);
                startActivity(intent);
            }
            else{
                Toast.makeText(this,"Enter marks to view report",Toast.LENGTH_LONG).show();
            }
        } catch (FileNotFoundException e) {
            Toast.makeText(this,"Enter marks to view report",Toast.LENGTH_LONG).show();
        }

    }
}