package edu.umbc.cs.forklift;

import java.util.Arrays;
import java.util.List;

import burlap.mdp.core.state.MutableState;
import burlap.mdp.core.state.State;
import burlap.mdp.core.state.StateUtilities;

import static edu.umbc.cs.forklift.forklift.ATT_X;
import static edu.umbc.cs.forklift.forklift.ATT_Y;
import static edu.umbc.cs.forklift.forklift.ATT_D;
import static edu.umbc.cs.forklift.forklift.ATT_W;
import static edu.umbc.cs.forklift.forklift.ATT_L;

public class FLState implements MutableState{

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
	
	public State copy() {
		return new FLState(x, y, direction, yLength, xWidth);
	}

	public Object get(Object variableKey) {
		if(variableKey instanceof String){
			if(variableKey.equals(ATT_X)){
				return x;
			}
			else if(variableKey.equals(ATT_Y)){
				return y;
			}
			else if(variableKey.equals(ATT_D))
			{
				return direction;
			}
			else if(variableKey.equals(ATT_W))
			{
				return xWidth;
			}
			else if(variableKey.equals(ATT_L))
			{
				return yLength;
			}
			else{
				throw new RuntimeException("Unknown key " + variableKey);
			}
		}
		else if(variableKey instanceof Integer){
			if((Integer)variableKey == 0){
				return x;
			}
			else if((Integer)variableKey == 1){
				return y;
			}
			else if((Integer)variableKey == 2){
				return direction;
			}
			else if((Integer)variableKey == 3)
			{
				return xWidth;
			}
			else if((Integer)variableKey == 4)
			{
				return yLength;
			}
			else{
				throw new RuntimeException("Unknown key " + variableKey);
			}
		}

		throw new RuntimeException("Unknown key " + variableKey);
	}

	public MutableState set(Object variableKey, Object value) {
		if(variableKey instanceof String){
			if(variableKey.equals(ATT_X)){
				this.x = StateUtilities.stringOrNumber(value).doubleValue();
				return this;
			}
			else if(variableKey.equals(ATT_Y)){
				this.y = StateUtilities.stringOrNumber(value).doubleValue();
				return this;
			}
			else if(variableKey.equals(ATT_D))
			{
				this.direction = StateUtilities.stringOrNumber(value).doubleValue();
				return this;
			}
			else{
				throw new RuntimeException("Unknown key " + variableKey);
			}
		}
		else if(variableKey instanceof Integer){
			if((Integer)variableKey == 0){
				this.x = StateUtilities.stringOrNumber(value).doubleValue();
				return this;
			}
			else if((Integer)variableKey == 1){
				this.y = StateUtilities.stringOrNumber(value).doubleValue();
				return this;
			}
			else if((Integer)variableKey == 2){
				this.direction = StateUtilities.stringOrNumber(value).intValue();
				return this;
			}
			else{
				throw new RuntimeException("Unknown key " + variableKey);
			}
		}

		throw new RuntimeException("Unknown key " + variableKey);
	}

	public List<Object> variableKeys() {
		return keys;
	}
	
	public String toString() {
		return StateUtilities.stateToString(this);
	}
	

}