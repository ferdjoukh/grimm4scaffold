import java.io.File;
import java.io.IOException;
import java.util.*;

import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.resource.Resource;

import Utils.ConfigFileGenerator;
import Utils.GenXCSP;
import Utils.ModelReader;
import Utils.Reconstruct;
import Utils.CSP2dot;


public class GenScaffoldGraph {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
         
	  
		/***
         *     Reconstruct
		 *     First parameter : The matamodel
		 *     Second Parameter: The metamodel root
		 *     Third Parameter:  CSP XML file path
		 *     Fourth Parameter: XMI Model file path
		 *        
		 *        
		 *     CSP2dot
		 *     First parameter : The matamodel
		 *     Second Parameter: The metamodel root
		 *     Third Parameter:  CSP XML file path
		 *     Fourth Parameter: Dot and Pdf Model file path   
		 *   
		 *      CallCSPGenerator
		 *      First parameter : Minimum Number of Instances for each class
		 *      Second Parameter: Maximum Number of Instances for each class
		 *      Third Parameter:  Maximum Number of Instances for each reference
		 *      Third Parameter:  Break or not symmetries {0,1}
		 *    
		 * 
		 */
		String mm = "ScaffoldGraph.ecore";
		String rootClass = "Graph";
		int sol=1;
		int mg=0;
		int nodes=0;
		double density=0;
		double lambda=0;
		int edges=0;
		
		
		
		
		/////////////////////////////////////////////////
		////////////////////////////////////////////////
		//////////////////////////////
		/////////////////////////////      
		//
		//Put help if no parameters
		//
		///////////////////////////
		//////////////////////////
		/////////////////////////
		
		if(args.length==0)
		{
			//System.out.println("To run Grimm, You need a command line like:");
			System.out.println("\nLunch\tjava -jar grimm4graph.jar for help");
			
			
			System.out.println("\n\nMondatory Parameters for Graph generation:");
			System.out.println("\t-n=value, value is the desired number of nodes.");
			System.out.println("\t-e=value, value is the desired number of edges.");
			System.out.println("\t or");
			System.out.println("\t-d=value, value is the desired graph density.");
			System.out.println("\t or");
			System.out.println("\t-l=value, value is the desired lambda for the probability distribution.");
						
			
			System.out.println("\n\nOptional Parameters for model generation:");
			System.out.println("\t-sol=x, generate the xth solution (default =1).");
			System.out.println("\t-mg={0,1}, MutliGraph option (default =0).");
			
			
			
			System.out.println("\n\nExamples:");
			System.out.println("\tjava -jar grimm4graph.jar");
			System.out.println("\tjava -jar grimm4graph.jar -n=50 -d=0.1");
			System.out.println("\tjava -jar grimm4graph.jar -n=50 -d=0.1 -sol=10 -mg=1");
			
			
			System.out.println("");
		}
		else
		{		
			
			String start;
			
			for( int i=0; i<args.length;i++)
			{
				//System.out.println("param "+i+" = "+args[i]);
				start= args[i].substring(0, args[i].lastIndexOf('=')+1);
				switch(start)
				{
				case "-sol=": {
								
								sol=	Integer.parseInt(args[i].substring(5));
								if (sol<=0)
									sol=1;
						//		System.out.println("Solution number ="+sol);	
							 }
						break;
				
				case "-mg=": {
								mg=	Integer.parseInt(args[i].substring(4));
					//			System.out.println("How many Solutions ="+ Nsol);	
								if(mg<0 || mg > 1)
								   mg=0;
	 			 			 }
						break;	
						
				case "-n=":{
					           nodes= Integer.parseInt(args[i].substring(3));
					           
					           if(nodes==0)
					        	   System.out.println("Invlid number of nodes");
				
						  }
						break;
						
				case "-d=":{
					        density= Double.parseDouble(args[i].substring(3));	
				}
				       break;
				
				case "-l=":{
					       lambda= Double.parseDouble(args[i].substring(3));
					       
				}
						break;
				 
				case "-e=":{
						   edges= Integer.parseInt(args[i].substring(3));
						   
				}
						break;
				default: System.out.println("Invalid Parameter: "+args[i]+" !!");	
				}
			}
			
			//Computing density, lambda and edges
			if(density !=0)
			{
				//density is the given parameter
				lambda = density * (nodes - 1);
				edges  = (int) (nodes * (nodes -1) * density * 0.5);
				
			}
			else if (lambda != 0)
			{
				//lambda is the given parameter
				density = lambda / (nodes - 1);
				edges  = (int) (nodes * (nodes -1) * density * 0.5);		
			}
			else if (edges > 0)
			{
				//edges is the given parameter
				edges= edges - (nodes /2);
				density = (double) (2 * edges) / (double) (nodes * (nodes -1));
				lambda = density * (nodes - 1) / 2;
			}
			
			new File(rootClass).mkdir();
			
			System.out.println("Generation of Scaffold Graph (nodes="+ nodes + " edges="+ edges + 
					" lambda="+ lambda +", density="+density+")");
			   	
			CSP2dot rec=new CSP2dot(mm,rootClass,rootClass+"/"+rootClass+".xml");
			rec.CallCSPGenrator2(nodes,edges,density,lambda, mg, sol);	
	    }	 
	}
}
