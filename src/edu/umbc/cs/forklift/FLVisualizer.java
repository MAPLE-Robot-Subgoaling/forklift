package edu.umbc.cs.forklift;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
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
		
		System.out.println("entered");
		
		ooStatePainter.addObjectClassPainter("agent", new ForkliftPainter("data/resources/robotImagesForklift/"));
		ooStatePainter.addObjectClassPainter("wall", new WallPainter());

		System.out.println("entered");
		
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
				
				float width = cWidth / (float)forklift.xBound;
				float height = cHeight / (float)forklift.yBound;
				
				float rx = (float)x * width;
				float ry = cHeight - height - (float)y * height;
				
				g2.fill(new Rectangle2D.Float(rx, ry, width, height));
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
			
			if(!imgPath.endsWith("/")){
				imgPath = imgPath + "/";
			}
	
			dirToImage = new HashMap<String, BufferedImage>(4);
			try {
				dirToImage.put("north", ImageIO.read(new File(imgPath + "robotNorth.png")));
				dirToImage.put("south", ImageIO.read(new File(imgPath + "robotSouth.png")));
				dirToImage.put("east", ImageIO.read(new File(imgPath + "robotEast.png")));
				dirToImage.put("west", ImageIO.read(new File(imgPath + "robotWest.png")));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
				
		public void paintObject(Graphics2D g2, OOState s, ObjectInstance ob, float cWidth, float cHeight) 
		{
			double x = (Double) ob.get(forklift.ATT_X);
			double y = (Double) ob.get(forklift.ATT_Y);
			double direction = (Double)ob.get(forklift.ATT_D);
			
			float width = cWidth / (float)forklift.xBound;
			float height = cHeight / (float)forklift.yBound;
			
			float rx = (float)x * width;
			float ry = cHeight - height - (float)y * height;
			
			String dir = null;
					
			if(direction > 315 || direction < 45)
			{
				dir = "north";
			}
			else if(direction > 225)
			{
				dir = "west";
			}
			else if(direction > 135)
			{
				dir = "south";
			}
			else
			{
				dir = "east";
			}
					
			BufferedImage img = this.dirToImage.get(dir);
			g2.drawImage(img, (int)rx, (int)ry, (int)width, (int)height, this);
			
		}
		
		public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) 
		{
			return false;
		}	
	}
}
