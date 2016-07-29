package edu.umbc.cs.forklift;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import burlap.mdp.core.oo.state.OOState;
import burlap.mdp.core.oo.state.ObjectInstance;
import burlap.visualizer.OOStatePainter;
import burlap.visualizer.ObjectPainter;
import burlap.visualizer.StateRenderLayer;
import burlap.visualizer.Visualizer;


import static edu.umbc.cs.forklift.forklift.CLASS_AGENT;
import static edu.umbc.cs.forklift.forklift.CLASS_WALL;
import static edu.umbc.cs.forklift.forklift.CLASS_BOX;

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
			if(imgPath.equals("none"))
			{
				g2.setColor(Color.RED);
				
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
	public static class ForkliftPainter implements ObjectPainter, ImageObserver
	{
		
		private String imgPath;
		
		Map<String, BufferedImage> dirToImage;
		
		public ForkliftPainter(String imgPath)
		{
			this.imgPath = imgPath;
			
			if(!this.imgPath.endsWith("/")){
				this.imgPath = this.imgPath + "/";
			}
	
			dirToImage = new HashMap<String, BufferedImage>(4);
			try {
				dirToImage.put("north", ImageIO.read(new File(this.imgPath + "robotNorth.png")));
				dirToImage.put("south", ImageIO.read(new File(this.imgPath + "robotSouth.png")));
				dirToImage.put("east", ImageIO.read(new File(this.imgPath + "robotEast.png")));
				dirToImage.put("west", ImageIO.read(new File(this.imgPath + "robotWest.png")));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
				
		public void paintObject(Graphics2D g2, OOState s, ObjectInstance ob, float cWidth, float cHeight) 
		{
			double x = (Double) ob.get(forklift.ATT_X);
			double y = (Double) ob.get(forklift.ATT_Y);
			double direction = (Double)ob.get(forklift.ATT_D);
			
			double width = cWidth / (double)forklift.xBound;
			double height = cHeight / (double)forklift.yBound;
			
			double rx = (double)x * width;
			double ry = cHeight - height - (double)y * height;
			
			String dir = null;
					
			//if(direction > 315 || direction < 45)
			//{
				dir = "east";
			/*}
			else if(direction > 225)
			{
				dir = "south";
			}
			else if(direction > 135)
			{
				dir = "west";
			}
			else
			{
				dir = "north";
			}*/
					
			BufferedImage img = this.dirToImage.get(dir);
			
			AffineTransform rot = new AffineTransform(); 
			Rectangle r = g2.getDeviceConfiguration().getBounds();
			System.out.println(r.getWidth() + " - " + r.getHeight());
			rot.translate(r.getWidth() * (x + 1)/ forklift.xBound, r.getHeight() * (forklift.yBound - y - 1) / forklift.yBound);
			rot.rotate(Math.toRadians(direction)); 
			double cWidthSize = r.getWidth()/forklift.xBound;
			double cHeightSize = r.getHeight()/forklift.yBound/2;
			double scaleWidth = cWidthSize/img.getWidth();
			double scaleHeight = cHeightSize/img.getHeight();
//			System.out.println(cWidthSize + " - " + cHeightSize + " - " + scaleWidth + " - " + scaleHeight);
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
