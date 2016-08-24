package edu.umbc.cs.forklift.state;

import static edu.umbc.cs.forklift.forklift.CLASS_AREA;
import static edu.umbc.cs.forklift.forklift.ATT_L;
import static edu.umbc.cs.forklift.forklift.ATT_N;
import static edu.umbc.cs.forklift.forklift.ATT_W;
import static edu.umbc.cs.forklift.forklift.ATT_X;
import static edu.umbc.cs.forklift.forklift.ATT_Y;

import java.util.Arrays;
import java.util.List;

import burlap.mdp.core.oo.state.ObjectInstance;
import burlap.mdp.core.state.State;

public class FLArea implements ObjectInstance {

	private double left;
	private double right;
	private double top;
	private double bot;
	private String name;
	private static String className;
	
	private static final List<Object> keys = Arrays.<Object>asList(ATT_X, ATT_W, ATT_Y, ATT_L);

	public FLArea(double l, double r, double t, double b, String name) {
		this.left = l;
		this.right = r;
		this.top = t;
		this.bot = b;
		this.className = CLASS_AREA;
		this.name = name;
		
	}
	public List<Object> variableKeys() {
		// TODO Auto-generated method stub
		return keys;
	}
	public Object get(Object variableKey) {
		if(variableKey instanceof String)
			if(variableKey.equals(ATT_X))
				return left;
			else if(variableKey.equals(ATT_W))
				return right;
			else if(variableKey.equals(ATT_Y))
				return top;
			else if(variableKey.equals(ATT_L))
				return bot;
			else
				throw new RuntimeException("Unknown key " + variableKey);
		
		else if(variableKey instanceof Integer)
				switch((Integer)variableKey){
					case 0:	return left;
					case 1:	return right;
					case 2: return top;
					case 3: return bot;
					default: throw new RuntimeException("Unknown key " + variableKey);
				}
		//if key is not string or integer
		throw new RuntimeException("Unknown key " + variableKey);
	}
	public FLArea copy() {
		return new FLArea(left, right, top, bot, name);
	}
	public String className() {
		return className;
	}
	public String name() {
		return this.name;
	}
	public ObjectInstance copyWithName(String objectName) {
		return new FLArea(left, right, top, bot, objectName);
	}
	

}
