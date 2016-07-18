package edu.umbc.cs.forklift;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import burlap.mdp.singleagent.SADomain;
import burlap.mdp.singleagent.environment.SimulatedEnvironment;
import burlap.shell.visual.VisualExplorer;
import burlap.visualizer.Visualizer;
import edu.umbc.cs.forklift.state.FLAgent;
import edu.umbc.cs.forklift.state.FLState;
import edu.umbc.cs.forklift.state.FLBlock;
import edu.umbc.cs.forklift.state.FLBlock.FLWall;

public class ForkliftClass {

	public static void main(String [] args){

		forklift gen = new forklift();
		SADomain domain = gen.generateDomain();
		FLAgent agent = new FLAgent(0,0,90,1,1,"agent");
		ArrayList<FLBlock> Walls = new ArrayList<FLBlock>();
		for(int i = 0; i < 10; i++)
		{
			Walls.add(new FLBlock.FLWall(i, 10, 1, 1, "Wall " + i + ", " + 10));
		}
		FLState state = new FLState(agent, Walls);
		SimulatedEnvironment env = new SimulatedEnvironment(domain, state);

		FLVisualizer flv = new FLVisualizer();
		Visualizer v = flv.getVisualizer();
		
		VisualExplorer exp = new VisualExplorer(domain, env, v);

		exp.addKeyAction("w", forklift.MOVE_FORWARD, "");
		exp.addKeyAction("s", forklift.MOVE_BACKWARD, "");
		exp.addKeyAction("d", forklift.ROTATE_CLOCKWISE, "");
		exp.addKeyAction("a", forklift.ROTATE_COUNTERCLOCKWISE, "");

		exp.initGUI();

	}
	
	//preconditions: x1 < x2 and y1 < y2
	private static List<FLWall> GenerateRoom(int x1, int x2, int y1, int y2, Map<Integer, List<Double>> doors){
		List<FLWall> room = new ArrayList<FLWall>();
		for(int i = x1; i < x2; i++){
			
			boolean addY1 = true;
			for(List<Double> door: doors.values()){
				if(i == door.get(0) && y1 == door.get(1))
					addY1 = false;
			}
			if(addY1){
				room.add(new FLWall(i, y1, 1, 1, "Wall " + i + ", "+ y1));
			}
			boolean addY2 = true;
			for(List<Double> door: doors.values()){
				if(i == door.get(0) && y2 == door.get(1))
					addY2 = false;
			}
			if(addY2){
				room.add(new FLWall(i, y2, 1, 1, "Wall " + i + ", "+ y2));
			}
		}
		for(int j  = y1+1; j < y2-1; j++){
			boolean addX1 = true;
			for(List<Double> door: doors.values()){
				if(j == door.get(1) && x1 == door.get(0))
					addX1 = false;
			}
			if(addX1){
				room.add(new FLWall(x1, j, 1, 1, "Wall " + x1 + ", "+ j));
			}
			boolean addX2 = true;
			for(List<Double> door: doors.values()){
				if(j == door.get(1) && x2 == door.get(0))
					addX2 = false;
			}
			if(addX2){
				room.add(new FLWall(x2, j, 1, 1, "Wall " + x2 + ", "+ j));
			}
		}
		return room;
	}
}
