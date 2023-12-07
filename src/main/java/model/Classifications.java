package model;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class Classifications {
	// c'est un ensemble de relations
Set<Relation> Relations;

static HashSet<String> PartOfClass = new HashSet<String>();
static HashSet<String> GroupDisorderClass = new HashSet<String>();
static HashSet<String> SubTypeClass = new HashSet<String>();
//static HashSet<String> Combainiasons = new HashSet<String>();

public Classifications() 
{
this.Relations=new HashSet<Relation>();
//this.Combainiasons=new HashSet<String>(); // ecrit pour donner les combinaisons possibles pour Annie
}



private static boolean IsPartOf(String childType, String parentType) {
	// fonction qui determine les partof 
	// vu avec Annie Olry dans fichier class_combinaisons_ORDO_Boulares_Annie.docx
	// retourne false si c'est du is_a
	if (childType.equals("21436")&&parentType.equals("36561")) return false;
	if (childType.equals("21443")&&parentType.equals("21443")) return false;
	if (childType.equals("36561")&&parentType.equals("21436")) return false;	
	if (childType.equals("21450")&&parentType.equals("21450")) return false;
	if (childType.equals("36561")&&parentType.equals("36561")) return false;	
	if (childType.equals("21436")&&parentType.equals("21436")) return false;   
	if (childType.equals("21443")&&parentType.equals("21450")) return false;
	if (childType.equals("21457")&&parentType.equals("21450")) return false;
	return true;
}


public void AddClassification(String InputFile) throws ParserConfigurationException, SAXException, IOException
{
	
	/*
	 * Ancienne version pour les relations avant d'utiliser IsPartOf
	PartOfClass.add("21394"); PartOfClass.add("21408"); PartOfClass.add("21422"); PartOfClass.add("21401"); PartOfClass.add("21415"); PartOfClass.add("21429");
	GroupDisorderClass.add("36561"); GroupDisorderClass.add("21436");
	PartOfClass.add("21450"); PartOfClass.add("21443"); PartOfClass.add("21457");
	SubTypeClass.add("21450"); SubTypeClass.add("21443"); SubTypeClass.add("21457");
	*/
	
	// ouverture du fichier classif 
	File inputFile = new File(InputFile);
	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	Document doc = dBuilder.parse(inputFile);
	doc.getDocumentElement().normalize();
    Vector<Token> toBeTreated= new Vector<Token>();
    Relation R=null;

    //R�cupere ClassificationNodeRootList 
    Node NodeRootList = (Node) doc.getElementsByTagName("ClassificationNode").item(0);               		
	Element ClassificationNodeRoot = (Element)NodeRootList;
	//R�cupere Disorder Head 
	Element disorder=(Element)ClassificationNodeRoot.getElementsByTagName("Disorder").item(0);
	
	
	// enfants de Disorder Head 
	Element ClassificationChildList=(Element)ClassificationNodeRoot.getElementsByTagName("ClassificationNodeChildList").item(0);
		
	for (int index = 0; index < ClassificationChildList.getChildNodes().getLength(); index++)
    {
		// pour chaque enfant
    	Node Classif= (Node) ClassificationChildList.getChildNodes().item(index);
		String Parent=disorder.getElementsByTagName("OrphaCode").item(0).getTextContent().trim();
		//ID of Category
		String ParentType="36561";
        
        if (Classif.getNodeType() == Node.ELEMENT_NODE) 
        {
        	Element Next=(Element) ((Element) Classif).getElementsByTagName("ClassificationNodeChildList").item(0);
        		
        	Element disorder_child=(Element) ((Element) Classif).getElementsByTagName("Disorder").item(0);
        	String ChildType=disorder_child.getElementsByTagName("DisorderType").item(0).getAttributes().item(0).getTextContent();
        	
        	//ajoute a toBeTreated avec le rang de la liste
        	toBeTreated.add(toBeTreated.size(),                     
        			new Token(disorder_child.getElementsByTagName("OrphaCode").item(0).getTextContent().trim(),disorder_child.getElementsByTagName("DisorderType").item(0).getAttributes().item(0).getTextContent(),Next)
        			);	
        	
        	
        	String Child=disorder_child.getElementsByTagName("OrphaCode").item(0).getTextContent().trim();       	
	       	
        	
  //      	this.Combainiasons.add("[ Fils = "+Value(ChildType)+", Parent = "+Value(ParentType)+"]");	
    	
        	
        	// test des enfants pour savoir si PartOf
        	if (IsPartOf(ChildType,ParentType)) 
        	{
        		
        		R=new Relation(Parent,0,Child);
        		//if(!contains(R))
        			Relations.add(R);
        	}
        	else // ce cas est is_a
        	{	// valeur 1
        	    R=new Relation(Parent,1,Child);
        	    //if(!contains(R))
        	    	Relations.add(R);	
        	}       
      }

    }

////End
	
for(int i=0;i<toBeTreated.size();i++)
	
{
	
	Token T=toBeTreated.get(i);
	//System.out.println(T.getNode().getTextContent().toString());
	// ajoute au fur a mesure les enfants dans toBeTreated et boucle tant que child n'a pas d'enfant
	
	for (int index = 0; index < T.getNode().getChildNodes().getLength(); index++)
    {
    	Node Classif= (Node) T.getNode().getChildNodes().item(index);
    	
        if (Classif.getNodeType() == Node.ELEMENT_NODE) 
        {
        	Element Next=(Element) ((Element) Classif).getElementsByTagName("ClassificationNodeChildList").item(0);
        		
        	Element disorder_child=(Element) ((Element) Classif).getElementsByTagName("Disorder").item(0);
        	toBeTreated.add(toBeTreated.size(),
        			new Token(disorder_child.getElementsByTagName("OrphaCode").item(0).getTextContent().trim(),disorder_child.getElementsByTagName("DisorderType").item(0).getAttributes().item(0).getTextContent(),Next)
              		);
        
            
            String Child=disorder_child.getElementsByTagName("OrphaCode").item(0).getTextContent().trim();       	
    		
            String ChildType=disorder_child.getElementsByTagName("DisorderType").item(0).getAttributes().item(0).getTextContent();
            
        	
           
      //  	this.Combainiasons.add("[ Fils = "+Value(ChildType)+", Parent = "+Value(T.getType())+"]");	
            
            if (IsPartOf(ChildType,T.getType())) 
        	{
        		R=new Relation(T.getParent(),0,Child);
        		//if(!contains(R))
        			Relations.add(R);
        	}
        	else 
        	{
        	    R=new Relation(T.getParent(),1,Child);
        	    //if(!contains(R))
        	    	Relations.add(R);	
        	}      
            
    			
        
        
        
        
        }	
    }
	
}	
}



public Set<Relation> getRelations() {
	return Relations;
}

public void setRelations(Set<Relation> relations) {
	Relations = relations;
}


}