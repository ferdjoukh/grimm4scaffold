package Utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Proba.*;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.ocl.ParserException;

import Utils.OCL.OclConstraints;

public class CSP2dot {

	ModelReader r;
	String ModelFile;
	String root;
	String InstanceFile;
	int refB;
	ArrayList<Integer> sizes;
	ArrayList<Integer> sizesMin;
	private int maxDomains;
	int mg=0;
	GenXMLFile generation;
	int nodes;
	
	/***
	 * 
	 * @param ModelFile: .ECORE meta-model File
	 * @param racine: Root Class of this meta-model
	 * @param InstanceFile: .XML file path of the produced CSP instance
	 */
	public CSP2dot(String ModelFile, String racine,String InstanceFile){
		
		this.ModelFile = ModelFile;
		this.root = racine;
		this.InstanceFile = InstanceFile;
	}
	
	/***
	 * 
	 * @param nodes
	 * @param density 
	 * @param multi graph
	 * @param solution number
	 * @throws IOException 
	 */
	public void CallCSPGenrator2(int nodes, int edges,double density, double lambda, int mg, int sol) throws IOException
	{
		this.nodes=nodes;
		if(nodes % 2 == 1)
		{
			System.out.println("|\tFatal Error: Number of Vertices (="+nodes+") must be pair !");
		}
		else
		{
		
		this.mg=mg;
				
		long debut; double duree;
		debut=System.nanoTime();
		
		//Sa racine est Petrinet
	    generation= new GenXMLFile(nodes, edges, density, lambda);
		generation.GenerateXCSP(InstanceFile);
				
		duree=(System.nanoTime()-debut)/1000000;
		System.out.println("  [OK] XCSP file generated");
		System.out.println("  [OK] generation time = "+ duree/1000);
		
		///////////////////////////////////////////////////////////////
		//Ececuter le solveur
		////////////////////////////////////////////////////////////
		BufferedReader r;
		r=Execute(InstanceFile,sol);
		ArrayList<Integer> vals= new ArrayList<Integer>();
		vals=RValues(r);
		int vv=0;
						
		///////////////////////////////////////////////////////////////
		//Reconstruire une solution
		/////////////////////////////////////////////////////////////
		if(vals.size()!=0)
		{
			generateDot(vals);
		}
		}
		
	}
	
	/**
	 * 
	 * Call the CSP solver
	 * 
	 * @param Instancefile: CSP instance xml file
	 * @param sol: solutions number
	 * @return
	 */
	public BufferedReader Execute(String Instancefile, int sol){

		String cmd = "java -jar abssol.jar " + Instancefile +" -s="+ sol;
		System.out.println("  [OK] Abscon Solver is processing ...");
		
		Process p = null;
			try {
				p = Runtime.getRuntime().exec(cmd);
				BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			    
				return reader;
			} catch (IOException e) {
				e.printStackTrace();		
			}
			
	        return null;
	}
	
