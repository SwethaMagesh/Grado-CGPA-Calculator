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

public class ScoreEntry extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_entry);

    }

    public void onCalculateSGPA(View v) {
        Toast.makeText(this, "will calci later", Toast.LENGTH_LONG).show();
    }

    public void onAddCourse(View v) {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.course_details_row, null);
        final EditText courseName = alertLayout.findViewById(R.id.courseName);
        final EditText credits = alertLayout.findViewById(R.id.credits);
        final EditText grades=alertLayout.findViewById(R.id.GradePoints);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Grade Entry");
        alert.setView(alertLayout);
        alert.setCancelable(false);
        alert.setNegativeButton("Cancel", (dialog, which) -> Toast.makeText(getBaseContext(), "Cancel clicked", Toast.LENGTH_SHORT).show());

        alert.setPositiveButton("Submit", (dialog, which) -> {
            String course_name = courseName.getText().toString();
            int credit = Integer.parseInt(credits.getText().toString());
            int grade=Integer.parseInt(grades.getText().toString());
            boolean flag=true;
            if(credit >= 1 && credit <=4 && grade >= 5 && grade <=10 && !course_name.equals("")){
                  View static_details=inflater.inflate(R.layout.static_course_details,null);
                  TextView c_nameWid= (TextView) static_details.findViewById(R.id.cName);
                  TextView cr_nameWid=(TextView) static_details.findViewById(R.id.crName);
                  TextView gpointWid=(TextView) static_details.findViewById(R.id.gPoint);
                  c_nameWid.setText(course_name);
                  cr_nameWid.setText(Integer.toString(credit));
                  gpointWid.setText(Integer.toString(grade));
                  LinearLayout score_entry=(LinearLayout) findViewById(R.id.courseDetails);
                  score_entry.addView(static_details);
            }else{
                flag = false;
            }

            // if invalid
            if(flag == false){
                // toast
                Toast.makeText(ScoreEntry.this, "INVALID DATA",Toast.LENGTH_LONG).show();
            }

    });
        AlertDialog dialog = alert.create();
        dialog.show();
    }
}