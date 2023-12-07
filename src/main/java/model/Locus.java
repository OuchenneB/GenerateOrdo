package model;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class Locus {
String locus,key;

public Locus(String locus, String key) {
	
	this.locus = locus;
	this.key = key;
}

public Locus(Node localNode) {
	Element El=(Element)localNode;
	this.locus=El.getElementsByTagName("GeneLocus").item(0).getTextContent().trim();	
	this.key=El.getElementsByTagName("LocusKey").item(0).getTextContent().trim();
}

@Override
public String toString() {
	return "Locus [locus=" + locus + ", key=" + key + "]";
}

public String getLocus() {
	return locus;
}

public void setLocus(String locus) {
	this.locus = locus;
}

public String getKey() {
	return key;
}

public void setKey(String key) {
	this.key = key;
}


}
