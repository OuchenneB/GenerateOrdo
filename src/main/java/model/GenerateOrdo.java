package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.AddOntologyAnnotation;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataHasValue;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.SetOntologyID;
import org.semanticweb.owlapi.vocab.OWL2Datatype;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class GenerateOrdo {
		
	
	
	static String Lang;
	static String Version;
	static String encodingValue				= "ISO-8859-1";
	
	static public String[][] legalTerms 	= Configuration.legalTerms;
	static String OrdoModelRoot				= Configuration.OrdoModelRoot;
	static String isoOrdoModel				= Configuration.isoOrdoModel;
	static String OutputOWLFile				= Configuration.OutputOWLFile;
	static Configuration Config;
	
	
	
	// Class needed to generate ORDO
	// creer l'objet Disorders à l'aide de la classe Disorder
	 
	public Set<Disorder> Disorders;
	public Classifications classifications;
	public Genes genes;
	public Set<Epidemiology> epidemiology;
	public Scanner fichierDef;



	


	
	
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, OWLOntologyCreationException, OWLOntologyStorageException
	
	{
	
		/*
		if(args.length<2)
		{
		System.out.println("les paramètres langue et version sont obligatoires");
		System.exit(0);
		}
		*/
		Lang="en";//args[0];
		Version="4.4";//args[1];
		Config = new Configuration(Lang,Version);	
		
	System.out.println("Process started for : " + Lang);
	
	
	
	if (Configuration.isoToTranslate.contains(Lang)) 
	encodingValue="UTF-8";
	
	GenerateOrdo ordo=new GenerateOrdo();
	
	
	
	ordo.LoadOBOFile();
	System.out.println("Load OBO file...!!!");
	
	ordo.LoadGenesFile();
	System.out.println("Load Genes file...!!!");
	
	
	
	ordo.LoadEpidemioFile();
	System.out.println("Load Epidemiological file...!!!");
	
	ordo.LoadClassificationsFiles();
	System.out.println("Load Classifications files...!!!");
	
	
	if(Configuration.isoToTranslate.contains(Lang))
		ordo.UpDate(new Trads(Lang));
	
	
	ordo.GenerateBaseOWLFile();
	
		ordo.WriteOWL();
	
	System.out.println("New OWL written...!!!");
	}
	
	private void UpDate(Trads trads) {
		
		//System.out.println(Disorders.size()+"----"+trads.getDisorders().size());
		
		for(Disorder entity:Disorders)
		{
			entity.setAlternativeTerms(new HashSet<String>());	
			entity.setSummary(".");
			
			// External references relations traductions
            for (Reference R:entity.getReferenceList())
            {    
            String manAsser="- "+trads.getRelTrad(R.getIdrel());
            if(!R.getIdicdrel().equals("."))
                manAsser=manAsser+".\n- "+trads.getRelTrad(R.getIdicdrel())+".";    
            
            R.setManualAssertion(manAsser);
            }			
            
		for(DisorderTrad Tentity:trads.getDisorders())
		if (entity.getOrphaCode().equals(Tentity.getOrphaCode()))
		{
		if(Tentity.getLabel()!=null)entity.setName(Tentity.getLabel());	
		entity.setAlternativeTerms(Tentity.getSynonyms());
		entity.setSummary(Tentity.getDefs());
		break;	
		}	
		}	
	
	}

	public void LoadEpidemioFile() throws ParserConfigurationException, SAXException, IOException {

		File inputFile = new File( Config.getepidemio_file());
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(inputFile);
		doc.getDocumentElement().normalize(); // deal with encoding strings
		NodeList disorderList = doc.getElementsByTagName("Disorder");
	    //Pour chaque élément de type Disorder Faire
		Epidemiology D = null;
		for (int index = 0; index < disorderList.getLength(); index++)
	    {
	    	Node disorder = disorderList.item(index);
	    	
	    	//System.out.println("\n Disorder Index: "+(index+1));
	       
	        if (disorder.getNodeType() == Node.ELEMENT_NODE) 
	        {
	        	Element ElementDisorder = (Element) disorder;	        	
	             
	            D=new Epidemiology(ElementDisorder);
	            //System.out.println(D);          
	           //System.out.println("\n Disorder Numero:"+D.getSummary());   
	            if(!((D.getOrphaCode().equals("2258"))||(D.getOrphaCode().equals("2989"))))//pour les deux classe offline
	    		this.epidemiology.add(D);
	        }
	          
	    }
		
	}
	
	
	
	
	public void LoadGenesFile()throws ParserConfigurationException, SAXException, IOException
	{
		this.genes.AddGenes(Config.getGenes_file());
	}
	

	public void LoadClassificationsFiles() throws ParserConfigurationException, SAXException, IOException{
		// pour chaque file classifications utilise Class Classifications et recupere les relations
		File InputFolder = new File(Config.getClassifications_directory());
		String listeFichiers[] = InputFolder.list();
		//System.out.println(listeFichiers);   
		for (int i = 0; i < listeFichiers.length; i++)if(listeFichiers[i].contains("product3")) {
			//System.out.println(listeFichiers[i]);
			this.classifications.AddClassification(Config.getClassifications_directory()+listeFichiers[i]);
		}
	}	
	
	
	
	public GenerateOrdo() {
		
		this.classifications=new Classifications();
		this.epidemiology=new
		HashSet<Epidemiology>();
		this.genes=new Genes();
		this.Disorders=new HashSet<Disorder>();


		}
	
	
	public Set<Disorder> getDisorders() {
		return Disorders;
	}

	public void setDisorders(Set<Disorder> disorders) {
			Disorders = disorders;
		}

	public void LoadOBOFile() throws ParserConfigurationException, SAXException, IOException
		{
	
	
		File inputFile = new File(Config.getObo_file());
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(inputFile);
		doc.getDocumentElement().normalize();
		NodeList disorderList = doc.getElementsByTagName("Disorder");
		//Pour chaque élément de type Disorder Faire
		for (int index = 0; index < disorderList.getLength(); index++)
			{
			Node disorder = disorderList.item(index);
			
			if (disorder.getNodeType() == Node.ELEMENT_NODE)
				{ Disorder D=new Disorder(disorder);
				
				 
				//System.out.println("OrphaCode: "+ D.getOrphaCode());
				
				
				
				this.Disorders.add(D);
				}
			}
		}
	

	
	
	
	public void GenerateBaseOWLFile() throws OWLOntologyCreationException, FileNotFoundException, OWLOntologyStorageException, UnsupportedEncodingException {
		// TODO Auto-generated method stub
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();	
		OWLOntology ordo = manager.loadOntologyFromOntologyDocument(new File(Config.getRoot_IRIS_model()));
		OWLDataFactory factory = manager.getOWLDataFactory();
        //Scanner FichierDef = new Scanner(new File(DefinitionsDirectory+Lang+"_model_def.txt"), "UTF-8");
		
        Scanner FichierDef = new Scanner(new File(Config.getDefinition_file()), encodingValue);
        	
        //System.out.println(Config.getDefinition_file());
        while(FichierDef.hasNext()){
            String Ligne = FichierDef.nextLine();
            //System.out.println(Ligne);
            String[] XLTable = Ligne.split("\t");
            String iri = XLTable[0].trim();
            String LABEL = XLTable[1].trim();
            if(Lang.equals("it"))LABEL=new String(LABEL.getBytes("ISO-8859-1"), "UTF-8");
            if(Lang.equals("pl"))LABEL = new String(LABEL.getBytes(),"UTF-8" );
            String DEFINITION = XLTable[2].trim();
            //System.out.println(DEFINITION);
            if(Lang.equals("it"))DEFINITION=new String(DEFINITION.getBytes("ISO-8859-1"), "UTF-8");
            if(Lang.equals("pl"))DEFINITION = new String(DEFINITION.getBytes(),"UTF-8" );
            if(XLTable.length!=3)
            {System.out.println("La ligne: "+Ligne+"n'est pas bien formatee\n Merci de la mettre é jour puis relance le script");System.exit(0);}
         
            
            OWLAnnotation label,definition;
            OWLAxiom labelling,addingdef;
            
            if(!iri.contains("ECO_")&&!iri.contains("BFO"))
        		 iri="http://www.orpha.net/ORDO/Orphanet_"+iri;
        	else iri="http://purl.obolibrary.org/obo/"+iri;
        	
            
            
            if(LABEL.equals("NA")){
            	System.out.println("NO LABEL FOR CONCEPT :"+iri);
            }else{
            	
            	label=factory.getOWLAnnotation(
            			factory.getRDFSLabel(), factory.getOWLLiteral(LABEL));
            	labelling= factory.getOWLAnnotationAssertionAxiom(IRI.create(iri), label);
            	manager.applyChange(new AddAxiom(ordo, labelling));
            }
            
            if(DEFINITION.equals("NA") || DEFINITION.equals("")){
            	System.out.println("NO DEFINITION FOR CONCEPT :"+iri);            	
            }else{
            	
            	definition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("http://www.ebi.ac.uk/efo/definition"),factory.getOWLLiteral(DEFINITION,Lang));
            	addingdef = factory.getOWLAnnotationAssertionAxiom(IRI.create(iri), definition);
    			manager.applyChange(new AddAxiom(ordo, addingdef));			 
                	
            	
            }
            
        
        }
        
        FichierDef.close();
	
        IRI ontologyIRI = IRI.create("https://www.orphadata.com/data/ontologies/ordo/last_version/ORDO_"+Lang+"_"+Version+".owl");
        IRI versionIRI = IRI.create("https://www.orphadata.com/data/ontologies/ordo/last_version/ORDO_"+Lang+"_"+Version+".owl");
    	OWLOntologyID newOntologyID = new OWLOntologyID(ontologyIRI,versionIRI);
    	SetOntologyID setOntologyID = new SetOntologyID(ordo, newOntologyID);
    	manager.applyChange(setOntologyID);
		
        
        
      //Save the ontology in a different file
      		File f = new File(Config.getLang_ordo_model());
      		IRI outIRI=IRI.create(f);
      		manager.saveOntology(ordo,outIRI);
      		
	
	}
	
	public void WriteOWL() throws OWLOntologyCreationException, OWLOntologyStorageException, FileNotFoundException, UnsupportedEncodingException {
		//load the base file
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();	
		OWLOntology ordo = manager.loadOntologyFromOntologyDocument(new File(Config.getLang_ordo_model()));
		
		//Declare IRI base
		String ordoIRI = "http://www.orpha.net/ORDO/Orphanet_";
	
		// Instanciate Classes
		HashSet<String> ClassGroupDisorder = new HashSet<String>();
		HashSet<String> ClassDisorder = new HashSet<String>();
		HashSet<String> ClassSubTypesDisorder = new HashSet<String>();
		
		// Category
		ClassGroupDisorder.add("36561");
		// Clinical group
		ClassGroupDisorder.add("21436");
		
		// Disease
		ClassDisorder.add("21394");
		// Biological anomaly
		ClassDisorder.add("21408");
		// Clinical syndrome
		ClassDisorder.add("21422");
		// Malformation syndrome
		ClassDisorder.add("21401");
		// Morphological anomaly
		ClassDisorder.add("21415");
		// Particular clinical situation in a disease or syndrome
		ClassDisorder.add("21429");
		
		// Clinical subtype
		ClassSubTypesDisorder.add("21450");
		// Etiological subtype
		ClassSubTypesDisorder.add("21443");
		// Histopathological subtype
		ClassSubTypesDisorder.add("21457");


		// charge le model de base
		OWLDataFactory factory = manager.getOWLDataFactory();
		
		// Pour chaque entité
		for(Disorder entity:Disorders)
			//Elimine les off-line
		if(!entity.getFlags().contains("490"))
		
		{
			
			// Crée l'IRI de l'entité http://www.orpha.net/ORDO/Orphanet_ + OrphaCode
			IRI iricls = IRI.create(ordoIRI+entity.getOrphaCode());
			OWLClass clsentity = factory.getOWLClass(iricls);
			
			// creer une classe pour chaque entité avec pour nom son label 
			OWLAnnotation label=factory.getOWLAnnotation(
			factory.getRDFSLabel(), factory.getOWLLiteral(entity.getName()));
			OWLDeclarationAxiom declarationClass = factory.getOWLDeclarationAxiom(clsentity);
			OWLAxiom labelling = factory.getOWLAnnotationAssertionAxiom(clsentity.getIRI(), label);
			manager.applyChange(new AddAxiom(ordo, labelling));
			manager.applyChange(new AddAxiom(ordo, declarationClass));
			//System.out.println(labelling);
			
			
			//System.out.println(disease.getName());
				
			// Triage of Obsolete entities
			if(		//Obsolete entity
					entity.getFlags().contains("455") 
					// Obsolete with resources
					||entity.getFlags().contains("461")
					// Deprecated entity
					||entity.getFlags().contains("459")
					// Non-rare disease in Europe
					||entity.getFlags().contains("456")
					)
				//si la classe est obsolete ou deprecated
			{
			
				
				//System.out.println("++"+entity.getObs().getTxtInfos());
				if(!entity.getObs().getTxtInfos().equals("."))
				{	
				OWLAnnotation reason_for_obsolescence= factory.getOWLAnnotation(factory.getOWLAnnotationProperty("http://www.ebi.ac.uk/efo/reason_for_obsolescence"),
						factory.getOWLLiteral(entity.getObs().getTxtInfos()));
				OWLAxiom addingReason_for_obsolescence = factory.getOWLAnnotationAssertionAxiom(clsentity.getIRI(), reason_for_obsolescence);
				manager.applyChange(new AddAxiom(ordo, addingReason_for_obsolescence));
				}
				
				if (entity.getFlags().contains("459")&&!entity.getObs().getOrphaCode().equals("."))
				{
					OWLObjectProperty moved_to= factory.getOWLObjectProperty("http://www.orpha.net/ORDO/Orphanet_C056");
					OWLClassExpression moved_toClass = factory.getOWLObjectSomeValuesFrom(moved_to, factory.getOWLClass("http://www.orpha.net/ORDO/Orphanet_"+entity.getObs().getOrphaCode()));
					OWLSubClassOfAxiom ax = factory.getOWLSubClassOfAxiom(clsentity, moved_toClass);
					manager.applyChange(new AddAxiom(ordo, ax));
				}	
				
				
				if ((entity.getFlags().contains("461")||entity.getFlags().contains("455"))&&!entity.getObs().getOrphaCode().equals("."))
				{
					OWLObjectProperty referred_to= factory.getOWLObjectProperty("http://www.orpha.net/ORDO/Orphanet_C057");
					OWLClassExpression referred_toClass = factory.getOWLObjectSomeValuesFrom(referred_to, factory.getOWLClass("http://www.orpha.net/ORDO/Orphanet_"+entity.getObs().getOrphaCode()));
					OWLSubClassOfAxiom ax = factory.getOWLSubClassOfAxiom(clsentity, referred_toClass);
					manager.applyChange(new AddAxiom(ordo, ax));
					
				}	
				
				

				   if(ClassGroupDisorder.contains(entity.getDisorderType().getId())&&entity.getFlags().contains("455"))
					   manager.applyChange(new AddAxiom(ordo,factory.getOWLSubClassOfAxiom(clsentity,factory.getOWLClass("http://www.orpha.net/ORDO/Orphanet_C051") )));
				   if(ClassDisorder.contains(entity.getDisorderType().getId())&&entity.getFlags().contains("455"))
					   manager.applyChange(new AddAxiom(ordo,factory.getOWLSubClassOfAxiom(clsentity,factory.getOWLClass("http://www.orpha.net/ORDO/Orphanet_C052") )));
				   if(ClassSubTypesDisorder.contains(entity.getDisorderType().getId())&&entity.getFlags().contains("455"))
					   manager.applyChange(new AddAxiom(ordo,factory.getOWLSubClassOfAxiom(clsentity,factory.getOWLClass("http://www.orpha.net/ORDO/Orphanet_C053") )));
				   
				   
				   if(ClassGroupDisorder.contains(entity.getDisorderType().getId())&&entity.getFlags().contains("461"))
					   manager.applyChange(new AddAxiom(ordo,factory.getOWLSubClassOfAxiom(clsentity,factory.getOWLClass("http://www.orpha.net/ORDO/Orphanet_C051") )));
				   if(ClassDisorder.contains(entity.getDisorderType().getId())&&entity.getFlags().contains("461"))
					   manager.applyChange(new AddAxiom(ordo,factory.getOWLSubClassOfAxiom(clsentity,factory.getOWLClass("http://www.orpha.net/ORDO/Orphanet_C052") )));
				   if(ClassSubTypesDisorder.contains(entity.getDisorderType().getId())&&entity.getFlags().contains("461"))
					   manager.applyChange(new AddAxiom(ordo,factory.getOWLSubClassOfAxiom(clsentity,factory.getOWLClass("http://www.orpha.net/ORDO/Orphanet_C053") )));
				   
				  
				   if(ClassGroupDisorder.contains(entity.getDisorderType().getId())&&entity.getFlags().contains("459"))
					   manager.applyChange(new AddAxiom(ordo,factory.getOWLSubClassOfAxiom(clsentity,factory.getOWLClass("http://www.orpha.net/ORDO/Orphanet_C043") )));
				   if(ClassDisorder.contains(entity.getDisorderType().getId())&&entity.getFlags().contains("459"))
					   manager.applyChange(new AddAxiom(ordo,factory.getOWLSubClassOfAxiom(clsentity,factory.getOWLClass("http://www.orpha.net/ORDO/Orphanet_C044") )));
				   if(ClassSubTypesDisorder.contains(entity.getDisorderType().getId())&&entity.getFlags().contains("459"))
					   manager.applyChange(new AddAxiom(ordo,factory.getOWLSubClassOfAxiom(clsentity,factory.getOWLClass("http://www.orpha.net/ORDO/Orphanet_C045") )));
					
				   
				   if(ClassGroupDisorder.contains(entity.getDisorderType().getId())&&entity.getFlags().contains("456"))
					   manager.applyChange(new AddAxiom(ordo,factory.getOWLSubClassOfAxiom(clsentity,factory.getOWLClass("http://www.orpha.net/ORDO/Orphanet_C047") )));
				   if(ClassDisorder.contains(entity.getDisorderType().getId())&&entity.getFlags().contains("456"))
					   manager.applyChange(new AddAxiom(ordo,factory.getOWLSubClassOfAxiom(clsentity,factory.getOWLClass("http://www.orpha.net/ORDO/Orphanet_C048") )));
				   if(ClassSubTypesDisorder.contains(entity.getDisorderType().getId())&&entity.getFlags().contains("456"))					   
					   manager.applyChange(new AddAxiom(ordo,factory.getOWLSubClassOfAxiom(clsentity,factory.getOWLClass("http://www.orpha.net/ORDO/Orphanet_C049") )));
					
				   
				   /* no type
				   if(entity.getDisorderType().getId().equals("21464"))
					   manager.applyChange(new AddAxiom(ordo,factory.getOWLSubClassOfAxiom(clsentity,factory.getOWLClass("http://www.orpha.net/ORDO/Orphanet_C041") )));
				    */
				   
				//System.out.println(disease.getDisorderType().getName());
				// ajout de l'assertion que cette entity est subclass of Obsolete class
				   
				
			}	
			
			else
			{
			
												// IRI + OrphaNum Type
			OWLClass clstype=factory.getOWLClass(ordoIRI+entity.getDisorderType().getOrphaNumber());
			// label entity utilisé pour créer le label de la class
			label=factory.getOWLAnnotation(
					factory.getRDFSLabel(), factory.getOWLLiteral(entity.getDisorderType().getName(),Lang));
			
			labelling = factory.getOWLAnnotationAssertionAxiom(clstype.getIRI(), label);
			
			
			// si entity Type fait partie des Type de la classe ClassDisorder
			if(ClassDisorder.contains(entity.getDisorderType().getId()))
			{	
				// creer une classe avec le label du type de l'entity i.e. "malformation"
			manager.applyChange(new AddAxiom(ordo, labelling));
				// cette entity (qui est une classe) est sous-classe de la class du type enity i.e. "malformation"
			manager.applyChange(new AddAxiom(ordo,factory.getOWLSubClassOfAxiom(clsentity, clstype)));
			}
			
			
			
			if(ClassDisorder.contains(entity.getDisorderType().getId()))
			{
					//Disorder
				clstype=factory.getOWLClass(ordoIRI+"557493");
				manager.applyChange(new AddAxiom(ordo,factory.getOWLSubClassOfAxiom(clsentity, clstype)));

			}		
			
			
			
			if(ClassGroupDisorder.contains(entity.getDisorderType().getId()))
				
			{
				//group of disorders
			clstype=factory.getOWLClass(ordoIRI+"557492");
			manager.applyChange(new AddAxiom(ordo,factory.getOWLSubClassOfAxiom(clsentity, clstype)));	
			OWLLiteral value=factory.getOWLLiteral(entity.getDisorderType().getName(),Lang);
			OWLAnnotation skosnotaion = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("http://www.w3.org/2004/02/skos/core#notation"),value);
			OWLAxiom addingSkosNotation = factory.getOWLAnnotationAssertionAxiom(clsentity.getIRI(), skosnotaion);
			manager.applyChange(new AddAxiom(ordo, addingSkosNotation));
			
			
			}
		    	
			//ClassSubTypesDisorder
			if(ClassSubTypesDisorder.contains(entity.getDisorderType().getId()))
			{
			clstype=factory.getOWLClass(ordoIRI+"557494");
			manager.applyChange(new AddAxiom(ordo,factory.getOWLSubClassOfAxiom(clsentity, clstype)));	
			OWLLiteral value=factory.getOWLLiteral(entity.getDisorderType().getName(),Lang);
			OWLAnnotation skosnotaion = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("http://www.w3.org/2004/02/skos/core#notation"),value);
			OWLAxiom addingSkosNotation = factory.getOWLAnnotationAssertionAxiom(clsentity.getIRI(), skosnotaion);
			manager.applyChange(new AddAxiom(ordo, addingSkosNotation));
			
			}	
			
			
			}
			
			
			// add foafpage ExpertLink Orphanet
			OWLAnnotation foafpage = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("http://www.orpha.net/ORDO/Orphanet_C055"),factory.getOWLLiteral("https://www.orpha.net/consor/cgi-bin/OC_Exp.php?lng="+Lang+"&Expert="+entity.getOrphaCode()));
			OWLAxiom addingFoaf = factory.getOWLAnnotationAssertionAxiom(clsentity.getIRI(), foafpage);
			manager.applyChange(new AddAxiom(ordo, addingFoaf));
					
			
			OWLDatatype stringDatatype = factory.getStringOWLDatatype(); 
		    //OWLLiteral age1 = factory.getOWLLiteral("1", intDatatype); https://www.editions-eni.fr/open/mediabook.aspx?idR=2be6ffff04f1713ac2d4e57557eea949
			
			// add ORPHA skos notation
			OWLLiteral value=factory.getOWLLiteral("ORPHA:"+entity.getOrphaCode(),stringDatatype);
			OWLAnnotation skosnotation = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("http://www.w3.org/2004/02/skos/core#notation"),value);
			OWLAxiom addingSkosNotation = factory.getOWLAnnotationAssertionAxiom(clsentity.getIRI(), skosnotation);
			manager.applyChange(new AddAxiom(ordo, addingSkosNotation));
			
			
			// add skos notation headOfClassification
			if(entity.getFlags().contains("458"))//si headOfClassification
			{
			OWLAnnotation headofclassif = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("http://www.w3.org/2004/02/skos/core#notation"),factory.getOWLLiteral("Head of classification",Lang));
			OWLAxiom addingHeadofNotation = factory.getOWLAnnotationAssertionAxiom(clsentity.getIRI(), headofclassif);
			manager.applyChange(new AddAxiom(ordo, addingHeadofNotation));
			}
			
			//add skos notation synonyms 
			for (String terms : entity.getAlternativeTerms())
		  	{
		  		OWLAnnotation alterms = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("http://www.ebi.ac.uk/efo/alternative_term"),factory.getOWLLiteral(terms,Lang));
				OWLAxiom addingAlterms = factory.getOWLAnnotationAssertionAxiom(clsentity.getIRI(), alterms);
				manager.applyChange(new AddAxiom(ordo, addingAlterms));	
		  	}
			
			// add skos notation definitions
			if (!entity.getSummary().equals("."))
		  	{
		  		OWLAnnotation definition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("http://www.ebi.ac.uk/efo/definition"),factory.getOWLLiteral(entity.getSummary(),Lang));
				OWLAxiom addingdef = factory.getOWLAnnotationAssertionAxiom(clsentity.getIRI(), definition);
				manager.applyChange(new AddAxiom(ordo, addingdef));	
				
				definition = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("http://www.ebi.ac.uk/efo/definition_citation"),factory.getOWLLiteral("Orphanet",Lang));     	
				addingdef = factory.getOWLAnnotationAssertionAxiom(clsentity.getIRI(), definition);
				manager.applyChange(new AddAxiom(ordo, addingdef));
		  	}
			
			// add references ext OMIM, ICD, UMLS
			for (Reference Ref : entity.getReferenceList())
		  	{
		  		
				
			 //PrefixManager oboInOwl = new DefaultPrefixManager("");
						//PrefixManager obo  = new DefaultPrefixManager("");
						
						OWLAnnotation database_cross_reference = factory.getOWLAnnotation(factory.getOWLAnnotationProperty(
										"http://www.geneontology.org/formats/oboInOwl#hasDbXref"), factory.getOWLLiteral(Ref.getSource() + ":" + Ref.getReference()));
						OWLAxiom xref = factory.getOWLAnnotationAssertionAxiom(clsentity.getIRI(), database_cross_reference);
						manager.applyChange(new AddAxiom(ordo, xref));
						//UPDATE SD mapping relation
						Set<OWLAnnotation> ref = new HashSet<OWLAnnotation>();
						ref.add(database_cross_reference);				
							//faire la séparation dans le cas de :::
							OWLAnnotation curationAssertion = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("http://purl.obolibrary.org/obo/ECO_0000218"),factory.getOWLLiteral(Ref.getManualAssertion(),Lang));
							Set<OWLAnnotation> owlAnnoCur = new HashSet<OWLAnnotation>();

							owlAnnoCur.add(curationAssertion);						
							OWLAxiom xref2 = factory.getOWLAnnotationAssertionAxiom(clsentity.getIRI(), database_cross_reference,owlAnnoCur);
							manager.applyChange(new AddAxiom(ordo, xref2));
				
					
		  	}
			

	

		}	

		//Ajout des classifications dans OWL
		
		// recuperation de IRI Partof dans le model 
		//OWLObjectProperty partOf = factory.getOWLObjectProperty("http://purl.obolibrary.org/obo/BFO_0000050");

		for(Relation r:this.classifications.getRelations())
		{

		OWLClass ParentClass=factory.getOWLClass(ordoIRI+r.getParent());
		OWLClass ChildClass=factory.getOWLClass(ordoIRI+r.getChild());




		if (r.getRelation()==0)//PartOf 
		{

			OWLObjectProperty partOf = factory.getOWLObjectProperty("http://purl.obolibrary.org/obo/BFO_0000050");

			OWLClassExpression partOfSuperClass = factory.getOWLObjectSomeValuesFrom(partOf, ParentClass);
			OWLSubClassOfAxiom ax = factory.getOWLSubClassOfAxiom(ChildClass, partOfSuperClass);
			manager.applyChange(new AddAxiom(ordo, ax));
		
		}
		else //Sous Classe
		{

		manager.applyChange(new AddAxiom(ordo,factory.getOWLSubClassOfAxiom(ChildClass, ParentClass)));

		}

		}
		
		
		//Ajout des informations Genes
		OWLClass geneticMaterial = factory.getOWLClass(ordoIRI+"C010");	
			
			for(Gene G:this.getGenes().getGenes())
				// parcours des genes et ajout de relation Gene-Disorder
			 {	
				// creation de classe en fonction de l'orphanum du gene et de son label
				OWLClass gene = factory.getOWLClass(ordoIRI+G.getOrphaNumber());
				OWLAnnotation geneLab = factory.getOWLAnnotation(factory.getRDFSLabel(), factory.getOWLLiteral(G.getName()));
				OWLDeclarationAxiom geneClass = factory.getOWLDeclarationAxiom(gene);
				manager.applyChange(new AddAxiom(ordo, geneClass));
				manager.applyChange(new AddAxiom(ordo, factory.getOWLAnnotationAssertionAxiom(gene.getIRI(), geneLab)));

						
				OWLClass superClass = factory.getOWLClass(ordoIRI+G.geneType.getOrphaNumber());
				
				//Ajout de sous classes
				//Classement des classes de genes en sous-classe de type et sous classe de Genetic Material
				manager.applyChange(new AddAxiom(ordo, factory.getOWLSubClassOfAxiom(gene, superClass)));
				manager.applyChange(new AddAxiom(ordo, factory.getOWLSubClassOfAxiom(superClass,geneticMaterial)));
				
				//Symbol du gene
				OWLAnnotation symbol = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("http://www.orpha.net/ORDO/symbol"),factory.getOWLLiteral(G.getSymbol()));
				OWLAxiom symb = factory.getOWLAnnotationAssertionAxiom(gene.getIRI(), symbol);
				manager.applyChange(new AddAxiom(ordo, symb));	
				
				
			 	//Synonymes	
				for (String terms : G.getSynonymList())
			  	{
			  		OWLAnnotation synonym = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("http://www.ebi.ac.uk/efo/alternative_term"),factory.getOWLLiteral(terms,Lang));
					OWLAxiom addingAlterms = factory.getOWLAnnotationAssertionAxiom(gene.getIRI(), synonym);
					manager.applyChange(new AddAxiom(ordo, addingAlterms));	
			  	}
				
				
				//dbxref OMIM, GeneAtlas, Ensembl etc...	 
				for (Reference Ref : G.getReferenceList())
			  	{				
							OWLAnnotation dbxref = factory.getOWLAnnotation(factory.getOWLAnnotationProperty(
											"http://www.geneontology.org/formats/oboInOwl#hasDbXref"), factory.getOWLLiteral(Ref.getSource() + ":" + Ref.getReference()));
							OWLAxiom xref = factory.getOWLAnnotationAssertionAxiom(gene.getIRI(), dbxref);
							manager.applyChange(new AddAxiom(ordo, xref));
				}
				
				
				
				//locus pour le gene
				OWLDataProperty has_Locus = factory.getOWLDataProperty(ordoIRI+"C040");
				
				
				for (Locus L : G.getLocusList())
			  	{
					OWLDataHasValue hasLocus = factory.getOWLDataHasValue(has_Locus, factory.getOWLLiteral(L.getLocus()));
					OWLSubClassOfAxiom locusax1 = factory.getOWLSubClassOfAxiom(gene,hasLocus);
					manager.applyChange(new AddAxiom(ordo, locusax1));
				}
			 			 
			 }
		
			//Lien Genes Maladies (voir getGenes plus bas)
			for(DisorderGene DG:this.getGenes().getRelationDisorderGene()) {
			OWLClass DiseaseClass = factory.getOWLClass(ordoIRI+DG.getOrphaCode());
				
				for(Association AS:DG.getAssociations()) 
				{	
			   //ajout  gene type object property avec le label pour le typing i.e :Disease-causing germline mutation(s) in
			 
					OWLObjectProperty has_typing = factory.getOWLObjectProperty(ordoIRI+AS.getType().getOrphaNumber());
			        OWLClass GeneClass = factory.getOWLClass(ordoIRI+Gene(AS.getId()));
			        //System.out.println(has_typing);System.exit(0);

			        OWLClassExpression hasTypingClass = factory.getOWLObjectSomeValuesFrom(has_typing, DiseaseClass);
		          	OWLSubClassOfAxiom ax1 = factory.getOWLSubClassOfAxiom(GeneClass,hasTypingClass);	
			        manager.applyChange(new AddAxiom(ordo, ax1));
			
				}
			}
			
			
			//Ajout des informations Epidemio

			for(Epidemiology E:this.getEpidemiology())
			 {	
				OWLClass clsentity = factory.getOWLClass(ordoIRI+E.getOrphaCode());
		        for(Prevalence P:E.getPrevalences())
		        {
		        	//Set<OWLClassExpression> intersec = new HashSet<OWLClassExpression> ();	
		        	
		        	String Prev=".";
		        	
		        	// Nettoyage si prevalence nulle ou Not yet documented
		        	if((P.getPrevalenceClass()!=null)
		        			&&(!P.getPrevalenceClass().getId().equals("23781")) // Not yet documented
		        			)
		        		
		        		
		        	{		        		
		        		String type=P.getPrevalenceType().getOrphaNumber();
						// SELECTION  du typage de classe 
		        		
						if (type.equals("409966") )
						{ type = "C025";      // prevalence point
						}else if (type.equals("409968") )
						{type = "C026"; // prevalence at birth
						}
						else if (type.equals("409969") )
						{type = "C027"; // lifetime prevalence		
						}else if (type.equals("409967") )
						{type = "C020";// "C020"; // annual incidence
						}else if 
						(type.equals("409970") )
						{type = "C024"; // cases ET families
						}
		        		
						
						Prev="AND "+getLabel(type)+ " : " +P.getPrevalenceClass().getName();
						
						
		        	}//end if class non null
		        		
		        		
		        		String type=P.getPrevalenceType().getOrphaNumber();
		        		if (type.equals("409966") ){ type = "C028";      // prevalence point
						}else if (type.equals("409968") ){type = "C029"; // prevalence at birth
						}else if (type.equals("409969") ){type = "C030"; // lifetime prevalence				
						}else if (type.equals("409967") ){type = "C032"; // annual incidence
						}else if (type.equals("409970") ){type = "C024"; // Cases et Family
						}else{
							System.out.println("Type non-reconnu: "+type);
						}
		        	
	            		OWLAnnotationProperty has_PrevType = factory.getOWLAnnotationProperty(ordoIRI+"C022");
		        		
		        		String ValPrev=P.getPrevalenceGeographic().getName();
		        		
		        		if (P.getValMoy()!=0)
		        			
		        		if(P.getQualification().getOrphaNumber().equals("409973")||P.getQualification().getOrphaNumber().equals("409974"))
		        			ValPrev=ValPrev+" AND "+getLabel(type)+" : "+P.getValMoy()+" ("+P.getQualification().getName()+")";
		        		else ValPrev=ValPrev+" AND "+getLabel(type)+" : "+P.getValMoy();
		        		
		        		
		        		
		        		
		        		
		        		if (!Prev.equals("."))ValPrev=ValPrev+" "+Prev;
		        		OWLAnnotation hasPrevType = factory.getOWLAnnotation(has_PrevType ,factory.getOWLLiteral(ValPrev));

		        		
		        		OWLAxiom addinghasPrevType = factory.getOWLAnnotationAssertionAxiom(clsentity.getIRI(), hasPrevType);
						manager.applyChange(new AddAxiom(ordo, addinghasPrevType));
					
						Prev=".";
						
					OWLAnnotationProperty has_ageOfOnset = factory.getOWLAnnotationProperty(ordoIRI+"C017");
					OWLAnnotationProperty has_inheritance = factory.getOWLAnnotationProperty(ordoIRI+"C016");
					if (!Configuration.isoToTranslate.contains(Lang))
					{
						
		        	for(Type AgeOfOnset:E.getAverageAgeOfOnsetList() )
				 {
				    
				   	OWLAnnotation hasAgeOfOnset = factory.getOWLAnnotation(has_ageOfOnset , factory.getOWLLiteral(AgeOfOnset.getName()));
					OWLAxiom addingAgeOfOnset = factory.getOWLAnnotationAssertionAxiom(clsentity.getIRI(), hasAgeOfOnset);
					manager.applyChange(new AddAxiom(ordo, addingAgeOfOnset));
				 }
		        	for(Type TypeOfInheritance:E.getTypeOfInheritanceList() )
				 {
					
					OWLAnnotation hasheritVal = factory.getOWLAnnotation(has_inheritance , factory.getOWLLiteral(TypeOfInheritance.getName()));
					OWLAxiom addingTypeOfInheritance = factory.getOWLAnnotationAssertionAxiom(clsentity.getIRI(), hasheritVal);
					manager.applyChange(new AddAxiom(ordo, addingTypeOfInheritance));
				 }
					}
					else {
				       	for(Type AgeOfOnset:E.getAverageAgeOfOnsetList() )
						 {
				       		OWLAnnotation hasAgeOfOnset = factory.getOWLAnnotation(has_ageOfOnset , factory.getOWLLiteral(getLabel(AgeOfOnset.getOrphaNumber())));
							OWLAxiom addingAgeOfOnset = factory.getOWLAnnotationAssertionAxiom(clsentity.getIRI(), hasAgeOfOnset);
							manager.applyChange(new AddAxiom(ordo, addingAgeOfOnset));
						 }
				        	for(Type TypeOfInheritance:E.getTypeOfInheritanceList() )
						 {
							OWLAnnotation hasheritVal = factory.getOWLAnnotation(has_inheritance , factory.getOWLLiteral(getLabel(TypeOfInheritance.getOrphaNumber())));
							OWLAxiom addingTypeOfInheritance = factory.getOWLAnnotationAssertionAxiom(clsentity.getIRI(), hasheritVal);
							manager.applyChange(new AddAxiom(ordo, addingTypeOfInheritance));
						 }
						
					}
			 }
	}
 

		///Ajout des annotaions version,date, auteurs, etc.
	

	    	// propriété Version 
	    	
			OWLAnnotation version = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("http://www.w3.org/2002/07/owl#versionInfo"),
					factory.getOWLLiteral(Version));
			manager.applyChange(new AddOntologyAnnotation(ordo, version));
		         
			
	    	
	     // propriétés Creators
	    	
	    	for (String creat: Configuration.creators){
				OWLAnnotation creator = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("http://purl.org/dc/elements/1.1/creator"),
						factory.getOWLLiteral(creat));
				manager.applyChange(new AddOntologyAnnotation(ordo, creator));
	    	}
          			
	    	//Ajout de propriétés date de création et de modification
	    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	    	Date date = new Date();
	    	
	    	OWLAnnotation modificationDate = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("http://purl.org/dc/terms/modified"),
	    	factory.getOWLLiteral(dateFormat.format(date), OWL2Datatype.XSD_DATE_TIME));
	    	manager.applyChange(new AddOntologyAnnotation(ordo, modificationDate));
           
			OWLAnnotation creationDate      = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("http://purl.org/dc/terms/created"),
					factory.getOWLLiteral(Configuration.CreationDate, OWL2Datatype.XSD_DATE_TIME));
			
			manager.applyChange(new AddOntologyAnnotation(ordo, creationDate));
			
			
			//Propriété Licence 
			IRI licenseIRI = IRI.create("https://creativecommons.org/licenses/by/4.0/");
			OWLAnnotation licence      = factory.getOWLAnnotation(factory.getOWLAnnotationProperty("http://purl.org/dc/terms/license"),licenseIRI);
			manager.applyChange(new AddOntologyAnnotation(ordo, licence));
			OWLClass licenceClass = factory.getOWLClass("https://creativecommons.org/licenses/by/4.0/");
			manager.applyChange(new AddOntologyAnnotation(ordo, licence));
			OWLDeclarationAxiom licenceDec = factory.getOWLDeclarationAxiom(licenceClass);
	    	manager.applyChange(new AddAxiom(ordo, licenceDec));
	    	//Ajout du label de la classe Licence
	    	
	    	OWLAnnotation licenceLabel = factory.getOWLAnnotation(factory.getRDFSLabel(),factory.getOWLLiteral("CC BY 4.0"));
	    	manager.applyChange(new AddAxiom(ordo, factory.getOWLAnnotationAssertionAxiom(licenceClass.getIRI(), licenceLabel)));
			
			for(int annotIndex = 0;annotIndex < legalTerms.length ;annotIndex++)
			{
				OWLAnnotation legalTerm = factory.getOWLAnnotation(
						factory.getOWLAnnotationProperty("https://creativecommons.org/licenses/"+legalTerms[annotIndex][0]), IRI.create(legalTerms[annotIndex][1]));
				
				manager.applyChange(new AddAxiom(ordo, factory.getOWLAnnotationAssertionAxiom(licenceClass.getIRI(), legalTerm)));
			}
			
			//Ajout de l'IRI de l'ontologie avec la version
		
	IRI ontologyIRI = IRI.create("https://www.orphadata.com/data/ontologies/ordo/last_version/ORDO_"+Lang+"_"+Version+".owl");
	        IRI versionIRI = IRI.create("https://www.orphadata.com/data/ontologies/ordo/last_version/ORDO_"+Lang+"_"+Version+".owl");
	    	OWLOntologyID newOntologyID = new OWLOntologyID(ontologyIRI,versionIRI);
	    	SetOntologyID setOntologyID = new SetOntologyID(ordo, newOntologyID);
	    	manager.applyChange(setOntologyID);
			
			
			
			
			
			
		//Save the ontology in a different file
		File f = new File(Config.getOrdo_ontology());
		IRI outIRI=IRI.create(f);
		manager.saveOntology(ordo,outIRI);
		}
			
	
	
	
	
	private String getLabel(String type) throws FileNotFoundException, UnsupportedEncodingException {
		fichierDef = new Scanner(new File(Config.getDefinition_file()), encodingValue);
		//System.out.println(Lang+"_model_def.txt");
        while(fichierDef.hasNext()){
            String Ligne = fichierDef.nextLine();
           // System.out.println(Ligne);
            String[] XLTable = Ligne.split("\t");
            String iri = XLTable[0].trim();
            String LABEL = XLTable[1].trim();
            if(Lang.equals("it"))LABEL=new String(LABEL.getBytes("ISO-8859-1"), "UTF-8");
            if(Lang.equals("pl"))LABEL = new String(LABEL.getBytes(),"UTF-8" );
            String DEFINITION = XLTable[2].trim();
            if(Lang.equals("it"))DEFINITION=new String(DEFINITION.getBytes("ISO-8859-1"), "UTF-8");
            if(Lang.equals("pl"))DEFINITION = new String(DEFINITION.getBytes(),"UTF-8" );
            if(iri.equals(type)) return LABEL;
        
        }
        System.out.println("Fatal Error..!!! No Label for IRI"+type);
        System.exit(0);
        return type;
	}

	public Set<Epidemiology> getEpidemiology() {
		return epidemiology;
	}

	public void setEpidemiology(Set<Epidemiology> epidemiology) {
		this.epidemiology = epidemiology;
	}

	public String Gene(String id) {
		// recupere les informations du gene avec son Id
		for(Gene G:this.getGenes().getGenes())
		 {
			if (G.getId().equals(id))return G.getOrphaNumber();
		 }	
		
		System.out.println("Probleme gene Not found..!!!");
		System.exit(0);
		return null;
	}

	
	public Genes getGenes() {
		return genes;
	}

	public void setGenes(Genes genes) {
		this.genes = genes;
	}
	
	
	
}