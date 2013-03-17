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
import java.util.ArrayList;
import no.ntnu.webintelligence.models.ICD10;

public class ICD10Parser {

    private ArrayList<ICD10> parsedICDs;

    public ICD10Parser() {
        parsedICDs = new ArrayList<ICD10>();
        
        FileInputStream is;
        
        try {
            is = new FileInputStream("src/main/resources/icd10no.owl");
            
            OntModel onto = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, null);
            onto.read(is, "RDF/XML");

            StmtIterator stit = onto.listStatements();
            ICD10 icd10 = new ICD10(null, null, null);
            while (stit.hasNext()) {

                Statement stmt = stit.nextStatement();  // get next statement
                Resource subject = stmt.getSubject();     // get the subject
                Property predicate = stmt.getPredicate();   // get the predicate
                RDFNode object = stmt.getObject();      // get the object

                // Create new object
                if (icd10.getId() == null) {
                    icd10 = new ICD10(subject.getLocalName(), null, null);
                }
                
                // Add object to list when no more statements
                if (!icd10.getId().equals(subject.getLocalName())){
                    parsedICDs.add(icd10);
                    icd10 = new ICD10(null, null, null);
                }
                
                // Add label and synonyms
                if (predicate.getLocalName().equals("label") || predicate.getLocalName().equals("synonym")) {
                    String value = object.toString();
                    int i = value.indexOf("http");
                    value = value.substring(0, i - 2);
                   
                    if (predicate.getLocalName().equals("label")){
                        icd10.setLabel(value);
                    }
                    else if (predicate.getLocalName().equals("synonym")){
                        icd10.addSynonym(value);
                    }
                }           
            }
//            Iterator it = onto.listAllOntProperties();
//            while (it.hasNext()) {
//                Property p = (Property) it.next();
//                properties.add(p);
//            }
//            System.out.println(properties);
//            Iterator it2 = onto.listNamedClasses();
//            while (it2.hasNext()) {
//                OntClass ontClass = (OntClass) it2.next();
//                String id = ontClass.getLocalName();
//                System.out.println(id);
//                String label = ontClass.getLabel(null);
//                System.out.println(label);
//
//                System.out.println(ontClass.getCardinality(null));
//                System.out.println(ontClass.getClass());
//                System.out.println(ontClass.getComment(null));
//                System.out.println(ontClass.getNameSpace());
//                System.out.println(ontClass.getSubClass());
//                System.out.println(ontClass.getSuperClass());
//                System.out.println(ontClass.getRDFType());
//                for (int i = 0; i<properties.size(); i++){
//                    Property p = properties.get(i);
//                    System.out.println(p.getLocalName());
//                    RDFNode r = ontClass.getPropertyValue(p);
//                    if (r != null){
//                        System.out.println(r.toString());
//                    }
//                    
//                    
//                }
//                ICD10 icd = new ICD10(id, label, null, null);
//                Iterator it3 = ontClass.listDeclaredProperties();
//                while (it3.hasNext()) {
//                    OntProperty p2 = (OntProperty) it3.next();
//                    if (ontClass.hasProperty(p2)) {
//                        System.out.println(p2.getLocalName());
//                        RDFNode r = p2.getPropertyValue(p2);
//                        System.out.println(r);
//                    }
//
//                }
//            }
//            System.out.println(onto.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < parsedICDs.size(); i++){
            System.out.println(parsedICDs.get(i));
        }
    }

    public ArrayList<ICD10> getParsedICDs() {
        return parsedICDs;
    }
    
    public static void main(String[] args) {
        ICD10Parser parseICD = new ICD10Parser();
    }
}