	public ArrayList<Integer> RValues(BufferedReader reader){
		ArrayList<Integer> vals= new ArrayList<Integer>();
		String line;
		String du = null;
		int found=0;
		
		try {
			while((line = reader.readLine()) != null) {
				//System.out.println(line);
				if(line.startsWith("s SATISFIABLE")) {	        
			    	
			    	found=1;
			    }
			    else  if(line.startsWith("v ")){
			   // 	System.out.println(line);
			    	int i=2;int varl=0;
			    	while(i<line.length())
			    	{
			    		varl=line.indexOf(" ", i);
			    		vals.add(Integer.parseInt((line.substring(i, varl))));
			    		i= varl+1;
			    	}
			    } 
			    else if(line.startsWith("   totalWckTime"))
			    {
			    	int kk=line.indexOf("CpuTime=");
			    	du="="+line.subSequence(kk+8, line.length());
			    }
			}
			if(found==0)
				System.out.println("  [PROBLEM] The CSP instance is unsatisfiable !! :(");
			else
			{
				System.out.println("  [OK] Resolution time" +du);
				
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    	
		}
		return vals;
	}
	
	
	public int domaineSum(int k)
	{
		int s=0;
		if (k==0)
			return 0;
		for(int i=0;i<=k-1;i++)
		{
			s+= sizes.get(i);
		}
		return s;
	}
	
	public int domaineSumMin(int k)
	{
		int s=0;
		if (k==0)
			return 0;
		for(int i=0;i<=k-1;i++)
		{
			s+= sizesMin.get(i);
		}
		return s;
	}
	
	public void generateDot(ArrayList<Integer> values) throws IOException
	{
		ArrayList<Integer> vals= values;
		int variable=0;
		PrintWriter ecrivain;
		
		ArrayList<String> references= new ArrayList<String>();
		String outputFileName = root+new Date().getTime()+"n"+nodes;
		
		//////////////////////////////////////
		// Create the Chromosome
		//////////////////////////////////////
		CSP2CHR(vals, outputFileName);
		
		ecrivain =  new PrintWriter(new BufferedWriter(new FileWriter(root+"/"+outputFileName +".dot")));
		ecrivain.write("Graph g{ \n");
		
		String AttrDots="";
		int lb=0,ub=0;
		
		///////////////////////////////////////////////////////////:
		//   Add possibility of entering this by user
		//
		//
		Uniform kimjongUn= new Uniform(1,50);
		
		
		ArrayList<Integer> myGeneratedWeights = null;
		myGeneratedWeights= kimjongUn.generate(generation.getEdges()+1);
		
		ArrayList<Integer> echantillon= generation.getEchantillon();
		/////////////////////////////////////////////////////////
		/////////////////
		//////////
		///////
		//  Créer les objets instances de classe et leurs attribut;
		for(int i=1; i<=generation.getNodes();i++)
		{
			//Add All the (2i+1,2i) edges
			if(i%2==1)
			{
				ecrivain.write(i+"--"+(i+1)+" [penwidth=10];\n");
				references.add(i+"-"+(i+1));
			}
			
			//Add the nodes
			ecrivain.write(i+";\n");
	   
			int sortant= echantillon.get(i-1)/2 + echantillon.get(i-1) % 2;   
			int weight;
			
			if(sortant!=0)
			{
			 int j=1;
			//Son degre sortant (de chaque noeud)
				while(j<=sortant && variable<generation.getEdges())
				{
					weight = kimjongUn.generate(1).get(0);
					
					if(!references.contains(i+"-"+vals.get(variable)) && !references.contains(vals.get(variable)+"-"+i))
					{
						ecrivain.write(i+"--"+vals.get(variable)+" [label=\""+weight+"\"] ;\n");
						references.add(i+"-"+vals.get(variable));
					}
					variable++;
					j++;
				}
			
				
			}
			
    	}
				//////////////////////////////////////////////////////////
				//// Choose A weight from the Randomly generated weights
				//////////////////////////////////////////////////////////
		//		
					
		
		
		ecrivain.write("} \n");
		ecrivain.close();
		
		
		////////////////////////////////////////////////
		/////////////////////////
		//////////
		////        Générer le pdf...
		
		String cmd = "dot -Tpdf "+root+"/"+outputFileName+".dot -o "+root+ "/"+outputFileName+".pdf";
		
		Process p = null;
		try {
			p = Runtime.getRuntime().exec(cmd);
			
			System.out.println("  [GENERATED] Scaffold dot file >> "+root+"/"+outputFileName +".dot");
			System.out.println("  [GENERATED] Scaffold pdf file >> "+root+"/"+outputFileName +".pdf");
		}
		catch(Exception e)
		{
			System.out.println("  [PROBLEM] GraphViz Software is not installed. So pdf file is not generated");
		}
		
	}
	
	/***
	 * This method generates a text file .chr that contains the 
	 *  chromosome of each generated graph
	 * 
	 * @param values: the ArrayList<Integer> that was given by the solver
	 * @param outputFileName: the name of generated .chr file
	 * @throws IOException
	 */
	public void CSP2CHR(ArrayList<Integer> values, String outputFileName) throws IOException{
		
		PrintWriter printwriter =  new PrintWriter(new BufferedWriter(new FileWriter(root+"/"+outputFileName +".chr")));
		printwriter.write(values.toString()+"\n");
		printwriter.close();
		
		System.out.println("  [GENERATED] Chromosome file >> "+root+"/"+outputFileName +".chr");
		
		
	}
}
