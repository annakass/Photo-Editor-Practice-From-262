import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Photo {
	
	//variables
	BufferedImage picture;
	int w; //width
	int h; //height
	double ratioW;
	double ratioH;
	int pixels;
	
	//constructor
	public Photo(String filename) throws IOException {
		picture = ImageIO.read(new File(filename));
		w = picture.getWidth();
		h = picture.getHeight();
		ratioW = ((double) w)/((double)h);
		ratioH = ((double) h)/((double)w);
		pixels = h * w;
	
	}
	//___________________PHOTO STUFF
	public void updateDimentions (int width, int height) {
		h = height;
		w = width;
		pixels = h * w;
		ratioW = ((double) w)/((double)h);
		ratioH = ((double) h)/((double)w);
	}
	
	public void ResizeByH (int height) {
		double temp = (height)/(ratioH);
		int estW = (int)Math.round(temp);
		Resize(estW, height);
		updateDimentions(estW, height);
		
		
	}
	
	public void ResizeByW (int width) {
		double temp = (width)/(ratioW);
		int estH = (int)Math.round(temp);
		Resize(width, estH);
		updateDimentions(width, estH);
		
	}
	
	public void Resize (int width, int height) {
		Image temp = picture.getScaledInstance(width, height, 0); //turns buffered image into 
		BufferedImage picture2 = new BufferedImage(width, height, 1); //make new buffered image with resize size
		
		Graphics transfer = picture2.getGraphics();
		transfer.drawImage(temp, 0, 0 , null); //cast image into bufferedimage type
		picture = picture2; 
		
		updateDimentions(width, height);
		//printphoto(picture);
		
	}
	
	public void printphoto (){
		JFrame frame = new JFrame();

		
//		Graphics transfer = image.getGraphics();
//
//		Container contain = frame.getContentPane();
//		contain.setSize(600,600);
//		contain.paintComponents(transfer);
//
//		contain.setVisible(true);
//		
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //close when stop program
//		
		
		
//		JFrame frame = new JFrame();
		Container contain = frame.getContentPane();
		contain.setLayout(new FlowLayout());
		contain.add(new JLabel(new ImageIcon(picture)));
		frame.pack();
		frame.setVisible(true);
//		frame.getContentPane().setLayout(new FlowLayout());
//		frame.getContentPane().add(new JLabel(new ImageIcon(picture)));
//		frame.pack();
//		frame.setVisible(true);
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //close when stop program
		
	}
	
	public int getWidth(){
		return w;	
	}
	
	public int getHeight(){
		return h;	
	}
	
	
	//--------------------COLOR STUFF
	
	public Color getColorAt (int width, int height) {
		Color col = new Color (picture.getRGB(width, height));
		return col;
	}
	
	public ColorA getColorAAt(int width, int height){
		Color col = new Color(picture.getRGB(width, height));
		ColorA colorA = new ColorA(col);
		return colorA;
	}

	public ColorA ColortoColorA(int rawcolor){
			Color col = new Color(rawcolor);
			ColorA colorA = new ColorA(col);
			return colorA;
		}

	
	//_--------------------COLOR CHANGEING STUFF
	
	public void changeColorAt (int width, int height, ColorA color) {
		int rawcolor = color.rgbint;
		picture.setRGB(width, height, rawcolor);
		
	}
	
	public void changeColorAt (int width, int height, Color color) {
		int rawcolor = color.getRGB();
		picture.setRGB(width, height, rawcolor);
		
	}
	
	public void makeGreyscale () {
		for (int width = 0; width < w; width++) {
			for (int height = 0; height < h; height++){
				makeGreyscaleAt (width, height);			
			}
 		}
	}
	
	public void makeGreyscaleAt (int width, int height){
		ColorA col = getColorAAt(width, height);
		double grey = col.bd + col.rd + col.gd;
		grey = grey / 3;
		Color colorGrey = new Color((int)grey, (int)grey, (int)grey);
		picture.setRGB(width, height, colorGrey.getRGB());
	
	}
	
	public void makeBW () {
		for (int ww = 0; ww < getWidth(); ww++) {
			for (int hh = 0; hh < getHeight(); hh++) { 
				makeBWat(ww, hh);
			}
		}
	}
	
	public void makeBWat(int width, int height) {
		if (getColorAAt(width, height).ratiog > 0.8) {
			Color c = Color.black;
			ColorA ca = new ColorA(c);
			changeColorAt(width, height, ca);
		
		}
		else {
			Color c = Color.white;
			ColorA ca = new ColorA(c);
			changeColorAt(width, height, ca);
		}
	
		
	}
	
	public void replacePicture(Photo front, Photo back, Photo heat) { //photo out is the in
		//if different sizes
		ColorA bl = new ColorA(Color.black);
		
		//if same size
		for (int ww = 0; ww < getWidth(); ww++) {
			for (int hh = 0; hh < getHeight(); hh++) { 
				if(heat.getColorAAt(ww, hh).rgbint == bl.rgbint) { //if heat is black
					changeColorAt(ww,hh, back.getColorAt(ww, hh));
				}
				else {
					changeColorAt(ww,hh, front.getColorAt(ww, hh));
				}
			}
		}
	}
}
