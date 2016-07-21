package edu.umbc.cs.forklift.state;

import static edu.umbc.cs.forklift.forklift.ATT_D;
import static edu.umbc.cs.forklift.forklift.ATT_L;
import static edu.umbc.cs.forklift.forklift.ATT_N;
import static edu.umbc.cs.forklift.forklift.ATT_W;
import static edu.umbc.cs.forklift.forklift.ATT_X;
import static edu.umbc.cs.forklift.forklift.ATT_Y;
import static edu.umbc.cs.forklift.forklift.ATT_VX;
import static edu.umbc.cs.forklift.forklift.ATT_VY;
import static edu.umbc.cs.forklift.forklift.ATT_VR;
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
	private float vx;
	private float vy;
	private float vr;
	private String name;
	
	private static final List<Object> keys = 
	Arrays.<Object>asList(ATT_X, ATT_Y, 
						  ATT_VX, ATT_VY, ATT_VR,
						  ATT_D,
						  ATT_W, ATT_L, 
						  ATT_N);
	
	public FLAgent()
	{
		
	}
	
	public FLAgent(float x, float y, float vx, float vy, float vr, float direction, float yLength, float xWidth, String name)	
	{
		this.x = x;
		this.y = y;
		this.vx = vx;
		this.vy = vy;
		this.vr = vr;
		this.direction = direction;
		this.yLength = yLength;
		this.xWidth = xWidth;
		this.name = name;
	}
	public FLAgent(float x, float y, float direction, float yLength, float xWidth, String name){
		this(x,y,0,0,0,direction,yLength,xWidth,name);
	}

	public FLAgent copy() {
		return new FLAgent(x, y, vx, vy, vr, direction, yLength, xWidth, name);
	}

	public void set(Object variableKey, Object v)
	{
		if(variableKey instanceof String)
			//force the String key to an Int key so next if statement will fire
			if(keys.indexOf(variableKey)!=-1)
				variableKey=keys.indexOf(variableKey);
			else
				throw new RuntimeException("Unknown key " + variableKey);
		//TODO test this set method
		if(variableKey instanceof Integer)
			switch((Integer)variableKey){
				case 0:	x = (Float) v;;
				case 1:	y = (Float) v;
				case 2: vx = (Float) v; break;
				case 3: vy = (Float) v; break;
				case 4: vr = (Float) v; break;
				case 5: direction = (Float) v;
				case 6: xWidth = (Float) v;
				case 7: yLength = (Float) v;
				case 8: name = (String) v;
				default: throw new RuntimeException("Unknown key " + variableKey);
			}
		//if key is not string or integer
		throw new RuntimeException("Unknown key " + variableKey);
	}
	
	public Object get(Object variableKey) {
		if(variableKey instanceof String)
			//force the String key to an Int key so next if statement will fire
			if(keys.indexOf(variableKey)!=-1)
				variableKey=keys.indexOf(variableKey);
			else
				throw new RuntimeException("Unknown key " + variableKey);
		//TODO test this get method
		if(variableKey instanceof Integer)
				switch((Integer)variableKey){
					case 0:	return x; 
					case 1:	return y; 
					case 2: return vx;
					case 3: return vy; 
					case 4: return vr;
					case 5: return direction;
					case 6: return xWidth;
					case 7: return yLength;
					case 8: return name;
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
