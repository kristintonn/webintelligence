/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.ntnu.webintelligence.parsers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import no.ntnu.webintelligence.models.NLHChapter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Kristin
 */
public class NLHChapterParser {

    private ArrayList<NLHChapter> chapters;

    public NLHChapterParser() throws IOException {
        chapters = new ArrayList<NLHChapter>();
        File dir = new File("src/main/resources/nlh");
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            //System.out.println(files[i].getName().replaceAll(".htm", ""));
            readHTMFile(files[i]);
        }
    }

    private void addChapter(NLHChapter chapter) {
        chapters.add(chapter);
    }

    private void readHTMFile(File file) throws IOException {
        Document doc = Jsoup.parse(file, "iso-8859-1");
        Elements body = doc.getElementsByClass("seksjon2");

        String bodyString = body.toString();
        String bodyResult = Jsoup.parse(bodyString).text();
        //String id = file.getName().replaceAll(".htm", "");

        ArrayList<String> titles = new ArrayList<String>();

        Elements h3s = doc.getElementsByTag("h3");
        Elements h2s = doc.getElementsByTag("h2");
        Elements h1s = doc.getElementsByTag("h1");

        // TODO: Fjerne *

        for (Element el : h1s) {
            if (!el.text().equals("Norsk legemiddelhåndbok") && !el.text().startsWith("Foreningen for utgivelse av Norsk legemiddelh")) {
                titles.add(el.text());
            }
        }
        for (Element el : h2s) {
            titles.add(el.text());
        }
        for (Element el : h3s) {
            titles.add(el.text());
        }

        String id;
        String title;
        String chapterText;
        if (titles.size() == 1) {
            chapterText = bodyResult;
            chapterText = removeStopWords(chapterText);
            int spaceIndex = titles.get(0).indexOf("\u00A0");
            if (spaceIndex > 0) {
                id = titles.get(0).substring(0, spaceIndex);
                title = titles.get(0).substring(spaceIndex, titles.get(0).length());
                NLHChapter chapter = new NLHChapter(id, title, chapterText);
                addChapter(chapter);
            }
        } else if (titles.size() > 1) {
            for (int i = 0; i < titles.size(); i++) {
                int startIndex;
                int stopIndex;
                int spaceIndex = titles.get(i).indexOf("\u00A0");
                if (spaceIndex < 0) {
                    continue;
                }
                id = titles.get(i).substring(0, spaceIndex);
                title = titles.get(i).substring(spaceIndex + 1, titles.get(i).length());
                startIndex = bodyResult.indexOf(titles.get(i));
                if (i < titles.size() - 1) {
                    stopIndex = bodyResult.indexOf(titles.get(i + 1));
                    if (startIndex == -1 || stopIndex == -1) {
                        return;
                    }
                } else {
                    stopIndex = bodyResult.length() - 1;
                }
                chapterText = bodyResult.substring(startIndex, stopIndex);
                chapterText = removeStopWords(chapterText);
                NLHChapter chapter = new NLHChapter(id, title, chapterText);
                addChapter(chapter);
            }
        }

//        String title = "";
//        Elements docs;
//        if (doc.select("h3") != null) {
//            docs = doc.select("h3");
//        } else if (doc.select("h2") != null) {
//            docs = doc.select("h2");
//        } else {
//            docs = doc.select("h1");
//        }
    }

    public ArrayList<NLHChapter> getChapters() {
        return chapters;
    }

    private String removeStopWords(String in) throws FileNotFoundException, IOException {
        BufferedReader reader;
        reader = new BufferedReader(new FileReader("src/main/resources/stopwords.txt"));
        String sCurrentLine;
        while ((sCurrentLine = reader.readLine()) != null) {
            in = in.replaceAll(" " + sCurrentLine + " ", " ");
        }
        reader.close();
        return in;
    }
}
