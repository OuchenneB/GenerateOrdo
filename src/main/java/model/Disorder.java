package model;
import java.util.HashSet;
import java.util.Set;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class Disorder {
String orphaCode;
String name;
Type disorderType;
Set<String> alternativeTerms;
Set<Reference> referenceList;
String summary;
Set<String> Flags;
Set<DisorderAssociation>  DisorderAssociationList;
Obsolescence obs;
public String getSummary() {
	return summary;
}
public void setSummary(String summary) {
	this.summary = summary;
}



@Override
public String toString() {
	return "Disorder [orphaCode=" + orphaCode + ", name=" + name + ", disorderType=" + disorderType
			+ ", alternativeTerms=" + alternativeTerms + ", referenceList=" + referenceList + ", summary=" + summary
			+ "]";
}
public Disorder() {
	this.alternativeTerms=new HashSet<String>();
	this.referenceList=new HashSet<Reference>();
    this.DisorderAssociationList=new HashSet<DisorderAssociation>();
}

public Disorder(Node N) {
Element disorder=(Element)N;	
this.orphaCode=disorder.getElementsByTagName("OrphaCode").item(0).getTextContent().trim();


//System.out.println("OrphaCode: "+this.orphaCode);

this.name=disorder.getElementsByTagName("Name").item(0).getTextContent().trim();
this.disorderType=new Type(disorder.getElementsByTagName("DisorderType").item(0));
this.Flags=FlagList(disorder.getElementsByTagName("DisorderFlagList").item(0));
this.alternativeTerms=SynonymList(disorder.getElementsByTagName("SynonymList").item(0));
this.referenceList=ReferenceList(disorder.getElementsByTagName("ExternalReferenceList").item(0));
this.summary=Summary(disorder.getElementsByTagName("SummaryInformationList").item(0));

//System.out.println(this.orphaCode);
// parse Obsolete 455 and deprecated 459
this.obs=new Obsolescence();
if(this.Flags.contains("455")||this.Flags.contains("461")||this.Flags.contains("459")||this.Flags.contains("456"))
{
this.obs=new Obsolescence();	

this.DisorderAssociationList=DisorderAssociationList(disorder.getElementsByTagName("DisorderDisorderAssociationList").item(0));
     if(DisorderAssociationList.size()>0)
     { for (DisorderAssociation o:DisorderAssociationList)
     {
    	 
    	 
    	 if(o.targetDisorder!=null)
    	// System.out.println("--> Target: " + o.getTargetDisorder().getOrphaNumber());
    	 {
    	 obs.setOrphaCode(o.getTargetDisorder().getOrphaNumber());   	 
    	 obs.setLabel(o.targetDisorder.getName());
    	 }     
    	 }
     
     Node P=disorder.getElementsByTagName("SummaryInformationList").item(0);
    	 obs.setTxtInfos(((Element) P).getElementsByTagName("Info").item(0).getTextContent());
     }

     if (this.Flags.contains("456"))
      	obs.setTxtInfos("This disease is not rare in Europe. It does not belong to the Orphanet nomenclature of rare diseases.");
      obs.setTxtInfos(obs.getTxtInfos().replace("<br/>", ""));
     //System.out.println("TXT: "+obs.getTxtInfos());
    
    	 //System.out.println("Element: "+this.orphaCode);
	//System.out.println(this.DisorderAssociationList.toString());
	

}


}
private Set<DisorderAssociation> DisorderAssociationList(Node N) {
Set<DisorderAssociation> Assocs=new HashSet<DisorderAssociation>();
int nmbrelts=N.getChildNodes().getLength();
for (int j=0;j<nmbrelts;j++)
{	
  if(N.getChildNodes().item(j).getNodeType()==1)
  {
  Node localNode= (Node)N.getChildNodes().item(j);
  Assocs.add(new DisorderAssociation(localNode));		   
  
  }
  }
return Assocs;	

	

}
private static String Summary(Node N) {
	
	int NombreElListe=Integer.parseInt(N.getAttributes().getNamedItem("count").getTextContent());
	if (NombreElListe==0)return ".";
	int nmbrelts=N.getChildNodes().getLength();

	
	for (int j=0;j<nmbrelts;j++)
	{	
	  if(N.getChildNodes().item(j).getNodeType()==1)
	  {
	  Node localNode= (Node)N.getChildNodes().item(j);
	  Element El=(Element)localNode;
	 
	  //R�cuperer l'attribut si -1 alors get "Info" sinon "Contents"
		  String attribut=localNode.getAttributes().item(0).getTextContent();
	  
	  if(attribut.equals("-1"))return ".";//El.getElementsByTagName("Info").item(0).getTextContent();;
	  //ligne ajout�e 28/11
	  
	  Node L=El.getElementsByTagName("TextSectionList").item(0);
	  attribut=L.getAttributes().item(0).getTextContent();
	  	  if(attribut.equals("0"))return ".";
	  //fin ligne ajout� 28/11
	  return El.getElementsByTagName("Contents").item(0).getTextContent();
	  }
	  }
	return ".";
	
	
	
}
private static Set<String> FlagList(Node N) {
Set<String> Flags=new HashSet<String>();

	
	int nmbrelts=N.getChildNodes().getLength();

	
	for (int j=0;j<nmbrelts;j++)
	{	
	  if(N.getChildNodes().item(j).getNodeType()==1)
	  {
	  Node localNode= (Node)N.getChildNodes().item(j);
	 
	//System.out.println(El.getTextContent().trim());
	  //R�cuperer l'attribut si -1 alors get "Info" sinon "Contents"
       String attribut=localNode.getAttributes().item(0).getTextContent();
       Flags.add(attribut);
	  //if(attribut.equals("-1"));//El.getElementsByTagName("Info").item(0).getTextContent();;
	  //return El.getElementsByTagName("Contents").item(0).getTextContent();
	  }
	  }
	
	
return Flags;	
	
}


private static Set<Reference> ReferenceList(Node N) {
	int nmbrelts=N.getChildNodes().getLength();
	Set<Reference> references=new HashSet<Reference>();
	for (int j=0;j<nmbrelts;j++)
	{	
	  if(N.getChildNodes().item(j).getNodeType()==1)
	  {
	  Node localNode= (Node)N.getChildNodes().item(j);
	  references.add(new Reference(localNode));		   
	  
	  }
	  }
	return references;	
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
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}

public  Type getDisorderType() {
	return disorderType;
}
public void setDisorderType( Type disorderType) {
	this.disorderType = disorderType;
}
public Set<String> getAlternativeTerms() {
	return alternativeTerms;
}
public void setAlternativeTerms(Set<String> alternativeTerms) {
	this.alternativeTerms = alternativeTerms;
}
public Set<Reference> getReferenceList() {
	return referenceList;
}
public void setReferenceList(Set<Reference> referenceList) {
	this.referenceList = referenceList;
}
public String getOrphaCode() {
	return orphaCode;
}
public void setOrphaCode(String orphaCode) {
	this.orphaCode = orphaCode;
}
public Set<String> getFlags() {
	return Flags;
}
public void setFlags(Set<String> flags) {
	Flags = flags;
}
public Set<DisorderAssociation> getDisorderAssociationList() {
	return DisorderAssociationList;
}
public void setDisorderAssociationList(Set<DisorderAssociation> disorderAssociationList) {
	DisorderAssociationList = disorderAssociationList;
}
public Obsolescence getObs() {
	return obs;
}
public void setObs(Obsolescence obs) {
	this.obs = obs;
}




}