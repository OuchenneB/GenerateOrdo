package model;
import java.util.HashSet;
import java.util.Set;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class Epidemiology {
String orphaCode;
Set<Prevalence>  prevalences;
Set<Type>  averageAgeOfOnsetList;
Set<Type>  averageAgeOfDeathList;
Set<Type>  typeOfInheritanceList;



public Epidemiology(Node R) {
	Element El=(Element)R;
	this.orphaCode=El.getElementsByTagName("OrphaCode").item(0).getTextContent().trim();
	//System.out.println(this.orphaCode);
	this.prevalences=PrevalenceList(El.getElementsByTagName("PrevalenceList").item(0));
	
    this.averageAgeOfOnsetList=Types(El.getElementsByTagName("AverageAgeOfOnsetList").item(0));
    this.averageAgeOfDeathList=Types(El.getElementsByTagName("AverageAgeOfDeathList").item(0));
    this.typeOfInheritanceList=Types(El.getElementsByTagName("TypeOfInheritanceList").item(0));
    
}


private Set<Type> Types(Node N) {
	int nmbrelts=N.getChildNodes().getLength();
	Set<Type> types=new HashSet<Type>();
	for (int j=0;j<nmbrelts;j++)
	{	
	  if(N.getChildNodes().item(j).getNodeType()==1)
	  {
	  Node localNode= (Node)N.getChildNodes().item(j);
	  types.add(new Type(localNode));		   
	  
	  }
	  }
	return types;		
	
	
	
}


@Override
public String toString() {
	return "Epidemiology [orphaCode=" + orphaCode + ", prevalences=" + prevalences + ", averageAgeOfOnsetList="
			+ averageAgeOfOnsetList + ", averageAgeOfDeathList=" + averageAgeOfDeathList + ", typeOfInheritanceList="
			+ typeOfInheritanceList + "]";
}


private static Set<Prevalence> PrevalenceList(Node N) {
	int nmbrelts=N.getChildNodes().getLength();
	Set<Prevalence> prevalences=new HashSet<Prevalence>();
	for (int j=0;j<nmbrelts;j++)
	{	
	  if(N.getChildNodes().item(j).getNodeType()==1)
	  {
	  Node localNode= (Node)N.getChildNodes().item(j);
	  prevalences.add(new Prevalence(localNode));		   
	  
	  }
	  }
	return prevalences;	
}



public String getOrphaCode() {
	return orphaCode;
}
public void setOrphaCode(String orphaCode) {
	this.orphaCode = orphaCode;
}
public Set<Prevalence> getPrevalences() {
	return prevalences;
}
public void setPrevalences(Set<Prevalence> prevalences) {
	this.prevalences = prevalences;
}
public Set<Type> getAverageAgeOfOnsetList() {
	return averageAgeOfOnsetList;
}
public void setAverageAgeOfOnsetList(Set<Type> averageAgeOfOnsetList) {
	this.averageAgeOfOnsetList = averageAgeOfOnsetList;
}
public Set<Type> getAverageAgeOfDeathList() {
	return averageAgeOfDeathList;
}
public void setAverageAgeOfDeathList(Set<Type> averageAgeOfDeathList) {
	this.averageAgeOfDeathList = averageAgeOfDeathList;
}
public Set<Type> getTypeOfInheritanceList() {
	return typeOfInheritanceList;
}
public void setTypeOfInheritanceList(Set<Type> typeOfInheritanceList) {
	this.typeOfInheritanceList = typeOfInheritanceList;
}

	
}