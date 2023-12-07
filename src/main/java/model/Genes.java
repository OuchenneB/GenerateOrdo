package model;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Genes {
	Set<Gene> genes;
	Set<DisorderGene> relationDisorderGene;
	public Set<Gene> getGenes() {
		return genes;
	}

	public void setGenes(Set<Gene> genes) {
		this.genes = genes;
	}

	
	public Genes() {
		
		this.genes = new HashSet<Gene>();
		this.relationDisorderGene = new HashSet<DisorderGene>();
	}



	public Set<DisorderGene> getRelationDisorderGene() {
		return relationDisorderGene;
	}

	public void setRelationDisorderGene(Set<DisorderGene> relationDisorderGene) {
		this.relationDisorderGene = relationDisorderGene;
	}

	public  void AddGenes(String GenesFile) throws ParserConfigurationException, SAXException, IOException {
				
		File inputFile = new File(GenesFile);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(inputFile);
		doc.getDocumentElement().normalize();
		NodeList disorderList = doc.getElementsByTagName("Disorder");
	    //Pour chaque élément de type Disorder Faire
		
		for (int index = 0; index < disorderList.getLength(); index++)
	    {
	    	
	    	Node disorder = disorderList.item(index);
	    	//System.out.println("\n Disorder Numéro: "+(index+1));
	        
	        if (disorder.getNodeType() == Node.ELEMENT_NODE) 
	        {
	        	Element ElementDisorder = (Element) disorder;	        	
	             
	           Element geneList=(Element) ElementDisorder.getElementsByTagName("GeneList").item(0);
               //Genes.addAll(GeneList(geneList));  	
	           
	           String orphaCode=ElementDisorder.getElementsByTagName("OrphaCode").item(0).getTextContent().trim();
	         
               for (Gene G:GeneList(geneList))
	           if(!Contain(this.genes,G))this.genes.add(G);
	           
	           Element AssociationList=(Element)ElementDisorder.getElementsByTagName("DisorderGeneAssociationList").item(0);
	           relationDisorderGene.add(AssociationGenesDisorder(AssociationList,orphaCode));
	           
	           
	             
	        }
	          
	    }
	
		

//for(Association g:Associations)System.out.println(g.toString());
//System.out.println(Associations.size());		
//System.out.println("Done...!!!!");
	}
	
	private static DisorderGene AssociationGenesDisorder(Node N, String OrphaCode) {
		int nmbrelts=N.getChildNodes().getLength();
		
		//int NombreElListe=Integer.parseInt(N.getAttributes().getNamedItem("count").getTextContent());
		//total+=NombreElListe;
		
		DisorderGene GenesDisorder=new DisorderGene();
		GenesDisorder.setOrphaCode(OrphaCode);
		Set<Association> associations= new HashSet<Association>();
		for (int j=0;j<nmbrelts;j++)
		{	
		  if(N.getChildNodes().item(j).getNodeType()==1)
		  {
		  Node localNode= (Node)N.getChildNodes().item(j);
		  associations.add(new Association(localNode));
		  
		  }
		  }
		GenesDisorder.setAssociations(associations);
		return GenesDisorder;	
	}

	private static boolean Contain(Set<Gene> Genes,Gene g) {
    for (Gene G:Genes)
    	if(G.equals(g))return true;
		
		return false;
	}

	private static Set<Gene> GeneList(Node N) {
		int nmbrelts=N.getChildNodes().getLength();
		Set<Gene> genes=new HashSet<Gene>();
		for (int j=0;j<nmbrelts;j++)
		{	
		  if(N.getChildNodes().item(j).getNodeType()==1)
		  {
		  Node localNode= (Node)N.getChildNodes().item(j);
		  genes.add(new Gene(localNode));
		  
		  }
		  }
		return genes;
	}		
	
	
	
	
}