package edu.umbc.cs.forklift.state;

import static edu.umbc.cs.forklift.forklift.ATT_L;
import static edu.umbc.cs.forklift.forklift.ATT_N;
import static edu.umbc.cs.forklift.forklift.ATT_W;
import static edu.umbc.cs.forklift.forklift.ATT_X;
import static edu.umbc.cs.forklift.forklift.ATT_Y;

import static edu.umbc.cs.forklift.forklift.CLASS_WALL;
import static edu.umbc.cs.forklift.forklift.CLASS_BOX;
import static edu.umbc.cs.forklift.forklift.CLASS_BLOCK;

import java.util.Arrays;
import java.util.List;

import burlap.mdp.core.oo.state.ObjectInstance;

public class FLBlock implements ObjectInstance {

	private double x;
	private double y;
	private double yLength;
	private double xWidth;
	private String name;
	private static String className;
	
	private static final List<Object> keys = Arrays.<Object>asList(ATT_X, ATT_Y, ATT_W, ATT_L, ATT_N);
	
	public FLBlock(double x, double y, double yLength, double xWidth, String name) {
		this.x = x;
		this.y = y;
		this.yLength = yLength;
		this.xWidth = xWidth;
		this.name = name;
		className = CLASS_BLOCK;
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

	public FLBlock copy() {
		return new FLBlock(x,y,yLength,xWidth,name);
	}

	public String className() {
		return className;
	}

	public String name() {
		return name;
	}

	public ObjectInstance copyWithName(String objectName) {
		if(!objectName.equals(className))
			throw new RuntimeException("Object must be class block");

		return copy();
	}
	public static class FLBox extends FLBlock{
		boolean onGround;
		public FLBox(double x, double y, double yLength, double xWidth, String name) {
			super(x, y, yLength, xWidth, name);
			className = CLASS_BOX;
			onGround = true;
		}
		public boolean pickUp(){
			//if the box is not on the ground, return false and do nothing
			if (!onGround)
				return false;
			else
				onGround = false;
			//if the box was on the ground, it now isn't, return true
			return true;
		}
		
		public boolean putDown(){
			//same deal here but reverse
			if(onGround)
				return false;
			else
				onGround = true;
			return true;
		}
		
		
	}
	public static class FLWall extends FLBlock{

		public FLWall(double x, double y, double yLength, double xWidth, String name) {
			super(x, y, yLength, xWidth, name);
			className = CLASS_WALL;
		}
		
	}

}
