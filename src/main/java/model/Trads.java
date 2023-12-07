package model;
import model.GenerateOrdo;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import model.DisorderTrad;




public class Trads {
	public Set<DisorderTrad> Disorders;
	public Set<RelationTrad> reltrad; 
	public Trads(String lang)
	{
		this.Disorders=new HashSet<DisorderTrad>();
		this.reltrad=new HashSet<RelationTrad>();
		this.LoadSynonymsAndLabels(lang);
		this.LoadDefinitions(lang);
		this.LoadRelationTrad(lang);
		
	}

public String getRelTrad(String id)
{
	for (RelationTrad D:this.getReltrad())
		if (D.getId().equals(id))return D.getTrad();

	System.out.println("Pas de définiton dans la base pour la relation"+id);
	System.exit(0);

return ""; 	
}	
	
public static void main(String[] args) {
	
	Trads T=new Trads("");
	//T.LoadSynonymsAndLabels();
	//T.LoadDefinitions();
	//int compt=0;
	for (DisorderTrad D:T.getDisorders())
	{
		//ELTS.add(D.getOrphaCode());
		if(D.getSynonyms().isEmpty()) 
		{
			
			System.out.println(D.toString());
		}
		
	}
	//System.out.println(ELTS.size()+"---"+compt+"---"+T.getDisorders().size());
	
}	


public Set<RelationTrad> getReltrad() {
	return reltrad;
}


public void setReltrad(Set<RelationTrad> reltrad) {
	this.reltrad = reltrad;
}


public void LoadRelationTrad(String lang) {
	try {
		// Loading the required JDBC Driver class
		Class.forName("com.mysql.cj.jdbc.Driver");
		// Creating a connection to the database        
		String DB_URL = GenerateOrdo.Config.getDB_URL();
		System.out.println("Getting external references relations Labels for " + lang);		
		System.out.println("Connected to: " + DB_URL);		
		final Properties prop = new Properties();
		prop.put("user", Configuration.getUser());
		prop.put("password",GenerateOrdo.Config.getPwd());	
		Connection conn = DriverManager.getConnection( DB_URL, prop );
		// Executing SQL query and fetching the result
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(Configuration.MLLSqlQuery);
		
		
		while (rs.next()) 
		{

			String labelLng = rs.getString("LblLNG");
			String id = rs.getString("Id");
			
			
			this.reltrad.add(new RelationTrad(id,labelLng));
			
		
			//System.out.println(orphaCode + "\t" + typeLBL + "\t" + labelISO);
			//System.out.println();
		}
		







} catch (Exception e) {
//TODO Auto-generated catch block
e.printStackTrace();
}
	
}






	public void LoadSynonymsAndLabels(String lang)
	{

		try {
			// Loading the required JDBC Driver class
			Class.forName("com.mysql.cj.jdbc.Driver");
			String DB_URL = GenerateOrdo.Config.getDB_URL();
			System.out.println("Getting Synonyms And Labels for " + lang);		
			System.out.println("Connected to: " + DB_URL);			
			final Properties prop = new Properties();
			prop.put("user", Configuration.getUser());
			prop.put("password",GenerateOrdo.Config.getPwd());	
			Connection conn = DriverManager.getConnection( DB_URL, prop );
			// Executing SQL query and fetching the result
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(Configuration.disorderLblSqlQuery);
			
			
			while (rs.next()) 
			{

				String labelISO = rs.getString("LblLNG");
				
				
			
											
				String orphaCode = rs.getString("ORPHA");
				String typeLBL = rs.getString("TypeLbl");
				
				
				this.AddTupleLabsSyns(orphaCode,typeLBL,labelISO);
				
			
				//System.out.println(orphaCode + "\t" + typeLBL + "\t" + labelISO);
				//System.out.println();
			}
			
	






} catch (Exception e) {
// TODO Auto-generated catch block
e.printStackTrace();
}
}
	
	public void LoadDefinitions(String lang) {
		try {
			// Loading the required JDBC Driver class
			Class.forName("com.mysql.cj.jdbc.Driver");
			String DB_URL = GenerateOrdo.Config.getDB_URL();
			System.out.println("Getting Definitions for " + lang);	
			System.out.println("Connected to: " + DB_URL);			
			final Properties prop = new Properties();
			prop.put("user", Configuration.getUser());
			prop.put("password",GenerateOrdo.Config.getPwd());	
			Connection conn = DriverManager.getConnection( DB_URL, prop );
			// Executing SQL query and fetching the result
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(Configuration.textInfoSqlQuery);
			
			
			while (rs.next()) 
			{

						
			
											
				String orphaCode = rs.getString("PatORPHA");
				String Def = rs.getString("TxtLNG");
				String nature = rs.getString("SectId");
				
				///this.AddTupleDef(orphaCode,typeLBL,labelISO);
				
			    if(nature.equals("16907"))
			    {			
			    	if(Def==null)Def=".";
			    	this.AddTupleDef(orphaCode,Def);
			    }	
			    	//System.out.println(orphaCode + "\t" + Def + "\t");
				//System.out.println();
			    
			}
			
	






} catch (Exception e) {
// TODO Auto-generated catch block
e.printStackTrace();
}

	
}
	

	public Set<DisorderTrad> getDisorders() {
		return Disorders;
	}
	public void setDisorders(Set<DisorderTrad> disorders) {
		Disorders = disorders;
	}

public void AddTupleDef(String orphaCode, String def) {
	for(DisorderTrad T:this.Disorders) 
   		if(T.getOrphaCode().equals(orphaCode))
   		{
   			T.setDefs(def);return;
   		}		
	System.out.println("No Element Defined!!!!");System.exit(0);	
	}
	public void AddTupleLabsSyns(String orphaCode, String type, String label)
	{
		for(DisorderTrad T:this.Disorders) 
	   		if(T.getOrphaCode().equals(orphaCode))
	   		{
	   			if (type.equals("Pat"))
	   			T.setLabel(label);
	   			else if (type.equals("Syn"))
	   					T.getSynonyms().add(label);
	   			else {System.out.println("Wrong Value: "+type);System.exit(0);}
	   			
	   			return;
	   		}  	
		
		DisorderTrad Temp=new DisorderTrad();
		if (type.equals("Pat"))
		Temp.setLabel(label);
		else if (type.equals("Syn"))
				Temp.getSynonyms().add(label);
		else {System.out.println("Wrong Value: "+type);System.exit(0);}
		Temp.setOrphaCode(orphaCode);
		Temp.setDefs(".");
		this.Disorders.add(Temp);
		
	}
	public boolean Contains(String orphaCode) {
	   	for(DisorderTrad T:this.Disorders) 
	   		if(T.getOrphaCode().equals(orphaCode))return true;
		
		return false;
	}
	
}
