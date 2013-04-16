/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.ntnu.webintelligence.parsers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import no.ntnu.webintelligence.models.PatientCase;
import no.ntnu.webintelligence.stemmers.NorwegianStemmer;
import org.apache.lucene.analysis.no.NorwegianLightStemmer;

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
        NorwegianLightStemmer norStemmer = new NorwegianLightStemmer();

        for (int c = 1; c <= 8; c++) {
            String stringText = "";
            fileName = "src/main/resources/cases/case" + c + ".txt";
            try {
                String sCurrentLine;
                is = new BufferedReader(new FileReader(fileName));
                while ((sCurrentLine = is.readLine()) != null) {
                    stringText += sCurrentLine + " ";
                    /*String[] strings = sCurrentLine.split(" ");
                    for (int i = 0; i < strings.length; i++) {
                        String oldString = strings[i];
                        char[] charArray = oldString.toCharArray();
                        int stopIndex = norStemmer.stem(charArray, charArray.length);
                        sCurrentLine = "";
                        for (int j = 0; j < stopIndex; j++) {
                            sCurrentLine += charArray[j];
                        }
                        strings[i] = sCurrentLine;
                    }
                    for (int i = 0; i < strings.length; i++) {
                        stringText += strings[i] + " ";
                    }*/
                }
                stringText = removeStopWords(stringText);
                PatientCase pcase = new PatientCase(c, stringText);
                parsedCases.add(pcase);
            } catch (Exception e) {
            }
        }
    }

    private String removeStopWords(String in) throws FileNotFoundException, IOException {
        BufferedReader reader;
        reader = new BufferedReader(new FileReader("src/main/resources/stopwords.txt"));
        String sCurrentLine;
        while ((sCurrentLine = reader.readLine()) != null) {
            in = in.replaceAll(" " + sCurrentLine + " ", " "); //TODO: Støtte for stor forbokstav?
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
