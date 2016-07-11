package edu.umbc.cs.forklift;

import java.util.ArrayList;

import burlap.mdp.singleagent.SADomain;
import burlap.mdp.singleagent.environment.SimulatedEnvironment;
import burlap.shell.visual.VisualExplorer;
import burlap.visualizer.Visualizer;
import edu.umbc.cs.forklift.state.FLAgent;
import edu.umbc.cs.forklift.state.FLState;
import edu.umbc.cs.forklift.state.FLWall;

public class ForkliftClass {

	public static void main(String [] args){

		forklift gen = new forklift();
		SADomain domain = gen.generateDomain();
		FLAgent agent = new FLAgent(0,0,0,5,5,"agent");
		ArrayList<FLWall> Walls = new ArrayList<FLWall>();
		for(int i = 0; i < 10; i++)
		{
			Walls.add(new FLWall(i, 10 , 1, 1, "wall"+i));
		}
		FLState state = new FLState(agent, Walls);
		SimulatedEnvironment env = new SimulatedEnvironment(domain, state);

		FLVisualizer flv = new FLVisualizer();
		Visualizer v = flv.getVisualizer();
		
		System.out.println("entered");
		
		VisualExplorer exp = new VisualExplorer(domain, env, v);
		
		System.out.println("entered");

		exp.addKeyAction("w", forklift.MOVE_FORWARD, "");
		exp.addKeyAction("s", forklift.MOVE_BACKWARD, "");
		exp.addKeyAction("d", forklift.ROTATE_CLOCKWISE, "");
		exp.addKeyAction("a", forklift.ROTATE_COUNTERCLOCKWISE, "");

		exp.initGUI();

	}
}
