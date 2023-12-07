package model;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class Prevalence {
	Type prevalenceType;
	Type qualification;
	Type prevalenceClass;
	Type prevalenceGeographic;
	String validationStatus;
	float valMoy;
	
	public Prevalence(Node R) 
	{
	Element El=(Element)R;
	this.prevalenceType=new Type(El.getElementsByTagName("PrevalenceType").item(0));
	this.qualification=new Type(El.getElementsByTagName("PrevalenceQualification").item(0));
	if(!(El.getElementsByTagName("PrevalenceClass").item(0).hasAttributes()))this.prevalenceClass=null;
	else this.prevalenceClass=new Type(El.getElementsByTagName("PrevalenceClass").item(0));
	this.prevalenceGeographic=new Type(El.getElementsByTagName("PrevalenceGeographic").item(0));
	if(!(El.getElementsByTagName("PrevalenceValidationStatus").item(0).hasAttributes()))this.validationStatus=null;
	else this.validationStatus=((Element) El.getElementsByTagName("PrevalenceValidationStatus").item(0)).getAttributeNode("id").getValue();
	this.valMoy=Float.parseFloat(El.getElementsByTagName("ValMoy").item(0).getTextContent().trim());
	}

	@Override
	public String toString() {
		return "Prevalence [prevalenceType=" + prevalenceType + ", qualification=" + qualification
				+ ", prevalenceClass=" + prevalenceClass + ", prevalenceGeographic=" + prevalenceGeographic
				+ ", validationStatus=" + validationStatus + ", valMoy=" + valMoy + "]";
	}

	public Type getPrevalenceType() {
		return prevalenceType;
	}

	public void setPrevalenceType(Type prevalenceType) {
		this.prevalenceType = prevalenceType;
	}

	public Type getQualification() {
		return qualification;
	}

	public void setQualification(Type qualification) {
		this.qualification = qualification;
	}

	public Type getPrevalenceClass() {
		return prevalenceClass;
	}

	public void setPrevalenceClass(Type prevalenceClass) {
		this.prevalenceClass = prevalenceClass;
	}

	public Type getPrevalenceGeographic() {
		return prevalenceGeographic;
	}

	public void setPrevalenceGeographic(Type prevalenceGeographic) {
		this.prevalenceGeographic = prevalenceGeographic;
	}

	public String getValidationStatus() {
		return validationStatus;
	}

	public void setValidationStatus(String validationStatus) {
		this.validationStatus = validationStatus;
	}

	public float getValMoy() {
		return valMoy;
	}

	public void setValMoy(float valMoy) {
		this.valMoy = valMoy;
	}
	
	
	
}