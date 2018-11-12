package Utils;

import java.io.FileOutputStream;
import java.rmi.server.ExportException;
import java.util.ArrayList;

import org.jdom2.*;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import Proba.Generator;
import Proba.LoiExponentielle;
import Proba.Uniform;

public class GenXMLFile {

	static Element instance= new Element("instance");
	private static org.jdom2.Document XCSPinstance;
    
	static Element domains= new Element("domains");
	static Element variables= new Element("variables");
	static Element predicates= new Element("predicates");
	static Element relations= new Element("relations");
	static Element constraints= new Element("constraints");
	
	int nbVars=0;
	int nbDoms=0;
	int nbPre=0;
	int nbRel=0;
	int nbCons=0;
	
	private int nodes;
	private double density;
	private double lambda;
	private int edges;
	private int maxNodeBound=0;
	private ArrayList<Integer> echantillon= null;
	private ArrayList<Integer> pickDomain= new ArrayList<Integer>();
	
	
	private void buildPickDomain()
	{
		for(int i=1;i<= nodes;i++){
			
			int j= echantillon.get(i-1)/2;
			for(int k=1;k<=j;k++)
				pickDomain.add(i);
		}
	}
	
	public GenXMLFile(int nodes, int edges, double density, double lambda)
	{
		setXCSPinstance(new Document(instance));
		Element rac1= new Element("presentation");
		Attribute a1= new Attribute("name", "?");
		Attribute a2= new Attribute("maxConstraintArity", "2");
		Attribute a3= new Attribute("format", "XCSP 2.0");
		rac1.setAttribute(a1);
		rac1.setAttribute(a2);
		rac1.setAttribute(a3);
		instance.addContent(rac1);
		
		this.nodes= nodes;
		this.edges= edges;
		this.density= density;
		this.lambda= lambda;
	}
	
	public int getNodes() {
		return nodes;
	}

	public int getEdges() {
		return edges;
	}
	
	public int getMaxNodeBound() {
		return maxNodeBound;
	}

	private void genVars()
	{
		LoiExponentielle expo= new LoiExponentielle(lambda);
		this.echantillon = expo.generate(nodes+1);
		
		buildPickDomain();
		
		String vars="";
		
		for(int i=1;i<= nodes;i++)
		{
			//Create a domain
			   // That excludes i and contains only 30 nodes
			String domainName= "N"+i;
			
			//number of connected nodes for each node
			int nodesBound= echantillon.get(i-1)/2 + echantillon.get(i-1) % 2; 
			
			//Update maxNodeBound
			if(nodesBound > maxNodeBound) {
				maxNodeBound = nodesBound;
			}
			
			createDomain(domainName, 1, nodes, i, nodesBound);
			
			
			//Create a node variable
			String variableName;
			String varsAllDiff="";
			int j=1;
			while(j<=nodesBound && nbVars<edges)
			{
				//Create j link variables
				variableName= "n"+i+"_"+j;
				vars= vars+ " "+variableName;
				createVariable(variableName,domainName);
				varsAllDiff= varsAllDiff+ " n"+i+"_"+j;
				j++;
			}
			
			//Create one all diff constraint for pointed nodes
			if(!varsAllDiff.equals("") && nodesBound!=1 )
				createAllDiff("AllDiff"+i, j-1, varsAllDiff);
		}
	
		String vals="";
		
		for(int i=1;i<=nodes;i++)
			vals= vals+" "+i;
	}
	
	public void createGcc3(String nom,int arity,int valsarity,String vars,String vals, ArrayList<Integer> lower, ArrayList<Integer> upper)
	{
		Element cons=new Element("constraint");
		nbCons++;
		
		//Les parametres
		//String vars= "";
		String lb=" ",ub=" ";
		
	    vals= ""+ vals;
		
	    //System.out.println("arity vals="+valsarity);
	    
		//Valeurs et bornes inf et sup
		for(int i=1;i<=valsarity;i++)
		{
			int l= 0;
			int u= upper.get(i-1)/2;
			
			if(u==0) u=1;
			
			lb=lb +l+" ";
			ub=ub +u+" ";
		}
		
        
		String pText="["+ vars +" ] ["+ vals +" ] ["+lb+"] ["+ub+"]";
		Element param= new Element("parameters");
		param.setText(pText);
		cons.addContent(param);
		
		//Les attributs
		Attribute name= new Attribute("name", nom);
		cons.setAttribute(name);
		Attribute Arity= new Attribute("arity", ""+arity);
		cons.setAttribute(Arity);
		Attribute Scope= new Attribute("scope", ""+vars);
		cons.setAttribute(Scope);
		Attribute refe= new Attribute("reference", "global:globalCardinality");
		cons.setAttribute(refe);
		
		constraints.addContent(cons);
		

	}
	
	public ArrayList<Integer> getEchantillon() {
		return echantillon;
	}

