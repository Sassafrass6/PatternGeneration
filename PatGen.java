import java.util.*;

class PatGen {
	
	/* Power determines how many pixels large the .png will be. */
	static int power = 10;
	static int resolution =(int)(Math.pow(2, power+1));
	static int offsetX = 0; //(int)(-resolution / 2);
	static int offsetY = 0; //(int)(-resolution / 2);
	
	static double scale = 1.7 / Math.pow(2, power);
	
	static double[][] red = new double[resolution][resolution];
	static double[][] green = new double[resolution][resolution];
	static double[][] blue = new double[resolution][resolution];
	//static double[][] grayScale = new double[(int)resolution][(int)resolution];
	
	protected static void printEquation ( Equation root, String arg ) {
		System.out.println(root.printNext());
	}
	
	public static void main ( String[] args ) {
		Random rand = new Random();
		Equation rootRed, rootGreen, rootBlue;
		Equation prevR, prevG, prevB;
		
		rootRed = prevR = new Equation(rand);
		rootGreen = prevG = new Equation(rand);
		rootBlue = prevB = new Equation(rand);		


		int numItsMax = 6;
		int numItsR = rand.nextInt(numItsMax);
		int numItsG = rand.nextInt(numItsMax);
		int numItsB = rand.nextInt(numItsMax);
		
		for ( int i = 0 ; i < numItsMax ; ++i ) {
			if ( i < numItsR ) {
				prevR.addChild(new Equation(rand));
				prevR = prevR.getChild();
			}
			if ( i < numItsG ) {
				prevG.addChild(new Equation(rand));
				prevG = prevG.getChild();
			}
			if ( i < numItsB ) {
				prevB.addChild(new Equation(rand));
				prevB = prevB.getChild();
			}
		}

		for ( int i = 0 ; i < resolution ; ++i ) {
			for ( int j = 0 ; j < resolution ; ++j ) {
				double ir = (i + offsetX) * scale;
				double jr = (j + offsetY) * scale;
				red[i][j] = rootRed.constructEquation(ir, jr);
				green[i][j] = rootGreen.constructEquation(ir, jr);
				blue[i][j] = rootBlue.constructEquation(ir, jr);
			}
		}
		printEquation(rootRed, "x");
		ImageWriter.createImage(red, green, blue);
	}
}