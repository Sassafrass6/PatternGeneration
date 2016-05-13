import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.File;

class ImageWriter {
	
	static double[][] red;
	static double[][] green;
	static double[][] blue;
	//static double[][] grayScale;
	
	private static double capPixelValue ( double[][] c, int x, int y ) {
		/* Enforce condition ( 0 <= c[x][y] <= 1 ) */
		return ( c[x][y] > 1.0 ) ? 1.0 : ( ( c[x][y] < 0.0 ) ? 0.0 : c[x][y] );
	}
	
	private static void addColorArrays ( double[][] r, double [][] g, double[][] b ) {
		red = r;
		green = g;
		blue = b;
		//grayScale = g;
	}
	
	private static void writeImageRGB ( ) {
		BufferedImage image = new BufferedImage(red.length, red[0].length, BufferedImage.TYPE_INT_RGB);
		/* Iterate over all x & y coordinates in the .png file. */
		for ( int y = 0 ; y < red[0].length ; ++y ) {
			for ( int x = 0 ; x < red.length ; ++x ) {
				red[x][y] = capPixelValue(red, x, y);
				green[x][y] = capPixelValue(green, x, y);
				blue[x][y] = capPixelValue(blue, x, y);
				Color col = new Color((float)red[x][y], (float)green[x][y], (float)blue[x][y]); 
				image.setRGB(x, y, col.getRGB());
			}
		}
		try {
			/* Retrieve image */
			/* TODO: CREATE UNIQUE FILENAMES */
			File outputfile = new File("Images/saved.png");
			outputfile.createNewFile();

			ImageIO.write(image, "png", outputfile);
		}
		catch ( IOException e ) {
			/* Blank catches are bad */
			throw new RuntimeException("PoorDesign");
		}
	}
	
	public static void createImage ( double[][] r, double[][] g, double[][] b ) {
		addColorArrays(r, g, b);
		writeImageRGB();
	}
}