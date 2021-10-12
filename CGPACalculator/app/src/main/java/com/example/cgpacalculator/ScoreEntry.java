package com.example.cgpacalculator;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

    public void onCalculateSGPA(View v) {
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
                  TextView textViewCourseName = (TextView) staticDetails.findViewById(R.id.cName);
                  TextView textViewCredits =(TextView) staticDetails.findViewById(R.id.crName);
                  TextView textViewGrade =(TextView) staticDetails.findViewById(R.id.gPoint);
                  textViewCourseName.setText(courseName);
                  textViewCredits.setText(Integer.toString(credits));
                  textViewGrade.setText(Integer.toString(grade));
                  LinearLayout scoreEntry=(LinearLayout) findViewById(R.id.courseDetails);
                  scoreEntry.addView(staticDetails);
                  // add it to datastructure adn increment count
                courseCount +=1;
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

    public void storeDetailsOfThisSem(){
        System.out.println("Saving the details and sgpa");
        // save semester num : spga, courseDetailsPerSemester
    }
}