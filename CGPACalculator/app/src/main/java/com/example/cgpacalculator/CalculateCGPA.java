package com.example.cgpacalculator;

import java.util.HashMap;

public class CalculateCGPA {
    public static double getCgpaFromPrevCgpa(HashMap scoreDetailsFromFile, int sem){

        double cgpa=0;
        double credits=0;
        double totalCredits=0;
        for(int i=1;i<=sem;i++){

            if(scoreDetailsFromFile.containsKey(Integer.toString(i))){
                HashMap thisSem = (HashMap) scoreDetailsFromFile.get(Integer.toString(i));
                double creditsThisSem = (double) thisSem.get("Credits");
                double sgpa = (double) thisSem.get("SGPA");
                totalCredits +=creditsThisSem;
                credits += sgpa*creditsThisSem;
            }
            else{
                cgpa =0;
                return cgpa;
            }
        }
        cgpa = credits/totalCredits;
        cgpa = Math.round(cgpa*100)/100;
        System.out.println(cgpa+" sem number"+sem);
        return cgpa;
    }

}
