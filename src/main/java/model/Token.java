package model;
import org.w3c.dom.Node;

public class Token {
String Parent;
String type;
Node node;
public String getParent() {
	return Parent;
}
public void setParent(String parent) {
	Parent = parent;
}
public Node getNode() {
	return node;
}
public void setNode(Node node) {
	this.node = node;
}

public String getType() {
	return type;
}
public void setType(String type) {
	this.type = type;
}
public Token(String parent,String type, Node node) {
	this.Parent = parent;
	this.node = node;
	this.type=type;
}



}