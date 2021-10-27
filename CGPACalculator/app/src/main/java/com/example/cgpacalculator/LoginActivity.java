package com.example.cgpacalculator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText get_roll_no;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        get_roll_no=(EditText) findViewById(R.id.editTextTextPersonName);
    }
    public void checkLoginDetails(View v)
    {
        String roll_no=get_roll_no.getText().toString();
        if(roll_no.equals(""))
        {
            Toast.makeText(this,"Enter roll no",Toast.LENGTH_LONG).show();
            return;
        }
        else if(!roll_no.matches("^[0-9]+[A-Z]+[0-9]+"))
        {
            Toast.makeText(this,"Enter valid roll no",Toast.LENGTH_LONG).show();
            return;
        }
        Intent intent=new Intent(this,SelectSemester.class);
        intent.putExtra("Roll No",roll_no);
        startActivity(intent);
    }
}