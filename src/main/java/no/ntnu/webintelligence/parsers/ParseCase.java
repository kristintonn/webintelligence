/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.ntnu.webintelligence.parsers;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.ArrayList;
import no.ntnu.webintelligence.models.ICD10;
import no.ntnu.webintelligence.models.PatientCase;

/**
 *
 * @author Ida Katrine
 */
public class ParseCase {

    private ArrayList<PatientCase> parsedCases;

    public ParseCase() {
        parsedCases = new ArrayList<PatientCase>();

        BufferedReader is;

        String fileName;
        

        for (int c = 1; c <= 8; c++) {
            String stringText = "";
            fileName = "src/main/resources/cases/case" + c + ".txt";
           
            try {
                String sCurrentLine;
                is = new BufferedReader(new FileReader(fileName));
                while ((sCurrentLine = is.readLine()) != null) {
                    stringText += sCurrentLine + " ";
                }

                //lage text
                PatientCase caseText = new PatientCase(c, stringText);
                parsedCases.add(caseText);


            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        for (int i = 0; i < parsedCases.size(); i++) {
            System.out.println(parsedCases.get(i));
        }
    }

    public static void main(String[] args) {
        ParseCase parseCase = new ParseCase();
    }
}
