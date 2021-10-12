package com.example.cgpacalculator;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.HttpAuthHandler;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.StandardOpenOption;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class ScoreEntry extends AppCompatActivity {
    public class CourseObject{
        String courseName;
        int credits;
        int gpoints;

        CourseObject(String cn, int cr, int gp){
            courseName = cn;
            credits = cr;
            gpoints = gp;
        }
        @Override
        public String toString(){
            String str="";
            str="Course Name:"+courseName+",Credits:"+credits+",Grade Points:"+gpoints;
            return str;
        }
    }
    public class SemesterObject{
        float sgpa;
        float cgpa;
        HashMap<Integer, CourseObject> courseDetailsPerSemester;

        SemesterObject(float sg,float cg,HashMap<Integer,CourseObject>crDetails){
            sgpa=sg;
            cgpa=cg;
            courseDetailsPerSemester=crDetails;
        }
    }
    int courseCount = 0;
    HashMap<Integer, CourseObject> courseDetailsPerSemester;
    int currentSemNumber;
    float sgpa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_entry);
        courseDetailsPerSemester = new HashMap<Integer, CourseObject>();
        currentSemNumber = Integer.parseInt(getIntent().getExtras().getString("Semester"));
        System.out.println("Current Sem "+currentSemNumber);
    }

    public void onCalculateSGPA(View v) throws JSONException {
        int totalCreditsThisSem  = 0;
        int totalCreditsObtained = 0;
        // calculate sgpa from the given data
        for (Map.Entry mapElement : courseDetailsPerSemester.entrySet()) {
            //int key = (int) mapElement.getKey();
            CourseObject value = (CourseObject) mapElement.getValue() ;
            totalCreditsObtained += value.gpoints * value.credits;
            totalCreditsThisSem  += value.credits * 10;
            System.out.println("Till course "+value.courseName+" "+totalCreditsObtained);
        }
        sgpa = totalCreditsObtained/(totalCreditsThisSem*1.0f);
        System.out.println("SGPA for semester "+currentSemNumber);
        this.storeDetailsOfThisSem();

    }

    public void onAddCourse(View v) {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.course_details_row, null);
        final EditText editTextCourseName = alertLayout.findViewById(R.id.courseName);
        final EditText editTextCredits = alertLayout.findViewById(R.id.credits);
        final EditText editTextGrades=alertLayout.findViewById(R.id.GradePoints);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Grade Entry");
        alert.setView(alertLayout);
        alert.setCancelable(false);
        alert.setNegativeButton("Cancel", (dialog, which) -> Toast.makeText(getBaseContext(), "Cancel clicked", Toast.LENGTH_SHORT).show());

        alert.setPositiveButton("Submit", (dialog, which) -> {
            String courseName = editTextCourseName.getText().toString();
            int credits = Integer.parseInt(editTextCredits.getText().toString());
            int grade = Integer.parseInt(editTextGrades.getText().toString());

            boolean validFlag = true;
            if(credits >= 1 && credits <=4 && grade >= 5 && grade <=10 && !courseName.equals("")){
                  View staticDetails=inflater.inflate(R.layout.static_course_details,null);
                 courseCount +=1;
                   staticDetails.setId(courseCount);
                  TextView textViewCourseName = (TextView) staticDetails.findViewById(R.id.cName);
                  TextView textViewCredits =(TextView) staticDetails.findViewById(R.id.crName);
                  TextView textViewGrade =(TextView) staticDetails.findViewById(R.id.gPoint);
                  textViewCourseName.setText(courseName);
                  textViewCredits.setText(Integer.toString(credits));
                  textViewGrade.setText(Integer.toString(grade));
                  LinearLayout scoreEntry=(LinearLayout) findViewById(R.id.courseDetails);
                  scoreEntry.addView(staticDetails);
                  // add it to datastructure adn increment count
                CourseObject currentCourse = new CourseObject(courseName, credits, grade);
                courseDetailsPerSemester.put(courseCount, currentCourse);
            }else{
                validFlag = false;
            }

            // if invalid
            if(validFlag == false){
                // toast
                Toast.makeText(ScoreEntry.this, "INVALID DATA",Toast.LENGTH_LONG).show();
            }

    });
        AlertDialog dialog = alert.create();
        dialog.show();
    }

    public void storeDetailsOfThisSem() throws JSONException {
        System.out.println("Saving the details and sgpa");
        // save semester num : spga, courseDetailsPerSemester
        /*

        Sem No:
        {
        sgpa:
        cgpa:
        courses:[
        1: courseObject,
        2: courseObject
        ]
        }
         */
        HashMap<Integer,SemesterObject> entireGradeDetails=new HashMap<>();
        SemesterObject semesterObject=new SemesterObject(sgpa,0,courseDetailsPerSemester);
        entireGradeDetails.put(currentSemNumber,semesterObject);
        JSONObject semDetails=new JSONObject(entireGradeDetails);
        System.out.println("Debug 1:"+entireGradeDetails);
        Toast.makeText(this,semDetails.toString(),Toast.LENGTH_LONG).show();
/*
        try{
//            FileOutputStream fileOutputStream=openFileOutput("File.txt",MODE_PRIVATE);
//            fileOutputStream.write(detailsString.getBytes(), StandardOpenOption.APPEND);
//            fileOutputStream.close();
            FileWriter fw=new FileWriter("File.txt",true);
            fw.write(detailsString);
            Toast.makeText(this, "File Saved", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try{
            FileInputStream file=openFileInput("File.txt");
            InputStreamReader inputStreamReader=new InputStreamReader(file);
            BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer=new StringBuffer();
            String lines;
            while((lines=bufferedReader.readLine())!=null)
            {
                stringBuffer.append(lines);
            }
            Toast.makeText(this, "After reading"+stringBuffer.toString(), Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

    }

    public void deleteRow(View view) {
        LinearLayout scoreEntry=(LinearLayout) findViewById(R.id.courseDetails);
        int courseId=((View) view.getParent()).getId();
        scoreEntry.removeView((View)view.getParent());
        courseDetailsPerSemester.remove(courseId);
        for(int i=courseId+1;i<=courseCount;i++)
        {
            CourseObject obj=courseDetailsPerSemester.remove(i);
            courseDetailsPerSemester.put(i-1,obj);
            View v=scoreEntry.findViewById(i);
            v.setId(i-1);
        }
        courseCount -=1;
        Toast.makeText(this,"Course Deleted!!",Toast.LENGTH_LONG).show();
    }
}