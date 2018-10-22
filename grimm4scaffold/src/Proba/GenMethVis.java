package Proba;

import java.util.ArrayList;

public class GenMethVis extends Generator {

	private double pub;
	private double pri;
	private double pac;
	private double pro;
	
	public GenMethVis(double pubb,double prii,double pacc,double proo){
		pub= pubb/100;
		pri= prii/100;
		pac= pacc/100;
		pro= proo/100;
		
	} 
	
	@Override
	public ArrayList<Integer> generate(int n)
	{
		ArrayList<Integer> vals= new ArrayList<Integer>();
		int i;
		
		for(int j=1;j<=n;j++)
		{
		
		uniforme = Math.random();
		if(uniforme <= pub)
			i=1;
		else if(uniforme <= pri+pub)
			i=2;
		else if(uniforme <= pac+pri+pub)
			i=3;
		else
			i=4;
		vals.add(i);
		}
		
		return vals;
				
	}
	
}
