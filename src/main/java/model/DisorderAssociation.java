package model;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
public class DisorderAssociation {
Type targetDisorder;
Type disorderAssociationType;
Type RootDisorder;


public DisorderAssociation(Node R) {
	Element El=(Element)R;
	Node TD = El.getElementsByTagName("TargetDisorder").item(0);
	Node RD = El.getElementsByTagName("RootDisorder").item(0);
	Node type = El.getElementsByTagName("DisorderDisorderAssociationType").item(0);
	
	if (!TD.hasChildNodes())
	{
		this.targetDisorder=null;
	   //System.out.println("--->"+TD.getAttributes().item(1).getTextContent());
	}
		else
	{	
		this.targetDisorder= new Type();
		String id=TD.getAttributes().item(0).getTextContent();
		this.targetDisorder.setId(id);
	    String orphCode=((Element)TD).getElementsByTagName("OrphaCode").item(0).getTextContent();
        this.targetDisorder.setOrphaNumber(orphCode);
        String Name=((Element)TD).getElementsByTagName("Name").item(0).getTextContent();
        this.targetDisorder.setName(Name);        
	}
	if (!RD.hasChildNodes())
		this.RootDisorder=null;
		else
		{	
			this.RootDisorder= new Type();
			String id=RD.getAttributes().item(0).getTextContent();
			this.RootDisorder.setId(id);
		    String orphCode=((Element)RD).getElementsByTagName("OrphaCode").item(0).getTextContent();
	        this.RootDisorder.setOrphaNumber(orphCode);
	        String Name=((Element)RD).getElementsByTagName("Name").item(0).getTextContent();
	        this.RootDisorder.setName(Name);
	        
		}
	this.disorderAssociationType=new Type(type);
	/*
	this.targetDisorder=);
	id=El.getAttributeNode("id").getValue();
    this.orphaNumber=El.getElementsByTagName("OrphaNumber").
    */	
}

public Type getTargetDisorder() {
	return targetDisorder;
}
public void setTargetDisorder(Type targetDisorder) {
	this.targetDisorder = targetDisorder;
}
public Type getDisorderAssociationType() {
	return disorderAssociationType;
}
public void setDisorderAssociationType(Type disorderAssociationType) {
	this.disorderAssociationType = disorderAssociationType;
}
public Type getRootDisorder() {
	return RootDisorder;
}
public void setRootDisorder(Type rootDisorder) {
	RootDisorder = rootDisorder;
}

@Override
public String toString() {
	return "DisorderAssociation [targetDisorder=" + targetDisorder + ", disorderAssociationType="
			+ disorderAssociationType + ", RootDisorder=" + RootDisorder + "]";
}



}