	public void createAllDiff(String nameS, int arity, String vars)
	{
		Element cons=new Element("constraint");
		nbCons++;
		
		//Les parametres	
        
		String pText="["+ vars +"]";
		Element param= new Element("parameters");
		param.setText(pText);
		cons.addContent(param);
		
		//Les attributs
		Attribute name= new Attribute("name", nameS);
		cons.setAttribute(name);
		Attribute Arity= new Attribute("arity", ""+arity);
		cons.setAttribute(Arity);
		Attribute Scope= new Attribute("scope", ""+vars);
		cons.setAttribute(Scope);
		Attribute refe= new Attribute("reference", "global:allDifferent");
		cons.setAttribute(refe);
		
		constraints.addContent(cons);
	
	}
	
		
	private void createVariable(String variableName, String domainName) {
		// TODO Auto-generated method stub
		Element variablef= new Element("variable");
    	Attribute namef= new Attribute("name",variableName);
    	Attribute domf= new Attribute("domain",domainName);
    	variablef.setAttribute(domf);
    	variablef.setAttribute(namef);
    	variables.addContent(variablef); 
    	nbVars++;
    	
	}
	
//	private void createDomain(String domainName,int lower, int upper, int exclude, int nb)
//	{
//		Element domaine = new Element("domain");
//		Attribute name= new Attribute("name", domainName);
//		domaine.setAttribute(name);
//		Attribute nbValuesD;
//		String textD= "";
//		int nbValues=0;
//		
//		Uniform unif= new Uniform(lower, upper);
//		ArrayList<Integer> mesValeurs = unif.generate(3*nb+1);
//		
//	//	LoiExponentielle loiExpo= new LoiExponentielle(2);
//	//	mesValeurs= loiExpo.generate(nb);
//		
//        //Creer une liste avec les forbiden values
//		ArrayList<Integer> forbidenVals= new ArrayList<Integer>();
//		
//		forbidenVals.add(0);
//		forbidenVals.add(exclude);
//		if(exclude % 2 == 1)
//		   forbidenVals.add(exclude+1);
//		
//		for(int j: mesValeurs)
//		{
//			if(!forbidenVals.contains(j))
//				{
//				textD= textD + " "+j;
//				forbidenVals.add(j);
//				nbValues++;
//				}
//		}
//		
//	//	System.out.println("D:"+exclude+" values="+textD);
//		
//		nbValuesD= new Attribute("nbValues", ""+nbValues);
//		domaine.setAttribute(nbValuesD);
//		
//		domaine.setText(textD);
//		
//		domains.addContent(domaine);
//		nbDoms++;
//	}

	private void createDomain(String domainName,int lower, int upper, int exclude, int nb)
	{
		Element domaine = new Element("domain");
		Attribute name= new Attribute("name", domainName);
		domaine.setAttribute(name);
		Attribute nbValuesD;
		String textD= "";
		int nbValues=0;
		
		Uniform unif= new Uniform(1, nodes);
		
		
        //Creer une liste avec les forbiden values
		ArrayList<Integer> forbidenVals= new ArrayList<Integer>();
		forbidenVals.add(0);
		forbidenVals.add(exclude);
		if(exclude % 2 == 1)
		   forbidenVals.add(exclude+1);
		else
		   forbidenVals.add(exclude-1);
		
		
		int i=0;
		
		while(i < nb)
		{
			//Take a random value from pickDomain
			int rVal= unif.generate(1).get(0);
			
			//Is rVal consistent
			if(!forbidenVals.contains(rVal))
				{
				   //Add it to the domain
				   textD= textD + " "+rVal;
				   //Update pickDomain
				   pickDomain.remove((Object) rVal);
				   forbidenVals.add(rVal);
				   //Increment i
				   i++;
				   nbValues++;
				}
			
		}
	
		nbValuesD= new Attribute("nbValues", ""+nbValues);
		domaine.setAttribute(nbValuesD);
		
		domaine.setText(textD);
		
		domains.addContent(domaine);
		nbDoms++;
	}
	
	private void createDomain(String domainName, int lower, int upper, int exclude) {
		// TODO Auto-generated method stub
		Element domaine= new Element("domain");
		Attribute n,n2;
		int a=lower, b=exclude-1, c, d=upper;
		
		if(exclude % 2 == 1)
			c=exclude+2;
		else
			c=exclude+1;
		
		String v="";
		n=new Attribute("name", domainName);
		domaine.setAttribute(n);
		
		if(a>b)
			v=c+".."+d;
		else if (a==b)
			v=a+" "+c+".."+d;
		else if (c==d)
			v=a+".."+b+" "+c;
		else if (c>d)
		    v=a+".."+b;
		else
			v=a+".."+b+" "+c+".."+d;
		
		domaine.setText(v);
		n2=new Attribute("nbValues", ""+(d-a));
		domaine.setAttribute(n2);
		domains.addContent(domaine);
		nbDoms++;
		
	}

	public Document GenerateXCSP(String file)
	{
	
		genVars();
	
		
    
		//Nombre de variables, de domaines, ... etc
		Attribute nbvars= new Attribute("nbVariables", ""+nbVars);
		Attribute nbdom= new Attribute("nbDomains", ""+nbDoms);
		Attribute nbpre= new Attribute("nbPredicates", ""+nbPre);
		Attribute nbrel= new Attribute("nbRelations", ""+nbRel);
		Attribute nbcons= new Attribute("nbConstraints", ""+nbCons);
		domains.setAttribute(nbdom);
		variables.setAttribute(nbvars);
		predicates.setAttribute(nbpre);
		relations.setAttribute(nbrel);
		constraints.setAttribute(nbcons);
		
		//Ajout des Variables, des domaines, des contraintes au XML
		instance.addContent(domains);
		instance.addContent(variables);
		instance.addContent(relations);
		instance.addContent(predicates);
		instance.addContent(constraints);
		
		//Sauver le XML
		saveXML(getXCSPinstance(), file);
		return getXCSPinstance();
	}
	
	public void saveXML(Document XCSP,String file)
	{
		
		 try
		   {
		      //On utilise ici un affichage classique avec getPrettyFormat()
		      XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
		      //Remarquez qu'il suffit simplement de créer une instance de FileOutputStream
		      //avec en argument le nom du fichier pour effectuer la sérialisation.
		      sortie.output(XCSP, new FileOutputStream(file));
		   }
		   catch (java.io.IOException e){}
		
	}
	
	public static org.jdom2.Document getXCSPinstance() {
		return XCSPinstance;
	}

	public static void setXCSPinstance(org.jdom2.Document xCSPinstance) {
		XCSPinstance = xCSPinstance;
	}
	
}
