package edu.umbc.cs.forklift;

import static edu.umbc.cs.forklift.forklift.CLASS_AGENT;
import static edu.umbc.cs.forklift.forklift.CLASS_BOX;
import static edu.umbc.cs.forklift.forklift.CLASS_WALL;
import static edu.umbc.cs.forklift.forklift.ATT_O;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import burlap.mdp.core.oo.state.OOState;
import burlap.mdp.core.oo.state.ObjectInstance;
import burlap.visualizer.OOStatePainter;
import burlap.visualizer.ObjectPainter;
import burlap.visualizer.StateRenderLayer;
import burlap.visualizer.Visualizer;

public class FLVisualizer {
	
	public FLVisualizer()
	{
		
	}
	
	public Visualizer getVisualizer()
	{
		Visualizer v = new Visualizer(this.getStateRenderLayer());
		return v;
	}
	
	public StateRenderLayer getStateRenderLayer(){
		StateRenderLayer slr = new StateRenderLayer();

		OOStatePainter ooStatePainter = new OOStatePainter();
		slr.addStatePainter(ooStatePainter);
		
		ooStatePainter.addObjectClassPainter(CLASS_AGENT, new ForkliftPainter("data/resources/robotImagesForklift/"));
		ooStatePainter.addObjectClassPainter(CLASS_WALL, new WallPainter());
		ooStatePainter.addObjectClassPainter(CLASS_BOX, new BoxPainter());
		
		return slr;
	}

	public static class WallPainter implements ObjectPainter
	{
		private String imgPath;
		public WallPainter()
		{
			this.imgPath = "none";
		}
		public WallPainter(String imgPath)
		{
			this.imgPath = imgPath;
		}

		public void paintObject(Graphics2D g2, OOState s, ObjectInstance ob, float cWidth, float cHeight) 
		{
			if(imgPath.equals("none"))
			{
				g2.setColor(Color.BLACK);
				
				double x = (Double) ob.get(forklift.ATT_X);
				double y = (Double) ob.get(forklift.ATT_Y);
				
				double width = cWidth / forklift.xBound;
				double height = cHeight / forklift.yBound;
				
				double rx = x * width;
				double ry = cHeight - height - y * height;
				
				g2.fill(new Rectangle2D.Double(rx, ry, width, height));
			}
			else
			{
				
			}
		}
	}
	public static class BoxPainter implements ObjectPainter
	{
		private String imgPath;
		public BoxPainter()
		{
			this.imgPath = "none";
		}
		public BoxPainter(String imgPath)
		{
			this.imgPath = imgPath;
		}

		public void paintObject(Graphics2D g2, OOState s, ObjectInstance ob, float cWidth, float cHeight) 
		{
			boolean o = (Boolean)ob.get(ATT_O);
			if(imgPath.equals("none") && o == true)
			{
				g2.setColor(Color.RED);
				
				double x = (Double) ob.get(forklift.ATT_X);
				double y = (Double) ob.get(forklift.ATT_Y);
				double w = (Double) ob.get(forklift.ATT_W);
				double l = (Double) ob.get(forklift.ATT_L);
				
				double width = cWidth / forklift.xBound;
				double height = cHeight / forklift.yBound;
				
				double rx = x * width;
				double ry = cHeight  - height * l - y * height;
				double rw = w * width;
				double rl = l * height;
				
				g2.fill(new Rectangle2D.Double(rx, ry, rw, rl));
			}
			else
			{
				
			}
		}
	}
	public static class ForkliftPainter implements ObjectPainter, ImageObserver
	{
		
		private String imgPath;
		
		BufferedImage img;
		
		public ForkliftPainter(String imgPath)
		{
			this.imgPath = imgPath;
			
			if(!this.imgPath.endsWith("/")){
				this.imgPath = this.imgPath + "/";
			}
	
			try {
				img = ImageIO.read(new File(this.imgPath + "robotEast.png"));
				}catch (IOException e) {
					e.printStackTrace();
				}
		}
				
		public void paintObject(Graphics2D g2, OOState s, ObjectInstance ob, float cWidth, float cHeight) 
		{
			double x = (Double) ob.get(forklift.ATT_X);
			double y = (Double) ob.get(forklift.ATT_Y);
			double w = (Double) ob.get(forklift.ATT_W);
			double l = (Double) ob.get(forklift.ATT_L);
			double direction = (Double)ob.get(forklift.ATT_D);
			
			//System.out.println(x + " " + y + " " + w + " " + l);
			
			AffineTransform rot = new AffineTransform(); 
			Rectangle r = g2.getDeviceConfiguration().getBounds();
			//System.out.println(r.getX() + " " + r.getY() + " " + r.getWidth()+ " " + r.getHeight());
			rot.translate(r.getWidth() * (x)/ (forklift.xBound), r.getHeight() * (forklift.yBound - y) / (forklift.yBound));
			rot.rotate(Math.toRadians(direction)); 
			double cWidthSize = r.getWidth()/forklift.xBound * w;
			double cHeightSize = r.getHeight()/forklift.yBound * l;
			double scaleWidth = cWidthSize/img.getWidth();
			double scaleHeight = cHeightSize/img.getHeight();
			rot.scale(scaleWidth, scaleHeight);
			rot.translate(-img.getWidth()/2,-img.getHeight()/2);
			g2.drawImage(img, rot, this);
			
		}
		
		public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) 
		{
			return false;
		}	
	}
}
