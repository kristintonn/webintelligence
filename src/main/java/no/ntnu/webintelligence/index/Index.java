/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.ntnu.webintelligence.index;

import java.io.IOException;
import no.ntnu.webintelligence.models.ICD10;
import org.apache.lucene.document.Document;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Field;
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
        index = new RAMDirectory();
        
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_35, analyzer);
        writer = new IndexWriter(index, config);
        
    }
    
    public void addICD10Document(ICD10 icd10) throws IOException {
        icd10 = new ICD10("1", "Hei", null);
        Document doc = new Document();
        doc.add(new Field("id", icd10.getId(), Field.Store.YES, Field.Index.NO));
        doc.add(new Field("label", icd10.getLabel(), Field.Store.YES, Field.Index.ANALYZED));
        for (String syn : icd10.getSynonyms()) {
            doc.add(new Field("synonyms", syn, Field.Store.YES, Field.Index.ANALYZED));
        }
        writer.addDocument(doc);
        writer.commit();
    }
}
