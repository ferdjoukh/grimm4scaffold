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

	
	private String mm;
	private String rootClass;
	private ModelReader modelReader;
	
	
	public ConfigFileGenerator(String mm, String rootClass)
	{
		this.mm = mm;
		this.rootClass = rootClass;
		modelReader = new ModelReader(mm, rootClass, 2, 2);
	}
	
	public void generate() throws IOException
	{
		String filePath= rootClass+".grimm";
		new File(rootClass).mkdir();
	
		PrintWriter ecrivain =  new PrintWriter(new BufferedWriter(new FileWriter(rootClass+"/"+filePath)));
		
		ecrivain.write("%This is a configuration file for Grimm Tool \n");
		ecrivain.write("%Please do not change the ordering or the name of any element !\n");
		ecrivain.write("%Put a numerical value instead of n, min, max, lambda, ... \n");
		
		ecrivain.write("% \n");
		ecrivain.write("% \n");
		ecrivain.write("%-------------------------------------------------------------\n");
		ecrivain.write("%Number of instances for Classes \n");
		ecrivain.write("%-------------------------------------------------------------\n");
		ecrivain.write("%-------------------------------------------------------------\n");
				
		ArrayList<EClass> cls= new ArrayList<EClass>();
		cls= (ArrayList<EClass>) modelReader.getClasses();
		for(EClass c: cls)
		{
			String name= c.getName();
			if (name.compareTo(rootClass)!=0)
			ecrivain.write(name+"=n\n");
		}
		ecrivain.write("%-------------------------------------------------------------\n");
		ecrivain.write("%-------------------------------------------------------------\n");
		ecrivain.write("%Domains of the features \n");
		ecrivain.write("%Please change min and max values\n");
		ecrivain.write("%To choose another distribution: Unif(a,b), Norm(mu,sigma), Expo(mu) \n"
				+ "-------------------------------------------------------------\n");
		ecrivain.write("%-------------------------------------------------------------\n");
		
		ecrivain.write("Edge/weight->Unif(min,max)\n");
		
		ecrivain.write("%-------------------------------------------------------------\n");
		ecrivain.write("%-------------------------------------------------------------\n");
		ecrivain.write("%Distributions of Degrees \n");
		ecrivain.write("%To choose another distribution: Unif(a,b), Norm(mu,sigma), Expo(mu)\n");
		ecrivain.write("%-------------------------------------------------------------\n");
		ecrivain.write("%-------------------------------------------------------------\n");
		
		ecrivain.write("Degree->Expo(mu)\n");
		
		ecrivain.write("%-------------------------------------------------------------\n");
		ecrivain.write("%-------------------------------------------------------------\n");
		ecrivain.write("%Some others \n");
		ecrivain.write("%-------------------------------------------------------------\n");
		ecrivain.write("%-------------------------------------------------------------\n");
		
		ecrivain.write("RefsBound=0\n");
				
		ecrivain.write("%-------------------------------------------------------------\n");
		
		
		ecrivain.close();
		System.out.println("|\t--Configuration file: \""+rootClass+"/"+ filePath + "\" was generated ! =) \n|");
		
	}
}
