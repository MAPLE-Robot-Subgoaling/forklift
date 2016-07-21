package edu.umbc.cs.forklift.state;

import static edu.umbc.cs.forklift.forklift.ATT_D;
import static edu.umbc.cs.forklift.forklift.ATT_L;
import static edu.umbc.cs.forklift.forklift.ATT_N;
import static edu.umbc.cs.forklift.forklift.ATT_W;
import static edu.umbc.cs.forklift.forklift.ATT_X;
import static edu.umbc.cs.forklift.forklift.ATT_Y;
import static edu.umbc.cs.forklift.forklift.CLASS_AGENT;

import java.util.Arrays;
import java.util.List;

import burlap.mdp.core.oo.state.OOStateUtilities;
import burlap.mdp.core.oo.state.ObjectInstance;

public class FLAgent implements ObjectInstance {

	private float direction;
	private float x;
	private float y;
	private float yLength;
	private float xWidth;
	private String name;
	
	private static final List<Object> keys = Arrays.<Object>asList(ATT_X, ATT_Y, ATT_D, ATT_W, ATT_L, ATT_N);
	
	public FLAgent()
	{
		
	}
	
	public FLAgent(float x, float y, float direction, float yLength, float xWidth, String name)
	{
		this.x = x;
		this.y = y;
		this.direction = direction;
		this.yLength = yLength;
		this.xWidth = xWidth;
		this.name = name;
	}

	public FLAgent copy() {
		return new FLAgent(x, y, direction, yLength, xWidth, name);
	}

	public void set(Object variableKey, Object v)
	{
		if(variableKey instanceof String)
			if(variableKey.equals(ATT_X))
				x = (Float) v;
			else if(variableKey.equals(ATT_Y))
				y = (Float) v;
			else if(variableKey.equals(ATT_D))
				direction = (Float) v;
			else if(variableKey.equals(ATT_W))
				xWidth = (Float) v;
			else if(variableKey.equals(ATT_L))
				yLength = (Float) v;
			else if(variableKey.equals(ATT_N))
				name = (String) v;
			else
				throw new RuntimeException("Unknown key " + variableKey);
		
		else if(variableKey instanceof Integer)
				switch((Integer)variableKey){
					case 0:	x = (Float) v;;
					case 1:	y = (Float) v;
					case 2: direction = (Float) v;
					case 3: xWidth = (Float) v;
					case 4: yLength = (Float) v;
					case 5: name = (String) v;
					default: throw new RuntimeException("Unknown key " + variableKey);
				}
		//if key is not string or integer
		throw new RuntimeException("Unknown key " + variableKey);
	}
	
	public Object get(Object variableKey) {
		if(variableKey instanceof String)
			if(variableKey.equals(ATT_X))
				return x;
			else if(variableKey.equals(ATT_Y))
				return y;
			else if(variableKey.equals(ATT_D))
				return direction;
			else if(variableKey.equals(ATT_W))
				return xWidth;
			else if(variableKey.equals(ATT_L))
				return yLength;
			else if(variableKey.equals(ATT_N))
				return name;
			else
				throw new RuntimeException("Unknown key " + variableKey);
		
		else if(variableKey instanceof Integer)
				switch((Integer)variableKey){
					case 0:	return x;
					case 1:	return y;
					case 2: return direction;
					case 3: return xWidth;
					case 4: return yLength;
					case 5: return name;
					default: throw new RuntimeException("Unknown key " + variableKey);
				}
		//if key is not string or integer
		throw new RuntimeException("Unknown key " + variableKey);
	}

	public List<Object> variableKeys() {
		return keys;
	}

	public String className() {
		return CLASS_AGENT;
	}

	public ObjectInstance copyWithName(String objectName) {
		if(!objectName.equals(CLASS_AGENT))
			throw new RuntimeException("Agent must be of class FLAgent");

		return copy();
	}

	public String name() {
		return CLASS_AGENT;
	}
	
	public String toString() {
		return OOStateUtilities.objectInstanceToString(this);
	}

}
