/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.ntnu.webintelligence.index;

import java.io.IOException;
import java.util.ArrayList;
import no.ntnu.webintelligence.models.ICD10;
import no.ntnu.webintelligence.parsers.ICD10Parser;
import org.apache.lucene.document.Document;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

/**
 *
 * @author Kristin
 */
public class Index {

    Directory index;
    Analyzer analyzer;
    IndexWriter writer;

    public Index() throws IOException {
        analyzer = new StandardAnalyzer(Version.LUCENE_35);
        index = new RAMDirectory(); //TODO: Replace
        
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_35, analyzer);
        writer = new IndexWriter(index, config);
        
        ICD10Parser parser = new ICD10Parser();
        ArrayList<ICD10> parsedICDs = parser.getParsedICDs();
        for (int i = 0; i<parsedICDs.size(); i++){
            addICD10Document(parsedICDs.get(i));
        }
        writer.close();
        // Skrive ut indeksen
        IndexReader reader = IndexReader.open(index);
        int num = reader.numDocs();
        //System.out.println(num);
        for (int i = 0; i<num; i++){
            if (!reader.isDeleted(i)){
                Document d = reader.document(i);
                //System.out.println("d= " + d);
            }
        }
    }
    
    public void addICD10Document(ICD10 icd10) throws IOException {
        Document doc = new Document();
        doc.add(new Field("id", icd10.getId(), Field.Store.YES, Field.Index.ANALYZED));
        doc.add(new Field("label", icd10.getLabel() == null ? "" : icd10.getLabel(), Field.Store.YES, Field.Index.ANALYZED));
        for (String syn : icd10.getSynonyms()) {
            doc.add(new Field("synonym", syn, Field.Store.YES, Field.Index.ANALYZED));
        }
        writer.addDocument(doc);
        writer.commit();
    }
    
    public Analyzer getAnalyzer(){
        return analyzer;
    }
    
    public Directory getIndex(){
        return index;
    }
    
    //TODO: Method for adding ATC documents
    
    public static void main(String[] args) throws IOException{
        Index index = new Index();
    }
}
