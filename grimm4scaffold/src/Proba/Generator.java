package Proba;
import java.util.ArrayList;


public abstract class Generator {
	
	protected double uniforme;

	//Generate a set of values
	public ArrayList<Integer> generate(int n)
	{
		return null;
	}
	
	public static void prettyPrint(ArrayList<Integer> vals)
	{
		System.out.print("ex=c(");
		for(int i:vals)
		{
			System.out.print(i+",");
		}
		System.out.print(")");
	}
	
}
