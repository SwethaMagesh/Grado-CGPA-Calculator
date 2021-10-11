package com.example.cgpacalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class SelectSemester extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_semester);
        populateSpinner();
    }
    void populateSpinner(){
        Spinner spin = (Spinner) findViewById(R.id.spinner);
        String semesters[] = {"Select","1", "2","3","4","5","6","7","8","9","10"};
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
            startActivity(intent);
        }
    }
}