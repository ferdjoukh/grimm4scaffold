package Proba;
import java.util.ArrayList;
import java.util.Random;


public class LoiNormale extends Generator {

	private double mu;
	private double sigma;
	
	public LoiNormale(double m,double si) {
		mu = m;
		sigma= si;
	}
	
	@Override
	public ArrayList<Integer> generate(int n)
	{
		ArrayList<Integer> vals= new ArrayList<Integer>();
		double i,uni;
		Random r= new Random();
		
		for(int j=1;j<n;j++)
		{
	
			uni= r.nextGaussian();
		
			i= mu + uni*sigma;
		
			int k=(int) Math.round(i);
			if (k<0)
				vals.add(0);
			else
				vals.add(k);
		}
		
		return vals;
				
	}
	
}
