package model;
import java.util.Set;

public class DisorderGene {
String orphaCode;
Set<Association> associations;

public DisorderGene() {
}
public String getOrphaCode() {
	return orphaCode;
}
public void setOrphaCode(String orphaCode) {
	this.orphaCode = orphaCode;
}
public Set<Association> getAssociations() {
	return associations;
}
public void setAssociations(Set<Association> associations) {
	this.associations = associations;
}
@Override
public String toString() {
	return "DisorderGene [orphaCode=" + orphaCode + ", associations=" + associations + "]";
}


}