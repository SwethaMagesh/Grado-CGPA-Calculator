package com.example.cgpacalculator;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ScoreEntry extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_entry);

    }
    public void onAddCourse(View v){
        AlertDialog.Builder builder =new AlertDialog.Builder(this);
        builder.setTitle("Add Course");
        builder.setMessage("Enter course name, credits and grade points for the course");
        builder.setView(R.layout.course_details_row);
        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //validate the edittexts
                EditText courseNameWidget = (EditText) findViewById(R.id.courseName);
                EditText courseCreditsWidget = (EditText) findViewById(R.id.credits);
                EditText gradeWidget = (EditText) findViewById(R.id.GradePoints);
                String courseName = courseNameWidget.getText().toString().trim();
                int credits = Integer.parseInt(courseCreditsWidget.getText().toString().trim());
                int grade = Integer.parseInt(gradeWidget.getText().toString().trim());
                // if valid add to the screen
                boolean flag = true;
                if(credits >= 1 && credits <=4 && grade >= 5 && grade <=10 && !courseName.equals("")){
                    // add to layout of score entry;
                    LinearLayout existing = findViewById(R.id.courseDetails);
                    LinearLayout staticRow = new LinearLayout(getApplicationContext());
                    TextView cname = new TextView(getApplicationContext());
                    cname.setText(courseName);
                    TextView credit = new TextView(getApplicationContext());
                    credit.setText(credits);
                    TextView gradeWid = new TextView(getApplicationContext());
                    gradeWid.setText(grade);
                    ImageView imgicon = new ImageView(getApplicationContext());
                    imgicon.setImageResource(R.drawable.ic_outline_delete_24);
                    staticRow.addView(cname);
                    staticRow.addView(credit);
                    staticRow.addView(gradeWid);
                    staticRow.addView(imgicon);
                    existing.addView(staticRow);
                }else{
                    flag = false;
                }

                // if invalid
                if(flag == false){
                    // toast
                    Toast.makeText(getApplicationContext(), "INVALID DATA",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void onCalculateSGPA(View v){
        Toast.makeText(this, "will calci later",Toast.LENGTH_LONG).show();
    }
}