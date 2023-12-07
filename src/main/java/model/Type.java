//renommer pour Type

package model;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class Type {
String orphaNumber;
String name;
String id;


public Type() {
	
}
public Type(Node N) {
	Element El=(Element)N;
	this.orphaNumber=El.getElementsByTagName("OrphaNumber").item(0).getTextContent().trim();	
	this.name=El.getElementsByTagName("Name").item(0).getTextContent().trim();
	this.id=El.getAttributes().item(0).getTextContent();
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
public String getId() {
	return id;
}
public void setId(String id) {
	this.id = id;
}
@Override
public String toString() {
	return "Type [orphaNumber=" + orphaNumber + ", name=" + name + ", id=" + id + "]";
}

	
}