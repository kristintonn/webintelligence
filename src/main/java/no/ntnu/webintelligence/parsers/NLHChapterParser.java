/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.ntnu.webintelligence.parsers;

import java.io.File;
import java.util.ArrayList;
import no.ntnu.webintelligence.models.NLHChapter;

/**
 *
 * @author Kristin
 */
public class NLHChapterParser {

    private ArrayList<NLHChapter> chapters;

    public NLHChapterParser() {
        chapters = new ArrayList<NLHChapter>();
        addChapters();
    }

    public void addChapters() {
        File dir = new File("src/main/resources/nlh");
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            System.out.println(files[i].getName());
            //TODO: Do something with the htm file
            //NLHChapter chapter = new NLHChapter(null, null, null);
            //chapters.add(chapter);
        }
    }

    public ArrayList<NLHChapter> getChapters() {
        return chapters;
    }

    public static void main(String[] args) {
        NLHChapterParser parser = new NLHChapterParser();
    }
}
