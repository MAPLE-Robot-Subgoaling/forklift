package edu.umbc.cs.forklift.state;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.util.OpenIntToDoubleHashMap.Iterator;

import burlap.mdp.core.oo.state.MutableOOState;
import burlap.mdp.core.oo.state.OOStateUtilities;
import burlap.mdp.core.oo.state.ObjectInstance;
import burlap.mdp.core.oo.state.exceptions.UnknownClassException;
import burlap.mdp.core.state.MutableState;
import edu.umbc.cs.forklift.state.FLBlock.FLBox;
import edu.umbc.cs.forklift.state.FLBlock.FLWall;
import edu.umbc.cs.forklift.state.FLBlock;

import static edu.umbc.cs.forklift.forklift.*;

public class FLState implements MutableOOState{
	
	FLAgent agent;
	List <FLBlock> walls;
	List <FLBlock> boxes;

	private static final List<Object> keys = Arrays.<Object>asList(CLASS_AGENT,CLASS_WALL,CLASS_BOX);
	
	public FLState(){
		walls = new ArrayList<FLBlock>();
		boxes = new ArrayList<FLBlock>();
	}
	
	public FLState(FLAgent agent, List<FLBlock> walls)
	{
		this.agent = agent;
		this.walls = walls;
	}
	public FLState(FLAgent agent, List<FLBlock> walls, List<FLBlock> boxes){
		this(agent, walls);
		this.boxes = boxes;
	}
	public FLState copy() {
		return new FLState(agent, walls, boxes);
	}
	
	public List<Object> variableKeys() {
		return OOStateUtilities.flatStateKeys(this);
	}
	
	public String toString() {
		return OOStateUtilities.ooStateToString(this);
	}

	//TODO when FLState is more object oriented, fill these out
	public int numObjects() {
		return 1 + walls.size()+boxes.size();
	}

	public ObjectInstance object(String s) {
		if(agent.name().equals(s))
			return agent;
		else{
			for(FLBlock w: walls)
				if(w.name().equals(s))
					return w;
			for(FLBlock b: boxes)
				if(b.name().equals(s))
					return b;
		}
		return null;
	}

	public List<ObjectInstance> objects() {
		List<ObjectInstance> obj = new ArrayList<ObjectInstance>();
		obj.add(agent);
		for(FLBlock w: walls)
			obj.add(w);
		for(FLBlock b: boxes)
			obj.add(b);
		return obj;
	}

	public List<ObjectInstance> objectsOfClass(String s) {
		List<ObjectInstance> ooc = new ArrayList<ObjectInstance>();
		if(s.equals(CLASS_AGENT))
			ooc.add(agent);
		else if(s.equals(CLASS_WALL))
			for(FLBlock w: walls)
				ooc.add(w);
		else if(s.equals(CLASS_BOX))
			for(FLBlock b: boxes)
				ooc.add(b);
		return ooc;
	}

	public MutableOOState addObject(ObjectInstance o) {
		if(o instanceof FLAgent)
			agent = (FLAgent)o;
		else if(o instanceof FLWall)
			walls.add((FLWall)o);
		else if(o instanceof FLBox)
			boxes.add((FLBox)o);
		else
			throw new UnknownClassException(o.className());

		return this;
	}

	public MutableOOState removeObject(String s) {
		if(agent.name().equals(s))
			agent = new FLAgent(); //cannot remove, so copy over with blank slate
		else{
			//use fancy iterator as list modification occurs
			for(java.util.Iterator<FLBlock> iterator = walls.iterator(); iterator.hasNext();)
				if(iterator.next().name().equals(s))
					iterator.remove();
			for(java.util.Iterator<FLBlock> iterator = boxes.iterator(); iterator.hasNext();)
				if(iterator.next().name().equals(s))
					iterator.remove();
		}
		return this;
	}

	public MutableOOState renameObject(String s1, String s2) {
		ObjectInstance o = this.object(s1);
		o = o.copyWithName(s2);
		this.removeObject(s1);
		this.addObject(o);
		return this;
	}

	public Object get(Object key) {
		if(key.equals(CLASS_AGENT))
			return agent;
		else if(key.equals(CLASS_WALL))
			return walls;
		else if(key.equals(CLASS_BOX))
			return boxes;
		return null;
	}

	public MutableState set(Object key, Object value) {
		if(key.equals(CLASS_AGENT))
			agent = (FLAgent) value;
		else if(key.equals(CLASS_WALL))
			walls = (List<FLBlock>) value;
		else if(key.equals(CLASS_BOX))
			boxes = (List<FLBlock>) value;
		return this;
	}
	

}
