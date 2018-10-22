package Proba;
import java.util.ArrayList;


public class test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		LoiExponentielle expo = new LoiExponentielle(5);
		
		ArrayList<Integer> vals= expo.generate(50);
		
		expo.prettyPrint(vals);
		
		System.out.println();
		
		LoiNormale norm = new LoiNormale(5, 1);
		
		ArrayList<Integer> vals1= norm.generate(50);
		
		norm.prettyPrint(vals1);
		
		Uniform unif= new Uniform(12, 20);
		
		ArrayList<Integer> vals2= unif.generate(10);
		
		System.out.println();
		
		
		unif.prettyPrint(vals2);
		
		
		
	}

}
