import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import javax.swing.text.*;
import java.util.*;
import javax.crypto.*;

class Equation {
	
	public enum Ops {
		MUL, DIV
	}
	public enum Trig {
		COS, SIN
	}
	public enum Vars {
		VX, VY, VXY, NONE
	}
	public long seed;
	
	Ops[] ops;
	Trig[] trigs;
	Vars[] vars;
	private Equation child;
	private Random rnd;
	private String arg;
	private Ops op;
	private Trig trig;
	private Vars var;
	
	public Equation ( Random rand ) {
		rnd = rand;
		ops = Ops.values();
		trigs = Trig.values();
		vars = Vars.values();
		
		child = null;
		
		/* Make random modifiers. 
		   Get random modifier. If indecies are negative add the length. */
		op = ops[getRandomIndex(rnd, ops.length)];
		trig = trigs[getRandomIndex(rnd, trigs.length)];
		var = vars[getRandomIndex(rnd, vars.length)];
	}
	
	protected int getRandomIndex ( Random rnd, int len ) {
		int ret;
		if ( (ret = rnd.nextInt()%len) < 0 )
			ret += len;
		return ret;
	}
	
	public double constructEquation ( double x, double y, double deepArg ) {
		int toReturn;
		double error = 0.0;
		boolean endOfLineage = false;
		double mulVar, mulFact, mulTrig;
		if ( child == null )
			endOfLineage = true;
		switch ( var ) {
			case VX:
				mulVar = x;
				break;
			case VY:
				mulVar = y;
				break;
			case VXY:
				mulVar = x * y;
			case NONE:
				mulVar = 1.0;
				break;
			default: return error;
		}
		switch ( op ) {
			case MUL:
				mulFact = mulVar;
				break;
			case DIV:
				mulFact = mulVar;
				break;
			default: return error+1;
		}
		switch ( trig ) {
			case SIN:
				if ( endOfLineage )
					mulTrig = Math.sin(deepArg);
				else
					mulTrig = Math.sin(child.constructEquation(x, y, deepArg));
				break;
			case COS:
				if ( endOfLineage )
					mulTrig = Math.cos(deepArg);
				else
					mulTrig = Math.cos(child.constructEquation(x, y, deepArg));
				break;
			default: return error+2;
		}
		return mulVar * mulFact * mulTrig;			
	}
	
	public Equation getChild ( ) {
		return child;
	}
	
	public void addChild ( Equation ch ) {
		child = ch;
	}
	
	public String printNext ( String deepArg ) {
		if ( child == null ) 
			return "("+var+" "+op+" "+trig+"("+deepArg+")"+")";
		else
			return "("+var+" "+op+" "+trig+"("+child.printNext(deepArg)+")"+")";
	}
}

class PatGen {
	
	private static class ImageWriter {
			public static void greyWriteImage(double[][] red, double[][] green){
					BufferedImage image = new BufferedImage(red.length, red[0].length, BufferedImage.TYPE_INT_RGB);
					for ( int y = 0 ; y < red[0].length ; y++ ) {
						for ( int x = 0 ; x < red.length ; x++ ) {
							if ( red[x][y] > 1 ) {
								red[x][y]=1;
							}
							else if ( red[x][y] < 0 ) {
								red[x][y] = 0;
							}
							if ( green[x][y] > 1 ) {
								green[x][y]=1;
							}
							else if ( green[x][y] < 0 ) {
								green[x][y] = 0;
							}
							Color col = new Color((float)red[x][y], (float)green[x][y], 0); 
							image.setRGB(x, y, col.getRGB());
						}
					}
					try {
						// retrieve image
						File outputfile = new File("saved.png");
						outputfile.createNewFile();

						ImageIO.write(image, "png", outputfile);
					}
					catch ( IOException e ) {
						//o no! Blank catches are bad
						throw new RuntimeException("PoorDesign");
					}
			}
	}
	
	public static void printEquation ( Equation root, String arg ) {
		System.out.println(root.printNext(arg));
	}
	
	public static void main ( String[] args ) {
		Random rand = new Random();
		Equation rootRed = new Equation(rand);
		Equation rootGreen = new Equation(rand);
		//Equation rootRed = new Equation(rand);
		
		int power = 9;
		
		double scale = 1.0/Math.pow(2, power);
		double resolution = Math.pow(2, power+2);
		double[][] red = new double[(int)resolution][(int)resolution];
		double[][] green = new double[(int)resolution][(int)resolution];

		Equation prevR = rootRed;
		Equation prevG = rootGreen;
		int numIts = rand.nextInt(3) + 2;
		for ( int i = 0 ; i < numIts ; i++ ) {
			prevR.addChild(new Equation(rand));
			prevG.addChild(new Equation(rand));
			prevR = prevR.getChild();
			prevG = prevG.getChild();
		}

		
		for ( double i = 0 ; i < resolution ; i += 1.0 ) {
			for ( double j = 0 ; j < resolution ; j += 1.0 ) {
				double ir = i*scale;
				double jr = j*scale;
				red[(int)i][(int)j] = rootRed.constructEquation(ir, jr, jr*ir);
				green[(int)i][(int)j] = rootGreen.constructEquation(ir, jr, jr*ir);
				//System.out.println(grayscale[(int)i][(int)j]);
			}
		}
		
		ImageWriter.greyWriteImage(red, green);
	}
}