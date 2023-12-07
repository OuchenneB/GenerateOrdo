package model;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
public class Reference {
String source, reference, manualAssertion,idrel,idicdrel;

public String getIdrel() {
	return idrel;
}


public void setIdrel(String idrel) {
	this.idrel = idrel;
}


public String getIdicdrel() {
	return idicdrel;
}


public void setIdicdrel(String idicdrel) {
	this.idicdrel = idicdrel;
}


public Reference() {
	
	
}


public String toString() {
	return "Reference [source=" + source + ", reference=" + reference + ", manualAssertion=" + manualAssertion + "]";
}

public Reference(Node R) {
	Element El=(Element)R;
	this.source=El.getElementsByTagName("Source").item(0).getTextContent().trim();	
	this.reference=El.getElementsByTagName("Reference").item(0).getTextContent().trim();
    this.manualAssertion=El.getElementsByTagName("Name").item(0).getTextContent().trim(); 
    if (this.source.contains("ICD"))this.manualAssertion="- "+this.manualAssertion+".\n- "+
    El.getElementsByTagName("DisorderMappingICDRelation").item(0).getChildNodes().item(3).getTextContent().trim()+"."; 
    this.idrel=El.getElementsByTagName("DisorderMappingRelation").item(0).getAttributes().item(0).getNodeValue();
    
    if (this.source.contains("ICD"))
        this.idicdrel=El.getElementsByTagName("DisorderMappingICDRelation").item(0).getAttributes().item(0).getNodeValue();
    else this.idicdrel="."; 
    //System.out.println(this.idrel+" _ "+this.idicdrel);
}
public Reference(Node R, String type) {
	Element El=(Element)R;
	this.source=El.getElementsByTagName("Source").item(0).getTextContent().trim();	
	this.reference=El.getElementsByTagName("Reference").item(0).getTextContent().trim();
    this.manualAssertion=type; 
}


public String getSource() {
	return source;
}

public void setSource(String source) {
	this.source = source;
}

public String getReference() {
	return reference;
}

public void setReference(String reference) {
	this.reference = reference;
}

public String getManualAssertion() {
	return manualAssertion;
}

public void setManualAssertion(String manualAssertion) {
	this.manualAssertion = manualAssertion;
}

}