package model;
import java.util.HashSet;
import java.util.Set;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class Gene {
String id;
String orphaNumber;
String name;
String symbol;
Type geneType;
Set<String> synonymList;
Set<Reference> referenceList;//Set<String> set = new HashSet<String>();
Set<Locus> locusList;
public Gene(Node R) {
	Element El=(Element)R;
	this.id=El.getAttributeNode("id").getValue();
    this.orphaNumber=El.getElementsByTagName("OrphaNumber").item(0).getTextContent().trim();
    this.name=El.getElementsByTagName("Name").item(0).getTextContent().trim(); 
    this.symbol=El.getElementsByTagName("Symbol").item(0).getTextContent().trim();
    // class ecrite Type
    this.geneType=new Type(El.getElementsByTagName("GeneType").item(0));
    // classes plus bas
    this.synonymList=SynonymList(El.getElementsByTagName("SynonymList").item(0));
    this.referenceList=ReferenceList(El.getElementsByTagName("ExternalReferenceList").item(0));
    this.locusList=LocusList(El.getElementsByTagName("LocusList").item(0));
}


public boolean equals(Gene obj) {
	//if((this.id.equals(obj.id))&&(this.toString().length()==obj.toString().length())) {System.out.println(this.toString());System.out.println(obj.toString());return true;}
	if(this.id.equals(obj.id))return true;
	
	//if(this.toString().equals(obj.toString()))return true;
	return false;
}



@Override
public String toString() {
	return "Gene [id=" + id + ", orphaNumber=" + orphaNumber + ", name=" + name + ", symbol=" + symbol + ", geneType="
			+ geneType + ", synonymList=" + synonymList + ", referenceList=" + referenceList + ", locusList="
			+ locusList +"]";
}


private static Set<String> SynonymList(Node N) {
	int nmbrelts=N.getChildNodes().getLength();
	Set<String> synonyms=new HashSet<String>();
	for (int j=0;j<nmbrelts;j++)
	{	
	  if(N.getChildNodes().item(j).getNodeType()==1)
	  {
	  Node localNode= (Node)N.getChildNodes().item(j);
	  String temp=localNode.getTextContent().trim();
	  synonyms.add(temp);
	  }
	  }
	return synonyms;
}
private static Set<Reference> ReferenceList(Node N) {
	int nmbrelts=N.getChildNodes().getLength();
	Set<Reference> references=new HashSet<Reference>();
	for (int j=0;j<nmbrelts;j++)
	{	
	  if(N.getChildNodes().item(j).getNodeType()==1)
	  {
	  Node localNode= (Node)N.getChildNodes().item(j);
	  references.add(new Reference(localNode,""));		   
	  
	  }
	  }
	return references;	
}

private static Set<Locus> LocusList(Node N) {
	int nmbrelts=N.getChildNodes().getLength();
	Set<Locus> Locuces=new HashSet<Locus>();
	for (int j=0;j<nmbrelts;j++)
	{	
	  if(N.getChildNodes().item(j).getNodeType()==1)
	  {
	  Node localNode= (Node)N.getChildNodes().item(j);
	  Locuces.add(new Locus(localNode));		   
	  
	  }
	  }
	return Locuces;	
}


public String getId() {
	return id;
}


public void setId(String id) {
	this.id = id;
}


public String getOrphaNumber() {
	return orphaNumber;
}


public void setOrphaNumber(String orphaNumber) {
	this.orphaNumber = orphaNumber;
}


public String getName() {
	return name;
}


public void setName(String name) {
	this.name = name;
}


public String getSymbol() {
	return symbol;
}


public void setSymbol(String symbol) {
	this.symbol = symbol;
}


public Type getGeneType() {
	return geneType;
}


public void setGeneType(Type geneType) {
	this.geneType = geneType;
}


public Set<String> getSynonymList() {
	return synonymList;
}


public void setSynonymList(Set<String> synonymList) {
	this.synonymList = synonymList;
}


public Set<Reference> getReferenceList() {
	return referenceList;
}


public void setReferenceList(Set<Reference> referenceList) {
	this.referenceList = referenceList;
}


public Set<Locus> getLocusList() {
	return locusList;
}


public void setLocusList(Set<Locus> locusList) {
	this.locusList = locusList;
}


}