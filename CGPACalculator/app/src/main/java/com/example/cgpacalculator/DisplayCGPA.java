package com.example.cgpacalculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;

public class DisplayCGPA extends AppCompatActivity {

    String roll_no;
    HashMap totalDetails;
    HashMap scoreDetailsFromFile;
    InputStream fd=null;
    int semesterNo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_cgpa);
        //Set home button
        ActionBar action=getSupportActionBar();
        action.setHomeAsUpIndicator(R.drawable.home_24);
        action.setDisplayHomeAsUpEnabled(true);
        roll_no=getIntent().getExtras().getString("Roll No");
        try {
            fd = openFileInput("scoreDetails.json");
            totalDetails = DataHandling.fileToHashMap(fd);
            scoreDetailsFromFile= (HashMap) totalDetails.get(roll_no);
            System.out.println(scoreDetailsFromFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        semesterNo = getIntent().getExtras().getInt("Semester");
//        if(semesterNo!=0)
        displaySgpaAndCgpa();
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
            case android.R.id.home:
                Intent intent=new Intent(this,SelectSemester.class);
                intent.putExtra("Roll No",roll_no);
                startActivity(intent);
                return true;
            case R.id.logout:
                Intent intent1=new Intent(this,LoginActivity.class);
                startActivity(intent1);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public ImageView findViewByCustomTag(int number){
        LinearLayout layout = findViewById(R.id.layout1);
        if(number>4)
            layout = findViewById(R.id.layout2);
        ImageView imageView = layout.findViewWithTag(Integer.toString(number));
        return imageView;
    }

    public void displaySgpaAndCgpa(){
        // highlight current sem
        // color all components

        Double sgpa,cgpa;
        TextView sgpaDisplay=(TextView) findViewById(R.id.sgpa_dis);
        TextView cgpaDisplay=(TextView) findViewById(R.id.cgpa_dis);
        if(semesterNo!=0) {
            if (!scoreDetailsFromFile.containsKey(Integer.toString(semesterNo))) {
                System.out.println("Going to get values:)");
                showDialogToEnterDetails();
            }
            HashMap thisSem = (HashMap) scoreDetailsFromFile.get(Integer.toString(semesterNo));
            try {

                sgpa = (Double) thisSem.get("SGPA");
                cgpa = (Double) thisSem.get("CGPA");
                if (cgpa == 0) {
                    cgpaDisplay.setText("Enter details for all previous semesters!!");
                } else {
                    cgpaDisplay.setText("CGPA upto semester " + semesterNo + " is " +String.format("%.2f",cgpa.floatValue()));
                }
                sgpaDisplay.setText("SGPA for the semester " + semesterNo + " is " + String.format("%.2f", sgpa.floatValue()));


            } catch (Exception e) {
                System.out.println(e);
            }
        }
        for (int i = 1; i <= 8; i++) {
            ImageView imageView = findViewByCustomTag(i);
            if (imageView == null) {
                System.out.println(i + " NULL");
                continue;
            }
            if (!scoreDetailsFromFile.containsKey(Integer.toString(i))) {
                System.out.println(i + " uncheck");
                imageView.setColorFilter(ContextCompat.getColor(this, R.color.uncheck_background));
            } else {
                System.out.println(i + " check");
                imageView.setColorFilter(ContextCompat.getColor(this, R.color.check_background));
            }

        }
            if(semesterNo!=0) {
                ImageView imageView = findViewByCustomTag(semesterNo);
                imageView.clearColorFilter();
//                imageView.setColorFilter(ContextCompat.getColor(this, R.color.selected_background));
            }

    }

    public void onClickingSemesterButtons(View view)
    {
        semesterNo=Integer.parseInt(view.getTag().toString());
        System.out.println(semesterNo);
        displaySgpaAndCgpa();
    }
    public void editSemDetails(View v)
    {
        showDialogToEnterDetails();
    }
    void showDialogToEnterDetails(){
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.semester_details, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Grade Entry");
        alert.setView(alertLayout);
        alert.setCancelable(false);
        EditText creditsView=alertLayout.findViewById(R.id.credits_for_sem);
        EditText sgpaView=alertLayout.findViewById(R.id.sgpa_for_sem);
        if (scoreDetailsFromFile.containsKey(Integer.toString(semesterNo))) {
            HashMap thisSem= (HashMap) scoreDetailsFromFile.get(Integer.toString(semesterNo));
            Double sgpa= (Double)thisSem.get("SGPA");
            Double credits=(Double)thisSem.get("Credits");
            creditsView.setText(Double.toString(credits));
            sgpaView.setText(Double.toString(sgpa));
        }

        alert.setNegativeButton("Cancel", (dialog, which) -> Toast.makeText(getBaseContext(), "Cancel clicked", Toast.LENGTH_SHORT).show());
        alert.setPositiveButton("Upload",(dialog,which)->{
            String creditVal=creditsView.getText().toString();
            String sgpaVal=sgpaView.getText().toString();
            try {
                uploadCurrentSemMarksIntoJSONFile(creditVal,sgpaVal);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Toast.makeText(this,"Uploaded successfully!!",Toast.LENGTH_LONG).show();
        });
        alert.setNeutralButton("Enter course wise marks",(dialog,which)->{
            //Start Intent to scoreEntry Activity
            Intent intent=new Intent(this,ScoreEntry.class);
            intent.putExtra("Roll No",roll_no);
            intent.putExtra("Semester",Integer.toString(semesterNo));
            startActivity(intent);
        });
        alert.show();
    }
    void uploadCurrentSemMarksIntoJSONFile(String creditVal,String sgpaVal) throws FileNotFoundException {
        System.out.println("Current function: uploadCurrentSemMarksIntoJSONFile");
        Double sgpa=Double.parseDouble(sgpaVal);
        Double credit=Double.parseDouble(creditVal);
        HashMap<String,Double>thisSemester=new HashMap<>();
        Double cgpa=0.0;
        thisSemester.put("CGPA", cgpa);
        thisSemester.put("SGPA", sgpa);
        thisSemester.put("Credits",credit);
        scoreDetailsFromFile.put(Integer.toString(semesterNo),thisSemester);
        cgpa = CalculateCGPA.getCgpaFromPrevCgpa(scoreDetailsFromFile,semesterNo);
        // updating the cgpa from 0 to actual value
        thisSemester.put("CGPA", cgpa);
        scoreDetailsFromFile.put(Integer.toString(semesterNo),thisSemester);
        for(int i=1;i<=8;i++)
        {
            System.out.println("Hi");
            if(scoreDetailsFromFile.containsKey(Integer.toString(i)) )
            {
                //Calculate CGPA
                thisSemester= (HashMap<String, Double>) scoreDetailsFromFile.get(Integer.toString(i));
                cgpa=CalculateCGPA.getCgpaFromPrevCgpa(scoreDetailsFromFile,i);
                thisSemester.put("CGPA",cgpa);
                System.out.println("Putting CGPA into semester"+i+" "+cgpa);
                scoreDetailsFromFile.put(Integer.toString(i),thisSemester);
            }
        }
        totalDetails.put(roll_no,scoreDetailsFromFile);
        FileOutputStream fout = openFileOutput("scoreDetails.json", MODE_PRIVATE);
        DataHandling.writeIntoFile(fout,totalDetails);
        displaySgpaAndCgpa();
    }
}