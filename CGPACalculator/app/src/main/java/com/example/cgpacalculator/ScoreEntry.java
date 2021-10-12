package com.example.cgpacalculator;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.simple.JSONValue;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
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
            str="Course Name: "+courseName+", Credits: "+credits+", Grade Points: "+gpoints;
            return str;
        }
    }

    int courseCount = 0;
    HashMap<Integer, CourseObject> courseDetailsPerSemester;
    HashMap<String, HashMap<String, Float>> scoreDetailsFromFile;
    int currentSemNumber;
    float sgpa;
    HashMap<String, Float> thisSemester;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_entry);
        courseDetailsPerSemester = new HashMap<Integer, CourseObject>();
        currentSemNumber = Integer.parseInt(getIntent().getExtras().getString("Semester"));
        TextView header = (TextView) findViewById(R.id.textView2);
        header.setText("Semester "+currentSemNumber);
        thisSemester = new HashMap<>();
        InputStream fd = null;
        try {
            fd = openFileInput("scoreDetails.json");
            scoreDetailsFromFile = DataHandling.fileToHashMap(fd);
            System.out.println(scoreDetailsFromFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void onCalculateSGPA(View v) {
        if(courseCount==0){
            Toast.makeText(this, "Please enter at least one course",Toast.LENGTH_LONG).show();
            return;
        }
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

        sgpa = 10*totalCreditsObtained/(totalCreditsThisSem*1.0f);
        System.out.println("SGPA for semester "+currentSemNumber+" "+sgpa);
        thisSemester.put("CGPA",0.0f);
        thisSemester.put("SGPA",sgpa);
        thisSemester.put("Credits",totalCreditsThisSem*1.0f);
        this.storeDetailsOfThisSem();
        // move to next screen
        Intent intent = new Intent(this, DisplaySGPA.class);
        intent.putExtra("Semester",currentSemNumber);
        startActivity(intent);

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

    public void storeDetailsOfThisSem() {
        System.out.println("Saving the details and sgpa");

        scoreDetailsFromFile.put(Integer.toString(currentSemNumber),thisSemester);
        System.out.println("Debug 1:"+scoreDetailsFromFile);
        String jsonString = "";
        try{
            jsonString = JSONValue.toJSONString(scoreDetailsFromFile);
            System.out.println(jsonString);
        }catch(Exception e){
            System.out.println("ERROR WHILE converting to json");
        }
        Toast.makeText(this,jsonString,Toast.LENGTH_LONG).show();
        try{

            FileOutputStream fout=openFileOutput("scoreDetails.json",MODE_PRIVATE);
            byte b[]=jsonString.getBytes();//converting string into byte array
            fout.write(b);
            fout.close();
            System.out.println("success...");
        }catch(Exception e){System.out.println(e);}
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