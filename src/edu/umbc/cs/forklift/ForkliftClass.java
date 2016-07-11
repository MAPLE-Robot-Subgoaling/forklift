package edu.umbc.cs.forklift;

import burlap.mdp.singleagent.SADomain;
import burlap.mdp.singleagent.environment.SimulatedEnvironment;
import burlap.shell.visual.VisualExplorer;
import burlap.visualizer.Visualizer;
import edu.umbc.cs.forklift.state.FLAgent;
import edu.umbc.cs.forklift.state.FLState;

public class ForkliftClass {

	public static void main(String [] args){

		forklift gen = new forklift();
		SADomain domain = gen.generateDomain();
		FLAgent initialState = new FLAgent(0,0,0,5,5,"agent");
		SimulatedEnvironment env = new SimulatedEnvironment(domain, initialState);

		FLVisualizer flv = new FLVisualizer();
		Visualizer v = flv.getVisualizer();
		
		System.out.println("entered");
		
		VisualExplorer exp = new VisualExplorer(domain, env, v);
		
		System.out.println("entered");

		/*exp.addKeyAction("w", forklift.MOVE_FORWARD, "");
		exp.addKeyAction("s", forklift.MOVE_BACKWARD, "");
		exp.addKeyAction("d", forklift.ROTATE_CLOCKWISE, "");
		exp.addKeyAction("a", forklift.ROTATE_COUNTERCLOCKWISE, "");*/

		exp.initGUI();

	}
}
