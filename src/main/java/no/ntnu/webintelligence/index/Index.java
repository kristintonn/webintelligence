/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.ntnu.webintelligence.index;

import java.io.IOException;
import java.util.ArrayList;

import no.ntnu.webintelligence.models.ATC;
import no.ntnu.webintelligence.models.ICD10;
import no.ntnu.webintelligence.models.NLHChapter;
import no.ntnu.webintelligence.parsers.ATCParser;
import no.ntnu.webintelligence.parsers.ICD10Parser;
import no.ntnu.webintelligence.parsers.NLHChapterParser;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;
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
	IndexWriterConfig config;

	public Index() throws IOException {
		analyzer = new StandardAnalyzer(Version.LUCENE_35);
		index = new RAMDirectory(); // TODO: Replace
		config = new IndexWriterConfig(Version.LUCENE_35, analyzer);

		/*
		 * Skrive ut indeksen IndexReader reader = IndexReader.open(index); int
		 * num = reader.numDocs(); //System.out.println(num); for (int i = 0;
		 * i<num; i++){ if (!reader.isDeleted(i)){ Document d =
		 * reader.document(i); //System.out.println("d= " + d); } }
		 */
	}

	/**
	 * add ICD10 to index
	 */
	public void addICD10() throws IOException {
		writer = new IndexWriter(index, config);

		ICD10Parser parser = new ICD10Parser();
		ArrayList<ICD10> parsedICDs = parser.getParsedICDs();
		for (ICD10 icd10 : parsedICDs) {
			Document doc = new Document();
			doc.add(new Field("id", icd10.getId(), Field.Store.YES,
					Field.Index.ANALYZED));
			doc.add(new Field("label", icd10.getLabel() == null ? "" : icd10
					.getLabel(), Field.Store.YES, Field.Index.ANALYZED));
			for (String syn : icd10.getSynonyms()) {
				doc.add(new Field("synonym", syn, Field.Store.YES,
						Field.Index.ANALYZED));
			}
			writer.addDocument(doc);
			writer.commit();
		}
		writer.close();
	}
	
	public void addICD10AndATC() throws IOException{
		writer = new IndexWriter(index, config);

		ICD10Parser parser = new ICD10Parser();
		ArrayList<ICD10> parsedICDs = parser.getParsedICDs();
		for (ICD10 icd10 : parsedICDs) {
			Document doc = new Document();
			doc.add(new Field("id", icd10.getId(), Field.Store.YES,
					Field.Index.ANALYZED));
			doc.add(new Field("label", icd10.getLabel() == null ? "" : icd10
					.getLabel(), Field.Store.YES, Field.Index.ANALYZED));
			for (String syn : icd10.getSynonyms()) {
				doc.add(new Field("synonym", syn, Field.Store.YES,
						Field.Index.ANALYZED));
			}
			writer.addDocument(doc);
		}
		ATCParser parser2 = new ATCParser();
		ArrayList<ATC> parsedATCs = parser2.getParsedATCs();
		for (ATC c : parsedATCs) {
			Document doc = new Document();
			doc.add(new Field("id", c.getId(), Field.Store.YES,
					Field.Index.ANALYZED));
			doc.add(new Field("label",
					c.getLabel() == null ? "" : c.getLabel(), Field.Store.YES,
					Field.Index.ANALYZED));
			writer.addDocument(doc);
			writer.commit();
		}
		writer.close();
	}

	/**
	 * add atc to index
	 */
	public void addATC() throws IOException {
		writer = new IndexWriter(index, config);

		ATCParser parser = new ATCParser();
		ArrayList<ATC> parsedATCs = parser.getParsedATCs();
		for (ATC c : parsedATCs) {
			Document doc = new Document();
			doc.add(new Field("id", c.getId(), Field.Store.YES,
					Field.Index.ANALYZED));
			doc.add(new Field("label",
					c.getLabel() == null ? "" : c.getLabel(), Field.Store.YES,
					Field.Index.ANALYZED));
			writer.addDocument(doc);
			writer.commit();
		}
		writer.close();
	}

	/**
	 * add Legemiddelhåndboka to index
	 * 
	 * @param TherapyAndDrugChaptersOnly
	 *            true if only therapy and drug chapters should be included,
	 *            false if all chapters is to be included.
	 */
	public void addNLMH(boolean TherapyAndDrugChaptersOnly) throws IOException {
		writer = new IndexWriter(index, config);

		NLHChapterParser parser = new NLHChapterParser();
		ArrayList<NLHChapter> parsedChapters = parser.getChapters();
		int counter = 0;
		for (NLHChapter c : parsedChapters) {
			if (!TherapyAndDrugChaptersOnly || c.getId().startsWith("L")
					|| c.getId().startsWith("T")) {
				Document doc = new Document();
				doc.add(new Field("id", c.getId(), Field.Store.YES,
						Field.Index.ANALYZED));
				doc.add(new Field("title", c.getTitle() == null ? "" : c
						.getTitle(), Field.Store.YES, Field.Index.ANALYZED));
				doc.add(new Field("text", c.getText(), Field.Store.YES,
						Field.Index.ANALYZED));
				writer.addDocument(doc);
				writer.commit();
				// System.out.println(c.getText());
				counter++;
			}

		}
		System.out.println("added " + counter + " chapters!");
		writer.close();
	}

	/**
	 * Adds a result of an ICD10 search on NLH to index
	 * 
	 * @param results
	 * @throws IOException
	 */
//	public void addICD10InNLH(ArrayList<DocumentMatch> results)
//			throws IOException {
//		writer = new IndexWriter(index, config);
//		for (DocumentMatch dm : results) {
//			if(!dm.getHits().isEmpty()){
//				Document document = new Document();
//				document.add(new Field("id", dm.getID() + ":" + dm.getSentenceId(), Field.Store.YES,
//						Field.Index.ANALYZED));
//				
//				String idc10 = "";
//				for (Document d : dm.getHits()) {
//					idc10 += d.get("id") + " ";
//				}
//				document.add(new Field("label", idc10, Field.Store.YES, Field.Index.ANALYZED));
//				writer.addDocument(document);
//				writer.commit();
//			}
//		}
//		writer.close();
//	}

	public Analyzer getAnalyzer() {
		return analyzer;
	}

	public Directory getIndex() {
		return index;
	}

	// TODO: Method for adding ATC documents

}
