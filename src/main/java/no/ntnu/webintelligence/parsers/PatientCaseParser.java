/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.ntnu.webintelligence.parsers;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import no.ntnu.webintelligence.models.ICD10;
import no.ntnu.webintelligence.models.PatientCase;

/**
 *
 * @author Ida Katrine
 */
public class PatientCaseParser {

    private ArrayList<PatientCase> parsedCases;

    public PatientCaseParser() {
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
                stringText = removeStopWords(stringText);
                //lage text
                PatientCase caseText = new PatientCase(c, stringText);
                parsedCases.add(caseText);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < parsedCases.size(); i++) {
            //System.out.println(parsedCases.get(i));
        }
    }

    private String removeStopWords(String in) throws FileNotFoundException, IOException {
        BufferedReader reader;
        reader = new BufferedReader(new FileReader("src/main/resources/stopwords.txt"));
        String sCurrentLine;
        while ((sCurrentLine = reader.readLine()) != null) {
            in = in.replaceAll(" " + sCurrentLine + " ", " ");
        }
        return in;
    }

    public ArrayList<PatientCase> getParsedCases() {
        return parsedCases;
    }

    public static void main(String[] args) {
        PatientCaseParser parseCase = new PatientCaseParser();
    }
}
