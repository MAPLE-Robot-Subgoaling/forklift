package edu.umbc.cs.forklift.state;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import burlap.domain.singleagent.lunarlander.state.LLAgent;
import burlap.domain.singleagent.lunarlander.state.LLBlock;
import burlap.mdp.core.oo.state.MutableOOState;
import burlap.mdp.core.oo.state.OOStateUtilities;
import burlap.mdp.core.oo.state.ObjectInstance;
import burlap.mdp.core.oo.state.exceptions.UnknownClassException;
import burlap.mdp.core.state.MutableState;
import burlap.mdp.core.state.StateUtilities;

import static edu.umbc.cs.forklift.forklift.ATT_X;
import static edu.umbc.cs.forklift.forklift.ATT_Y;
import static edu.umbc.cs.forklift.forklift.ATT_D;
import static edu.umbc.cs.forklift.forklift.ATT_W;
import static edu.umbc.cs.forklift.forklift.ATT_L;
import static edu.umbc.cs.forklift.forklift.ATT_N;
//TODO make all the attributes of a forklift agent play with an agent object, not FLState
public class FLState implements MutableOOState{
	
	FLAgent agent;
	List <FLWall> walls;
	
	public FLState(){
	}
	
	public FLState(FLAgent agent, List<FLWall> walls)
	{
		this.agent = agent;
		this.walls = walls;
	}
	
	public FLState copy() {
		return new FLState(agent, walls);
	}
	
	public List<Object> variableKeys() {
		return OOStateUtilities.flatStateKeys(this);
	}
	
	public String toString() {
		return OOStateUtilities.ooStateToString(this);
	}

	//TODO when FLState is more object oriented, fill these out
	public int numObjects() {
		return 1 + walls.size();
	}

	public ObjectInstance object(String s) {
		if(agent.name() == s)
			return agent;
		else{
			for(FLWall w: walls)
			{
				if(w.name() == s)
					return w;
			}
		}
		return null;
	}

	public List<ObjectInstance> objects() {
		List<ObjectInstance> obj = new ArrayList<ObjectInstance>();
		obj.add(agent);
		for(FLWall w: walls)
		{
			obj.add(w);
		}
		return obj;
	}

	public List<ObjectInstance> objectsOfClass(String s) {
		ArrayList
		if(agent.className() == s){
			return new ArrayList<ObjectInstance>(){{add(agent);}};
		}
		else if(walls.get(0).className().equals(s))
		{
			walls;
		}
		return null;
	}

	public MutableOOState addObject(ObjectInstance o) {
		if(o instanceof LLAgent){
			agent = (FLAgent)o;
		}
		else if(o instanceof FLWall){
			walls.add((FLWall)o);
		}
		else{
			throw new UnknownClassException(o.className());
		}

		return this;
	}

	public MutableOOState removeObject(String s) {
		if(agent.name().equals(s)){
			agent = new FLAgent(); //cannot remove, so copy
		}
		return this;
	}

	public MutableOOState renameObject(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object get(Object variableKey) {
		// TODO Auto-generated method stub
		return null;
	}

	public MutableState set(Object variableKey, Object value) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
