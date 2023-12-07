package model;



public class Obsolescence {
	String txtInfos;
	String label;
	String orphaCode;
	
	public Obsolescence() 
	{
		this.txtInfos=".";
		this.label=".";
		this.orphaCode=".";
	
	}
	
	
	
	public String getTxtInfos() {
		return txtInfos;
	}
	public void setTxtInfos(String txtInfos) {
		this.txtInfos = txtInfos;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String Label) {
		this.label = Label;
	}
	public String getOrphaCode() {
		return orphaCode;
	}
	public void setOrphaCode(String orphaCode) {
		this.orphaCode = orphaCode;
	}
	
	
	

}
