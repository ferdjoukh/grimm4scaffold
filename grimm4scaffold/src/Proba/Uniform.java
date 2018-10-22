package Proba;
import java.util.ArrayList;


public class Uniform extends Generator{

	private int min;
	private int max;
	private int n;
	
	public Uniform(int min,int max){
		this.min = min;
		this.max = max;
		n= max-min+1;
	}
	
	@Override
	public ArrayList<Integer> generate(int z)
	{
		ArrayList<Integer> vals= new ArrayList<Integer>();
		
		double i;
		
		for(int j=1;j<=z;j++)
		{
		
			uniforme = Math.random();
			i= uniforme*this.n+min-1;
			int k=(int) Math.round(i);
			if (k<0)
				vals.add(0);
			else
				vals.add(k);
		}
		
		return vals;
	}
	
}
