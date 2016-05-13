import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.*;

class PatGen {
	
	/* Power determines how many pixels large the .png will be. */
	static int power = 9;
	static double scale = 1.0 / Math.pow(2, power);
	static double resolution = Math.pow(2, power+2);
	
	static double[][] red = new double[(int)resolution][(int)resolution];
	static double[][] green = new double[(int)resolution][(int)resolution];
	//static double[][] blue = new double[(int)resolution][(int)resolution];
	//static double[][] grayScale = new double[(int)resolution][(int)resolution];
	
	private static class ImageWriter {
		
		static double[][] red;
		static double[][] green;
		//static double[][] blue;
		//static double[][] grayScale;
		
		private static double capPixelValue ( double[][] c, int x, int y ) {
			/* Enforce condition ( 0 <= c[x][y] <= 1 ) */
			return ( c[x][y] > 1.0 ) ? 1.0 : ( ( c[x][y] < 0.0 ) ? 0.0 : c[x][y] );
		}
		
		private static void addColorArrays ( double[][] r, double [][] g ) { //, double[][] b, double[][] g ) {
			red = r;
			green = g;
			//blue = b;
			//grayScale = g;
		}
		
		private static void writeImageRGB ( ) {
			BufferedImage image = new BufferedImage(red.length, red[0].length, BufferedImage.TYPE_INT_RGB);
			/* Iterate over all x & y coordinates in the .png file. */
			for ( int y = 0 ; y < red[0].length ; ++y ) {
				for ( int x = 0 ; x < red.length ; ++x ) {
					red[x][y] = capPixelValue(red, x, y);
					green[x][y] = capPixelValue(green, x, y);
					Color col = new Color((float)red[x][y], (float)green[x][y], 0); 
					image.setRGB(x, y, col.getRGB());
				}
			}
			try {
				/* Retrieve image */
				/* TODO: CREATE UNIQUE FILENAMES */
				File outputfile = new File("saved.png");
				outputfile.createNewFile();

				ImageIO.write(image, "png", outputfile);
			}
			catch ( IOException e ) {
				/* Blank catches are bad */
				throw new RuntimeException("PoorDesign");
			}
		}
		
		public static void createImage ( double[][] r, double[][] g ) { //, double[][] b ) {
			addColorArrays(r, g);
			writeImageRGB();
		}
	}
	
	protected static void printEquation ( Equation root, String arg ) {
		System.out.println(root.printNext(arg));
	}
	
	public static void main ( String[] args ) {
		Random rand = new Random();
		Equation rootRed, rootGreen; //, rootBlue;
		Equation prevR, prevG; //, prevB;
		
		rootRed = prevR = new Equation(rand);
		rootGreen = prevG = new Equation(rand);
		//Equation rootRed = new Equation(rand);		

		int numIts = rand.nextInt(3) + 2;
		for ( int i = 0 ; i < numIts ; i++ ) {
			prevR.addChild(new Equation(rand));
			prevG.addChild(new Equation(rand));
			prevR = prevR.getChild();
			prevG = prevG.getChild();
		}

		/* REFACTOR DOUBLES! */
		for ( double i = 0 ; i < resolution ; i += 1.0 ) {
			for ( double j = 0 ; j < resolution ; j += 1.0 ) {
				double ir = i*scale;
				double jr = j*scale;
				red[(int)i][(int)j] = rootRed.constructEquation(ir, jr, jr*ir);
				green[(int)i][(int)j] = rootGreen.constructEquation(ir, jr, jr*ir);
			}
		}
		
		ImageWriter.createImage(red, green);
	}
}