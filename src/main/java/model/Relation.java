package model;
// class pour creer les relation parents enfants 
// cela fonctionnne de maniere binaire 0 et 1 
// si c'est 0 c'est partOf et 1 subclassOf
// map les relations des classifications 
public class Relation {
String Parent;
int Relation;
String Child;
public String getParent() {
	return Parent;
}
public void setParent(String parent) {
	Parent = parent;
}
public int getRelation() {
	return Relation;
}
public void setRelation(int relation) {
	Relation = relation;
}
public String getChild() {
	return Child;
}
public void setChild(String child) {
	Child = child;
}
public Relation(String parent, int relation, String child) {
	super();
	Parent = parent;
	Relation = relation;
	Child = child;
}
@Override
public String toString() {
	
	if(this.Relation==0)
		return "Relation [Parent=" + Parent + ", Relation= Part_Of, Child=" + Child + "]";
	return "Relation [Parent=" + Parent + ", Relation= SubClass"+", Child=" + Child + "]";

}

}