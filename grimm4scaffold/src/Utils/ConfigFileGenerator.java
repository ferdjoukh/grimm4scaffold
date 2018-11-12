package Utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;

public class ConfigFileGenerator {

	
	private String filePath; 
	private int vertices;
	private int edges;
	private int minWeight;
	private int maxWeight;
	private int refBound;
	
	
	public ConfigFileGenerator(String filePath, int vertices, int edges, int minWeight, int maxWeight, int refBound)
	{
		this.filePath= filePath;
		this.vertices= vertices;
		this.edges= edges;
		this.maxWeight=maxWeight;
		this.minWeight=minWeight;
		this.refBound= refBound;
	}
	
	public void generate() throws IOException
	{
		
		PrintWriter ecrivain =  new PrintWriter(new BufferedWriter(new FileWriter(filePath)));
		
		ecrivain.write("%This is a configuration file for Grimm Tool \n" + 
				"%Please do not change the ordering or the name of any element !\n" + 
				"%Put a numerical value instead of 0, lower, upper, a and z \n" + 
				"% \n" + 
				"% \n" + 
				"%-------------------------------------------------------------\n" + 
				"% Number of instances for Classes \n" + 
				"%-------------------------------------------------------------\n" + 
				"%-------------------------------------------------------------\n");
		
		ecrivain.write("Vertex="+vertices+"\n");
		ecrivain.write("Edge="+edges+"\n");
		
		ecrivain.write("%-------------------------------------------------------------\n" + 
				"%-------------------------------------------------------------\n" + 
				"%Domains of the features \n" + 
				"%-------------------------------------------------------------\n" + 
				"%-------------------------------------------------------------\n" + 
				"");
		
		ecrivain.write("Edge/weight="+minWeight+".."+maxWeight+"\n");
		
		ecrivain.write("%-------------------------------------------------------------\n" + 
				"%-------------------------------------------------------------\n" + 
				"%Some others \n" + 
				"%-------------------------------------------------------------\n" + 
				"%-------------------------------------------------------------\n" + 
				"%-------------------------------------------------------------\n");
			
		ecrivain.write("RefsBound=" +refBound+"\n" );
		ecrivain.write("FeaturesBound=0\n");
		
		ecrivain.write("%-------------------------------------------------------------\n");
		
		ecrivain.close();
		
	}
}
