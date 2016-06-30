package edu.umbc.cs.forklift;

import java.util.Arrays;
import java.util.List;

import burlap.mdp.core.oo.state.MutableOOState;
import burlap.mdp.core.oo.state.OOStateUtilities;
import burlap.mdp.core.oo.state.ObjectInstance;
import burlap.mdp.core.state.MutableState;
import burlap.mdp.core.state.StateUtilities;

import static edu.umbc.cs.forklift.forklift.ATT_X;
import static edu.umbc.cs.forklift.forklift.ATT_Y;
import static edu.umbc.cs.forklift.forklift.ATT_D;
import static edu.umbc.cs.forklift.forklift.ATT_W;
import static edu.umbc.cs.forklift.forklift.ATT_L;
//TODO make all the attributes of a forklift agent play with an agent object, not FLState
public class FLState implements MutableOOState{

	private double direction;
	private double x;
	private double y;
	private double yLength;
	private double xWidth;
	
	private static final List<Object> keys = Arrays.<Object>asList(ATT_X, ATT_Y, ATT_D);
	
	public FLState(){
	}
	
	public FLState(double x, double y, double direction, double yLength, double xWidth)
	{
		this.x = x;
		this.y = y;
		this.direction = direction;
		this.yLength = yLength;
		this.xWidth = xWidth;
	}
	
	public FLState copy() {
		return new FLState(x, y, direction, yLength, xWidth);
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
			else
				throw new RuntimeException("Unknown key " + variableKey);
		
		else if(variableKey instanceof Integer)
				switch((Integer)variableKey){
					case 0:	return x;
					case 1:	return y;
					case 2: return direction;
					case 3: return xWidth;
					case 4: return yLength;
					default: throw new RuntimeException("Unknown key " + variableKey);
				}
		//if key is not string or integer
		throw new RuntimeException("Unknown key " + variableKey);
	}

	public MutableState set(Object variableKey, Object value) {
		
		if(variableKey instanceof String){
			
			if(variableKey.equals(ATT_X))
				this.x = StateUtilities.stringOrNumber(value).doubleValue();
			else if(variableKey.equals(ATT_Y))
				this.y = StateUtilities.stringOrNumber(value).doubleValue();
			else if(variableKey.equals(ATT_D))
				this.direction = StateUtilities.stringOrNumber(value).doubleValue();
			else
				throw new RuntimeException("Unknown key " + variableKey);
			
			return this;
			
		}else if(variableKey instanceof Integer){
			
			switch((Integer)variableKey){
				case 0: this.x = StateUtilities.stringOrNumber(value).doubleValue();break;
				case 1: this.y = StateUtilities.stringOrNumber(value).doubleValue();break;
				case 2: this.direction = StateUtilities.stringOrNumber(value).doubleValue();break;
				default:throw new RuntimeException("Unknown key " + variableKey);
			}
			
			return this;
		}
		throw new RuntimeException("Unknown key " + variableKey);
	}
	
	public List<Object> variableKeys() {
		return OOStateUtilities.flatStateKeys(this);
	}
	
	public String toString() {
		return OOStateUtilities.ooStateToString(this);
	}

	//TODO when FLState is more object oriented, fill these out
	public int numObjects() {
		// TODO Auto-generated method stub
		return 0;
	}

	public ObjectInstance object(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<ObjectInstance> objects() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<ObjectInstance> objectsOfClass(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public MutableOOState addObject(ObjectInstance arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public MutableOOState removeObject(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public MutableOOState renameObject(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
