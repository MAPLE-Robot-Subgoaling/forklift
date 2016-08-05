package edu.umbc.cs.forklift;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import burlap.mdp.singleagent.SADomain;
import burlap.mdp.singleagent.environment.SimulatedEnvironment;
import burlap.mdp.singleagent.oo.OOSADomain;
import burlap.shell.visual.VisualExplorer;
import burlap.visualizer.Visualizer;
import edu.umbc.cs.forklift.state.FLAgent;
import edu.umbc.cs.forklift.state.FLArea;
import edu.umbc.cs.forklift.state.FLState;
import edu.umbc.cs.forklift.state.FLBlock;
import edu.umbc.cs.forklift.state.FLBlock.FLWall;

public class ForkliftClass {

	public static void main(String [] args){

		forklift gen = new forklift();
		SADomain domain = gen.generateDomain();

		FLAgent agent = new FLAgent(2.1, 1.6, 0, 0, 0, 0, 1, 2,"agent", null);
		
		ArrayList<FLBlock> walls = new ArrayList<FLBlock>();
		ArrayList<FLBlock> boxes = new ArrayList<FLBlock>(); 
		FLBlock box = new FLBlock.FLBox(30.0, 10.0, 2, 2, "Boxer");
		boxes.add(box);

		ArrayList<Point2D.Double> gaps = new ArrayList<Point2D.Double>();
		gaps.add(new Point2D.Double(10.0,20.0));
		gaps.add(new Point2D.Double(9.0, 20.0)); 
		gaps.add(new Point2D.Double(11.0, 20.0)); 
		gaps.add(new Point2D.Double(20.0, 10.0)); 
		gaps.add(new Point2D.Double(20.0, 9.0)); 
		gaps.add(new Point2D.Double(20.0, 11.0)); 
		gaps.add(new Point2D.Double(30.0, 20.0)); 
		gaps.add(new Point2D.Double(29.0, 20.0)); 
		gaps.add(new Point2D.Double(31.0, 20.0)); 
		gaps.add(new Point2D.Double(20.0, 30.0)); 
		gaps.add(new Point2D.Double(20.0, 29.0)); 
		gaps.add(new Point2D.Double(20.0, 31.0)); 

		walls.addAll(GenerateRoom(0,20,0,20,gaps));
		walls.addAll(GenerateRoom(20,39,0,20,gaps));
		walls.addAll(GenerateRoom(20,39,20,39,gaps));
		walls.addAll(GenerateRoom(0,20,20,39,gaps));
		//use wally for testing 
		//walls.add(new FLBlock.FLWall(0, 0, 0, 0, "Wally"));
		FLArea goalArea = new FLArea(20.0, 39.0, 20.0, 0.0, "goal");


		FLState state = new FLState(agent, walls, boxes, goalArea);
		System.out.println(domain.getModel().terminal(state));
		SimulatedEnvironment env = new SimulatedEnvironment(domain, state);

		FLVisualizer flv = new FLVisualizer();
		Visualizer v = flv.getVisualizer();
		
		VisualExplorer exp = new VisualExplorer(domain, env, v);

		exp.addKeyAction("w", forklift.MOVE_FORWARD, "");
		exp.addKeyAction("s", forklift.MOVE_BACKWARD, "");
		exp.addKeyAction("d", forklift.ROTATE_CLOCKWISE, "");
		exp.addKeyAction("a", forklift.ROTATE_COUNTERCLOCKWISE, "");
		exp.addKeyAction(" ", forklift.BRAKE, "");
		exp.addKeyAction("x", forklift.IDLE, "");

		exp.initGUI();
		
	}
	
	//preconditions: x1 < x2 and y1 < y2
	private static List<FLWall> GenerateRoom(int x1, int x2, int y1, int y2, ArrayList<Point2D.Double> doors){
		List<FLWall> room = new ArrayList<FLWall>();
		for(int i = x1; i <= x2; i++){
			
			boolean addY1 = true;
			for(Point2D.Double door: doors){
				if(i == door.x && y1 == door.y)
					addY1 = false;
			}
			if(addY1){
				room.add(new FLWall(i, y1, 1, 1, "Wall " + i + ", "+ y1));
			}
			boolean addY2 = true;
			for(Point2D.Double door: doors){
				if(i == door.x && y2 == door.y)
					addY2 = false;
			}
			if(addY2){
				room.add(new FLWall(i, y2, 1, 1, "Wall " + i + ", "+ y2));
			}
		}
		for(int j  = y1+1; j <= y2-1; j++){
			boolean addX1 = true;
			for(Point2D.Double door: doors){
				if(j == door.y && x1 == door.x)
					addX1 = false;
			}
			if(addX1){
				room.add(new FLWall(x1, j, 1, 1, "Wall " + x1 + ", "+ j));
			}
			boolean addX2 = true;
			for(Point2D.Double door: doors){
				if(j == door.y && x2 == door.x)
					addX2 = false;
			}
			if(addX2){
				room.add(new FLWall(x2, j, 1, 1, "Wall " + x2 + ", "+ j));
			}
		}
		return room;
	}
}
