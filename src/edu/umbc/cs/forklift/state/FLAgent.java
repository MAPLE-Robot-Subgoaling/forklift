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
import static edu.umbc.cs.forklift.forklift.ATT_B;
import static edu.umbc.cs.forklift.forklift.ATT_VM;
import static edu.umbc.cs.forklift.forklift.CLASS_AGENT;

import java.util.Arrays;
import java.util.List;

import burlap.mdp.core.oo.state.OOStateUtilities;
import burlap.mdp.core.oo.state.ObjectInstance;
import edu.umbc.cs.forklift.state.FLBlock.FLBox;

public class FLAgent implements ObjectInstance {

	private double direction;
	private double x;
	private double y;
	private double yLength;
	private double xWidth;
	private double vx;
	private double vy;
	private double vr;
	private String name;
	private FLBox grabbed;
	private double vm;
	
	private static final List<Object> keys = 
	Arrays.<Object>asList(ATT_X, ATT_Y, 
						  ATT_VX, ATT_VY, ATT_VR,
						  ATT_D,
						  ATT_W, ATT_L, 
						  ATT_N, ATT_VM);
	
	public FLAgent()
	{
		
	}
	
	public FLAgent(double x, double y, double vx, double vy, double vr, double direction, double yLength, double xWidth, String name, FLBox grabbed, double vm)	
	{
		this.x = x;
		this.y = y;
		this.vx = vx;
		this.vy = vy;
		this.vr = vr;
		this.vm = vm;
		this.direction = direction;
		this.yLength = yLength;
		this.xWidth = xWidth;
		this.name = name;
		this.grabbed = grabbed;
	}
	public FLAgent(double x, double y, double direction, double yLength, double xWidth, String name, FLBox grabbed, double vm){
		this(x,y,0,0,0,direction,yLength,xWidth,name, grabbed, vm);
	}

	public FLAgent copy() {
		return new FLAgent(x, y, vx, vy, vr, direction, yLength, xWidth, name, grabbed, vm);
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
				case 0:	x = (Double) v;;
				case 1:	y = (Double) v;
				case 2: vx = (Double) v; break;
				case 3: vy = (Double) v; break;
				case 4: vr = (Double) v; break;
				case 5: direction = (Double) v;
				case 6: xWidth = (Double) v;
				case 7: yLength = (Double) v;
				case 8: name = (String) v;
				case 9: vm = (Double)v;
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
					case 9: return vm;
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
	
	public FLBox getGrabbed()
	{
		return grabbed;
	}
	
	public void setGrabbed(FLBox grabbed)
	{
		this.grabbed = grabbed;
	}
	
	public String toString() {
		return OOStateUtilities.objectInstanceToString(this);
	}

}
