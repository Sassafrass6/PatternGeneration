import java.util.*;

class Equation {
	
	public enum Ops {
		MUL, DIV
	}
	public enum Trig {
		COS, SIN
	}
	public enum Vars {
		VX, VY, VXY, VXOY, VYOX, VPI, NONE
	}
	public long seed;
	
	Ops[] ops;
	Trig[] trigs;
	Vars[] vars;
	private Equation child;
	private Random rnd;
	private String arg;
	private Vars deepArgStr;
	private int deepArg;
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
		deepArg = getRandomIndex(rnd, 3);
		deepArgStr = vars[deepArg];
	}
	
	protected int getRandomIndex ( Random rnd, int len ) {
		int ret;
		if ( (ret = rnd.nextInt()%len) < 0 )
			ret += len;
		return ret;
	}
	
	public double constructEquation ( double x, double y ) {
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
				break;
			case VXOY:
				if ( y == 0.0 )
					y = 0.001;
				mulVar = x / y;
				break;
			case VYOX:
				if ( x == 0.0 )
					x = 0.001;
				mulVar = y / x;
				break;
			case VPI:
				mulVar = Math.PI;
				break;
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
				mulFact = 1.0 / mulVar;
				break;
			default: return error+1;
		}
		switch ( trig ) {
			case SIN:
				if ( endOfLineage ) {
					switch ( deepArg ) {
						case 0:
							mulTrig = Math.sin(x);
							break;
						case 1:
							mulTrig = Math.sin(y);
							break;
						default:
							mulTrig = Math.sin(x*y);
							break;
					}
				}
				else
					mulTrig = Math.sin(child.constructEquation(x, y));
				break;
			case COS:
				if ( endOfLineage ) {
					switch ( deepArg ) {
						case 0:
							mulTrig = Math.sin(x);
							break;
						case 1:
							mulTrig = Math.sin(y);
							break;
						default:
							mulTrig = Math.sin(x*y);
							break;
					}
				}
				else
					mulTrig = Math.cos(child.constructEquation(x, y));
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
	
	public String printNext ( ) {
		if ( child == null ) 
			return "("+var+" "+op+" "+trig+"("+deepArgStr+")"+")";
		else
			return "("+var+" "+op+" "+trig+"("+child.printNext()+")"+")";
	}
}