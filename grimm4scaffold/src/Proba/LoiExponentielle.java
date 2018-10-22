package Proba;
import java.util.ArrayList;


public class LoiExponentielle extends Generator {

	private double mu;
	private double lambda;
	
	public LoiExponentielle(double m) {
		mu = m;
		lambda= 1 / m;
	}
	
	@Override
	public ArrayList<Integer> generate(int n)
	{
		ArrayList<Integer> vals= new ArrayList<Integer>();
		double i;
		
		for(int j=1;j<n;j++)
		{
		
			uniforme = Math.random();
			i= -1/lambda * Math.log(uniforme);
		
			int k=(int) Math.round(i);
			if (k<1)
				vals.add(1);
			else
				vals.add(k);
		}
		
		return vals;
				
	}
}
