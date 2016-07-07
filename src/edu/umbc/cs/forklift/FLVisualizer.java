package edu.umbc.cs.forklift;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import burlap.mdp.core.oo.state.OOState;
import burlap.mdp.core.oo.state.ObjectInstance;
import burlap.visualizer.ObjectPainter;

public class FLVisualizer {

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
				double length = (Double) ob.get(forklift.ATT_L);
				double width = (Double) ob.get(forklift.ATT_W);
				
				g2.fill(new Rectangle2D.Double(x, y, width, length));
			}
			else
			{
				
			}
		}
		
		public static class ForkliftPainter implements ObjectPainter
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
				double length = (Double) ob.get(forklift.ATT_L);
				double width = (Double) ob.get(forklift.ATT_W);
				double direction = (Double)ob.get(forklift.ATT_D);
				
				
			}
			
		}
		
	}
}
