package edu.umbc.cs.forklift;

import java.util.ArrayList;
import java.util.HashMap;
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
		
		List<Float> goalArea = new ArrayList<Float>();
		goalArea.add(12.0f);
		goalArea.add(19.0f);
		goalArea.add(12.0f);
		goalArea.add(19.0f);

		forklift gen = new forklift(goalArea);
		SADomain domain = gen.generateDomain();
		FLAgent agent = new FLAgent(1.1f, 1.1f, 0, 0, 0, 0, 1, 0.5f,"agent");
		ArrayList<FLBlock> Walls = new ArrayList<FLBlock>();
		Map<Integer, List<Float>> doors = new HashMap<Integer, List<Float>>();
		List<Float> door1 = new ArrayList<Float>();
		List<Float> door2 = new ArrayList<Float>();
		List<Float> door3 = new ArrayList<Float>();
		List<Float> door4 = new ArrayList<Float>();
		List<Float> door5 = new ArrayList<Float>();
		List<Float> door6 = new ArrayList<Float>();
		List<Float> door7 = new ArrayList<Float>();
		List<Float> door8 = new ArrayList<Float>();
		List<Float> door9 = new ArrayList<Float>();
		List<Float> door10 = new ArrayList<Float>();
		List<Float> door11 = new ArrayList<Float>();
		List<Float> door12 = new ArrayList<Float>();
		door1.add(5.0f);
		door1.add(10.0f);
		door2.add(4.0f);
		door2.add(10.0f);
		door3.add(6.0f);
		door3.add(10.0f);
		door4.add(10.0f);
		door4.add(5.0f);
		door5.add(10.0f);
		door5.add(4.0f);
		door6.add(10.0f);
		door6.add(6.0f);
		door7.add(15.0f);
		door7.add(10.0f);
		door8.add(14.0f);
		door8.add(10.0f);
		door9.add(16.0f);
		door9.add(10.0f);
		door10.add(10.0f);
		door10.add(15.0f);
		door11.add(10.0f);
		door11.add(14.0f);
		door12.add(10.0f);
		door12.add(16.0f);
		doors.put(0, door1);
		doors.put(1, door2);
		doors.put(2, door3);
		doors.put(3, door4);
		doors.put(4, door5);
		doors.put(5, door6);
		doors.put(6, door7);
		doors.put(7, door8);
		doors.put(8, door9);
		doors.put(9, door10);
		doors.put(10, door11);
		doors.put(11, door12);

		Walls.addAll(GenerateRoom(0,10,0,10,doors));
		Walls.addAll(GenerateRoom(10,19,0,10,doors));
		Walls.addAll(GenerateRoom(10,19,10,19,doors));
		Walls.addAll(GenerateRoom(0,10,10,19,doors));
		
		FLState state = new FLState(agent, Walls);
		SimulatedEnvironment env = new SimulatedEnvironment(domain, state);

		FLVisualizer flv = new FLVisualizer();
		Visualizer v = flv.getVisualizer();
		
		VisualExplorer exp = new VisualExplorer(domain, env, v);

		exp.addKeyAction("w", forklift.MOVE_FORWARD, "");
		exp.addKeyAction("s", forklift.MOVE_BACKWARD, "");
		exp.addKeyAction("d", forklift.ROTATE_CLOCKWISE, "");
		exp.addKeyAction("a", forklift.ROTATE_COUNTERCLOCKWISE, "");
		exp.addKeyAction(" ", forklift.BRAKE, "");

		exp.initGUI();

	}
	
	//preconditions: x1 < x2 and y1 < y2
	private static List<FLWall> GenerateRoom(int x1, int x2, int y1, int y2, Map<Integer, List<Float>> doors){
		List<FLWall> room = new ArrayList<FLWall>();
		for(int i = x1; i <= x2; i++){
			
			boolean addY1 = true;
			for(List<Float> door: doors.values()){
				if(i == door.get(0) && y1 == door.get(1))
					addY1 = false;
			}
			if(addY1){
				room.add(new FLWall(i, y1, 1, 1, "Wall " + i + ", "+ y1));
			}
			boolean addY2 = true;
			for(List<Float> door: doors.values()){
				if(i == door.get(0) && y2 == door.get(1))
					addY2 = false;
			}
			if(addY2){
				room.add(new FLWall(i, y2, 1, 1, "Wall " + i + ", "+ y2));
			}
		}
		for(int j  = y1+1; j <= y2-1; j++){
			boolean addX1 = true;
			for(List<Float> door: doors.values()){
				if(j == door.get(1) && x1 == door.get(0))
					addX1 = false;
			}
			if(addX1){
				room.add(new FLWall(x1, j, 1, 1, "Wall " + x1 + ", "+ j));
			}
			boolean addX2 = true;
			for(List<Float> door: doors.values()){
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
