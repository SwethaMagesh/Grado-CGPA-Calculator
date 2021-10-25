package com.example.cgpacalculator;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;

public class DisplayCGPA extends AppCompatActivity {

    HashMap<String, HashMap<String, Double>> scoreDetailsFromFile;
    int semesterNo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_cgpa);
        InputStream fd = null;
        try {
            fd = openFileInput("scoreDetails.json");
            scoreDetailsFromFile = DataHandling.fileToHashMap(fd);
            System.out.println(scoreDetailsFromFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void onClickingSemesterButtons(View view)
    {
        semesterNo=Integer.parseInt(view.getTag().toString());
        System.out.println(semesterNo);
        Double sgpa,cgpa;
        TextView sgpaDisplay=(TextView) findViewById(R.id.sgpa_dis);
        TextView cgpaDisplay=(TextView) findViewById(R.id.cgpa_dis);
        if(!scoreDetailsFromFile.containsKey(Integer.toString(semesterNo)))
        {
            System.out.println("Going to get values:)");
            showDialogToEnterDetails();
        }
        HashMap thisSem = scoreDetailsFromFile.get(Integer.toString(semesterNo));
        try {

            sgpa = (Double) thisSem.get("SGPA");
            cgpa=(Double) thisSem.get("CGPA");
            if(cgpa==0)
            {
                cgpaDisplay.setText("Make sure that you have entered all previous semester marks!!");
            }
            else{
                cgpaDisplay.setText("Your CGPA upto semester "+semesterNo+" : "+cgpa);
            }
            sgpaDisplay.setText("Your SGPA for the semester "+semesterNo+" : "+sgpa);
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
    void showDialogToEnterDetails(){
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.semester_details, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Grade Entry");
        alert.setView(alertLayout);
        alert.setCancelable(false);
        alert.setNegativeButton("Cancel", (dialog, which) -> Toast.makeText(getBaseContext(), "Cancel clicked", Toast.LENGTH_SHORT).show());
        alert.setPositiveButton("Upload",(dialog,which)->{
            EditText creditsView=alertLayout.findViewById(R.id.credits_for_sem);
            EditText sgpaView=alertLayout.findViewById(R.id.sgpa_for_sem);
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
        thisSemester.put("CGPA",0.0);
        thisSemester.put("SGPA",sgpa);
        thisSemester.put("Credits",credit);
        scoreDetailsFromFile.put(Integer.toString(semesterNo),thisSemester);
        FileOutputStream fout = openFileOutput("scoreDetails.json", MODE_PRIVATE);
        DataHandling.writeIntoFile(fout,scoreDetailsFromFile);
    }
}