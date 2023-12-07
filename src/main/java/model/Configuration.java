package model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Configuration {

public String products_directory;//exemple format: root/ordo/inputs/
public String root_IRIS_model;
public String lang_ordo_model;
static String ordo_ontology;
public String obo_file;
public String genes_file;
public String epidemio_file;
public String definitions_directory;
public String definition_file;
public String DB_URL;
static String user;
public String pwd;



static String lang;//"//app//";
static String root_directory="C:\\Users\\OBoulares\\Desktop\\ORDO\\";
static String OrdoModelRoot	;//		= root_directory + "model_version3\\ORDO_model_root_IRIs2.owl";
static String isoOrdoModel	;//		= root_directory + "model_version3\\" + lang  + "_ORDO_model.owl";
static String OutputOWLFile		;//	= ordo_ontology;
static public Map<String, String> tradsDBurls = new HashMap<String, String>() {
	private static final long serialVersionUID = 1L;
{
    put("pl", "jdbc:mysql://pqlop2f1.inserm.fr:1590/d_orpop1d1_00");
    put("cs", "jdbc:mysql://pqlop2f1.inserm.fr:1590/d_orpop2d1_00");
    put("ru", "jdbc:mysql://pqlop2f1.inserm.fr:1590/d_orpop3d1_00");
}};
static String tradsDBuser			= "orpha_read";
static String tradsDBpass			= "Inserm2022!";
static String disorderLblSqlQuery 	= "SELECT * FROM Disorder WHERE TypeLbl IN ( \"Pat\", \"Syn\")";
static String textInfoSqlQuery 		= "SELECT * FROM Text_Info";
static String MLLSqlQuery 			= "SELECT Id,LblLNG FROM MLL WHERE Type IN (\"Rel\",\"Snd\")";


static public String[][] legalTerms = { { "permits", "http://web.resource.org/cc/Reproduction" },
		{ "permits", "http://web.resource.org/cc/DerivativeWorks" },
		{ "permits", "http://web.resource.org/cc/Distribution" },
		{ "requires", "http://web.resource.org/cc/Notice" },
		{ "requires", "http://web.resource.org/cc/Attribution" }
	};

static public String[] creators = { "Ana Rath",
		"Annie Olry",
		"Marc Hanauer",
		"David Lagorce",
		"Boulares Ouchenne",
		"Valérie Lanneau"
		};

static String CreationDate = "2013-06-20T12:00:00";

static List<String> isoToTranslate 	= Arrays.asList("pl", "cs", "ru");





public Configuration(String Lang, String Version)
{
Configuration.lang=Lang;
//Configuration Inputs;
this.products_directory		=	root_directory+"//"+Lang+"//";//is also classification directory
if(Lang.equalsIgnoreCase("pl")||Lang.equalsIgnoreCase("cs"))Lang="en";
this.obo_file				=	products_directory+"OBO_EBI_"+Lang+".xml";
this.epidemio_file			=	products_directory+Lang+"_product2.xml";
this.genes_file				=	products_directory+Lang+"_product6.xml";
this.root_IRIS_model		=	products_directory+"ORDO_model.owl";

Lang						=	Configuration.lang;

if(Lang.equalsIgnoreCase("pl")||Lang.equalsIgnoreCase("cs"))
{
this.DB_URL	= tradsDBurls.get(lang);
user	=tradsDBuser;
this.pwd	=tradsDBpass;
}	



//Configuration Outputs
this.definition_file=products_directory+Lang+"_model_def.txt";
this.lang_ordo_model=root_directory+Lang+"_ORDO_model.owl";
Configuration.ordo_ontology=root_directory+"ORDO_"+Lang+"_"+Version+".owl";

}



 public String getDB_URL() {
	return DB_URL;
}

public void setDB_URL(String dB_URL) {
	DB_URL = dB_URL;
}

public static String getUser() {
	return user;
}


public String getPwd() {
	return pwd;
}

public void setPwd(String pwd) {
	this.pwd = pwd;
}

public String getRoot_directory() {
	return root_directory;
}

public void setRoot_directory(String root_directory) {
	Configuration.root_directory = root_directory;
}

public String getProducts_directory() {
	return products_directory;
}
public String getClassifications_directory() {
	return products_directory;
}

public void setProducts_directory(String products_directory) {
	this.products_directory = products_directory;
}


public String getRoot_IRIS_model() {
	return root_IRIS_model;
}

public void setRoot_IRIS_model(String root_IRIS_model) {
	this.root_IRIS_model = root_IRIS_model;
}

public String getLang_ordo_model() {
	return lang_ordo_model;
}

public void setLang_ordo_model(String lang_ordo_model) {
	this.lang_ordo_model = lang_ordo_model;
}
public String getLang() {
	return lang;
}

public void setLang(String lang) {
	Configuration.lang = lang;
}



public String getOrdo_ontology() {
	return ordo_ontology;
}

public void setOrdo_ontology(String ordo_ontology) {
	Configuration.ordo_ontology = ordo_ontology;
}

public String getObo_file() {
	return obo_file;
}

public void setObo_file(String obo_file) {
	this.obo_file = obo_file;
}

public String getGenes_file() {
	return genes_file;
}

public void setGenes_file(String genes_file) {
	this.genes_file = genes_file;
}

public String getepidemio_file() {
	return epidemio_file;
}

public void setepidemio_file(String epidemio_file) {
	this.epidemio_file = epidemio_file;
}

public String getDefinitions_directory() {
	return definitions_directory;
}

public void setDefinitions_directory(String definition_directory) {
	this.definitions_directory = definition_directory;
}

public String getDefinition_file() {
	return definition_file;
}

public void setDefinition_file(String definition_file) {
	this.definition_file = definition_file;
}


public static void main(String[] args) {

	System.out.println(new Configuration("cs","4.3").toString());
}

}
