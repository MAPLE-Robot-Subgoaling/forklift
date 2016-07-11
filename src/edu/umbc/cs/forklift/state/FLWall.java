package edu.umbc.cs.forklift.state;

import static burlap.domain.singleagent.lunarlander.LunarLanderDomain.CLASS_AGENT;
import static edu.umbc.cs.forklift.forklift.ATT_D;
import static edu.umbc.cs.forklift.forklift.ATT_L;
import static edu.umbc.cs.forklift.forklift.ATT_N;
import static edu.umbc.cs.forklift.forklift.ATT_W;
import static edu.umbc.cs.forklift.forklift.ATT_X;
import static edu.umbc.cs.forklift.forklift.ATT_Y;

import java.util.Arrays;
import java.util.List;

import burlap.mdp.core.oo.state.ObjectInstance;
import burlap.mdp.core.state.State;

import static edu.umbc.cs.forklift.forklift.ATT_X;
import static edu.umbc.cs.forklift.forklift.ATT_Y;
import static edu.umbc.cs.forklift.forklift.ATT_D;
import static edu.umbc.cs.forklift.forklift.ATT_W;
import static edu.umbc.cs.forklift.forklift.ATT_L;
import static edu.umbc.cs.forklift.forklift.ATT_N;

public class FLWall implements ObjectInstance{

	private double x;
	private double y;
	private double yLength;
	private double xWidth;
	private String name;
	
	private static final List<Object> keys = Arrays.<Object>asList(ATT_X, ATT_Y, ATT_W, ATT_L, ATT_N);
	
	public FLWall(double x, double y, double yLength, double xWidth, String name)
	{
		this.x = x;
		this.y = y;
		this.yLength = yLength;
		this.xWidth = xWidth;
		this.name = name;
	}
	
	public List<Object> variableKeys() {
		return keys;
	}

	public Object get(Object variableKey) {
		if(variableKey instanceof String)
			if(variableKey.equals(ATT_X))
				return x;
			else if(variableKey.equals(ATT_Y))
				return y;
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
					case 2: return xWidth;
					case 3: return yLength;
					case 4: return name;
					default: throw new RuntimeException("Unknown key " + variableKey);
				}
		//if key is not string or integer
		throw new RuntimeException("Unknown key " + variableKey);
	}

	public FLWall copy() {
		return new FLWall(x,y,yLength,xWidth,name);
	}

	public String className() {
		return "wall";
	}

	public String name() {
		return "wall";
	}

	public ObjectInstance copyWithName(String objectName) {
		if(!objectName.equals("wall"))
			throw new RuntimeException("Wall must be class wall");

		return copy();
	}

}
