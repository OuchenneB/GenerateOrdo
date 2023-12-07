package model;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class Association {
String id;
Type type;

public Association() {
	
}
public Association(Node R) 
{
	Element El=(Element)R;
	this.id=El.getElementsByTagName("Gene").item(0).getAttributes().item(0).getTextContent();
    this.type=new Type(El.getElementsByTagName("DisorderGeneAssociationType").item(0));
}
@Override
public String toString() {
	return "Association [id=" + id + ", type=" + type + "]";
}
public String getId() {
	return id;
}
public void setId(String id) {
	this.id = id;
}
public Type getType() {
	return type;
}
public void setType(Type type) {
	this.type = type;
}


}