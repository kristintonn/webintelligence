package no.ntnu.webintelligence.icd10;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;

public class ParseICD {
	
public static void main(String[] args) {
        
        //JenaOWLModel owlModel = null;        
        FileInputStream is;
        
        try {
            is = new FileInputStream("src/main/resources/icd10no.owl");     
            OntModel onto = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, null);
            onto.read(is, "OWL/XML");
            Iterator it = onto.listClasses();
            while(it.hasNext()){
                System.out.println(it.next());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }    
}
