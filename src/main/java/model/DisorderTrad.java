package model;

import java.util.HashSet;
import java.util.Set;

public class DisorderTrad {
	String orphaCode;
	String label;
	String defs;
	Set<String> synonyms;
public  DisorderTrad() {
	this.synonyms=new HashSet<String>();

	
}
public String getOrphaCode() {
	return orphaCode;
}
public void setOrphaCode(String orphaCode) {
	this.orphaCode = orphaCode;
}
public Set<String> getSynonyms() {
	return synonyms;
}
public void setSynonyms(Set<String> synonyms) {
	this.synonyms = synonyms;
}
public String getDefs() {
	return defs;
}
public void setDefs(String defs) {
	this.defs = defs;
}
public String getLabel() {
	return label;
}
public void setLabel(String label) {
	this.label = label;
}
@Override
public String toString() {
	return "DisorderTrad [orphaCode=" + orphaCode + ", label=" + label + ", defs=" + defs + ", synonyms=" + synonyms
			+ "]";
}

	

}
