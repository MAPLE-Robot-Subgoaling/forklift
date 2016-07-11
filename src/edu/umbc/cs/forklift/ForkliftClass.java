package edu.umbc.cs.forklift;

import java.util.ArrayList;

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
			Walls.add(new FLBlock.FLWall(i, 10, 1, 1, "wall"+i));
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
}
