/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.ntnu.webintelligence.parsers;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import no.ntnu.webintelligence.models.ATC;

/**
 *
 * @author Kristin
 */
public class ATCParser {
    private ArrayList<ATC> parsedATCs;
    
    public ATCParser() throws FileNotFoundException{
        parsedATCs = new ArrayList<ATC>();
        FileInputStream is;
        
            is = new FileInputStream("src/main/resources/atc.owl");
            
            OntModel onto = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, null);
            onto.read(is, "RDF/XML");

            StmtIterator stit = onto.listStatements();
            ATC atc = new ATC(null, null);
            
            while(stit.hasNext()){
                Statement stmt = stit.nextStatement();  // get next statement
                Resource subject = stmt.getSubject();     // get the subject
                Property predicate = stmt.getPredicate();   // get the predicate
                RDFNode object = stmt.getObject();      // get the object
                
                System.out.println(subject + "-" + predicate + "-" + object);
            }
    }
    
    public ArrayList<ATC> getParsedATCs(){
        return parsedATCs;
    }
    
    public static void main(String[] args) throws FileNotFoundException{
        ATCParser parser = new ATCParser();
    }
}
