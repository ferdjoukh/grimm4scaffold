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
		
		String mm = "ScaffoldGraph.ecore";
		String rootClass = "Graph";
		int nodes=0;
		double density=0;
		double lambda=0;
		int edges=0;
		
		if(args.length==0)
		{
			//////////////////////////////////////////////////////
			//
			//    Show help
			//
			/////////////////////////////////////////////////////
			System.out.println("grimm4scaffold is a tool for the generation of scaffold graphs");
			System.out.println("");
			
			System.out.println("COMMAND");
			System.out.println("    java -jar grimm4scaffold.jar [options]");
			
			System.out.println("");
			System.out.println("OPTIONS");
			
			System.out.println("     n && (e || d || l)");
			
			System.out.println("");
			
			System.out.println("    -n=value : desired number of nodes.");
			System.out.println("    -e=value : desired number of edges.");
			System.out.println("    -d=value : desired graph density.");
			System.out.println("    -l=value : desired lambda for probability distribution.");
			
			System.out.println("");
			System.out.println("OUTPUT");
			
			System.out.println("    the tool generates 1 graph for each call");
			System.out.println("    for each graph: a .dot, .chr and .pdf files are generated");
			
			System.out.println("");
			System.out.println("EXAMPLES");
						
			System.out.println("    java -jar grimm4graph.jar");
			System.out.println("    java -jar grimm4graph.jar -n=50 -e=100");
			System.out.println("    java -jar grimm4graph.jar -n=50 -d=0.1");
		}
		else
		{		
			
			String start;
			
			for( int i=0; i<args.length;i++)
			{
				start= args[i].substring(0, args[i].lastIndexOf('=')+1);
				switch(start)
				{
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
			
			///////////////////////////////////////
			//
			//  Computing density, lambda and edges
			//
			///////////////////////////////////////
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
			   	
			CSP2dot csp2dot=new CSP2dot(mm,rootClass,rootClass+"/"+rootClass+".xml");
			csp2dot.CallCSPGenrator2(nodes,edges,density,lambda);	
	    }	 
	}
}
