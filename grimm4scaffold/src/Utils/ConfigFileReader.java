package Utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Hashtable;

import org.omg.CORBA.portable.InputStream;

import Proba.*;


public class ConfigFileReader {

	
	private int FeatureBound=0;
	private int RefBound=0;
	private String configFilePath;
	private ArrayList<String> content;
	private Hashtable<String,String> featuresDomaines;
	private Generator weightDistrib;
	private Generator DegreeDistrib; 
	private int edges=0;
	private int vertex=0;
	
	
	public ConfigFileReader(String configFilePath) {
		// TODO Auto-generated constructor stub
		this.configFilePath = configFilePath;
	}

	@SuppressWarnings("deprecation")
	public void read() throws IOException {
		
		ArrayList<String> content= new ArrayList<String>();
		
		File z= new File(configFilePath);
		FileInputStream zz= new FileInputStream(z);
		BufferedInputStream zzz= new BufferedInputStream(zz);
		
		DataInputStream reader = new DataInputStream(zzz);
		
		String line="";
		
		while(reader.available()!=0)
		{
			line= reader.readLine();
			
			if (line.startsWith("RefsBound="))
			{
				this.RefBound= Integer.parseInt(line.substring(line.lastIndexOf("=")+1));
			}
			else if (line.startsWith("Edge/weight->"))
			{
				this.weightDistrib= setWeightDistrib(line.substring(line.lastIndexOf(">")+1));
			}
			else if (line.startsWith("Degree->"))
			{
				this.DegreeDistrib= setDegreedDistrib(line.substring(line.lastIndexOf(">")+1));
			}
			else if(!line.startsWith("%"))
			{
				content.add(line);
				//System.out.println(line);
				
				if(line.startsWith("Edge="))
					this.edges= Integer.parseInt(line.substring(line.indexOf("=")+1));
				
				if(line.startsWith("Vertex="))
					this.vertex= Integer.parseInt(line.substring(line.indexOf("=")+1));
				
			}
		}
		
		this.content = content;
		
		reader.close();
		zzz.close();
		zz.close();
		
	}
	
	public int getEdges()
	{
		return this.edges;
	}

	public int getVertices()
	{
		return this.vertex;
	}

	
	public ArrayList<String> getContent() {
		return content;
	}

	public int getRefsBound() {
		// TODO Auto-generated method stub
		return RefBound;
	}
	
	private Generator setWeightDistrib(String s)
	{
		Generator gene;
		String s2= s.substring(0, s.indexOf('('));
		
		switch (s2){
			case "Norm":{
							double mu= Double.parseDouble(s.substring(s.indexOf('(')+1,s.indexOf(',')));
							double sigma= Double.parseDouble(s.substring(s.indexOf(',')+1,s.indexOf(')')));
							gene = new LoiNormale(mu, sigma);
							//System.out.println("Norm   mu="+ mu+ "  sigma="+sigma);
							//System.out.println("Loi Norm pour Weight");
							
						}
			break;
			case "Expo":{
							double mu= Double.parseDouble(s.substring(s.indexOf('(')+1,s.indexOf(')')));
							gene = new LoiExponentielle(mu);
							//System.out.println("Expo   mu="+ mu);
							//System.out.println("Loi Expo pour weight");	
						}
			break;
			case "Unif":{
							int a= Integer.parseInt(s.substring(s.indexOf('(')+1,s.indexOf(',')));
							int b= Integer.parseInt(s.substring(s.indexOf(',')+1,s.indexOf(')')));
							gene= new Uniform(a, b);
							//System.out.println("Unif   a="+ a+ "  b="+b);
							//System.out.println("Loi Unif pour weight");
							
					    }
			break;
			default: {
						gene = new DefaultDistrib();
					}
			break;
		
		}
		
		return gene;
		
	}
	
	private Generator setDegreedDistrib(String s)
	{

		Generator gene;
		String s2= s.substring(0, s.indexOf('('));
		switch (s2){
			case "Norm":{
							double mu= Double.parseDouble(s.substring(s.indexOf('(')+1,s.indexOf(',')));
							double sigma= Double.parseDouble(s.substring(s.indexOf(',')+1,s.indexOf(')')));
							gene = new LoiNormale(mu, sigma);
							//System.out.println("Norm   mu="+ mu+ "  sigma="+sigma);
							//System.out.println("Loi Norm pour Degree");
							
						}
			break;
			case "Expo":{
							double mu= Double.parseDouble(s.substring(s.indexOf('(')+1,s.indexOf(')')));
							gene = new LoiExponentielle(mu);
							//System.out.println("Expo   mu="+ mu);
							//System.out.println("Loi Expo pour weight");	
						}
			break;
			case "Unif":{
							int a= Integer.parseInt(s.substring(s.indexOf('(')+1,s.indexOf(',')));
							int b= Integer.parseInt(s.substring(s.indexOf(',')+1,s.indexOf(')')));
							gene= new Uniform(a, b);
							//System.out.println("Unif   a="+ a+ "  b="+b);
							//System.out.println("Loi Unif pour weight");
					    }
			break;
			default: {
						gene = new DefaultDistrib();
				//		System.out.println("Loi Default pour "+refName);
						
					}
			break;
		
		}
		
		return gene;
		
	}
	
	public Generator getWeightDistrib()
	{
		return this.weightDistrib;
	}
	
	public Generator getDegreedDistrib()
	{
		return this.DegreeDistrib;
	}
	

}
