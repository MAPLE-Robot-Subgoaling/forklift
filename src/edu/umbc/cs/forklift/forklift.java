package edu.umbc.cs.forklift;

import java.util.List;

import burlap.mdp.auxiliary.DomainGenerator;
import burlap.mdp.core.action.Action;
import burlap.mdp.core.Domain;
import burlap.mdp.core.StateTransitionProb;
import burlap.mdp.core.TerminalFunction;
import burlap.mdp.core.state.MutableState;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.SADomain;
import burlap.mdp.singleagent.model.RewardFunction;
import burlap.mdp.singleagent.model.statemodel.FullStateModel;

public class forklift implements DomainGenerator{

	public static final String ATT_X = "x";
	public static final String ATT_Y = "y";
	public static final String ATT_D = "d";
	
	public static final String ACTION_FORWARD = "forward";
	public static final String ACTION_BACKWARDS = "backward";
	public static final String ROTATE_CLOCKWISE = "clockwise";
	public static final String ROTATE_COUNTERCLOCKWISE = "counterclockwise";
	
	public static final int NORTH = 0;
	public static final int EAST = 1;
	public static final int SOUTH = 2;
	public static final int WEST = 3;
	
	protected RewardFunction rf;
	protected TerminalFunction tf;
	
	
	public TerminalFunction getTf() {
		return tf;
	}

	public void setTf(TerminalFunction tf) {
		this.tf = tf;
	}

	public RewardFunction getRf() {
		return rf;
	}

	public void setRf(RewardFunction rf) {
		this.rf = rf;
	}
	
	public SADomain generateDomain() {
		
		SADomain domain = new SADomain();
		
		
		
		return null;
	}
	
	public static class FLModel implements FullStateModel
	{

		public State sample(State s, Action a) {
			s = s.copy();
			return move(s, a);
		}

		public State move(State s, Action a) {
			double px = (Double)s.get(ATT_X);
			double py = (Double)s.get(ATT_Y);
			int direction = (Integer)s.get(ATT_D);
			
			String actionName = a.actionName();
			if(actionName.equals(ROTATE_COUNTERCLOCKWISE)){
				direction -= 1;
				if(direction < 0){
					direction += 4;
				}
			}
			else if(actionName.equals(ROTATE_CLOCKWISE)){
				direction += 1;
				if(direction > 3){
					direction -= 4;
				}
			}
			else if(actionName.equals(ACTION_FORWARD)){
				if(direction == 0){
					py += 0.02;
					((MutableState)s).set(ATT_X, px);
				}
				else if(direction == 1){
					px += 0.02;
					((MutableState)s).set(ATT_X, px);
				}
				else if(direction == 2){
					py -= 0.02;
					((MutableState)s).set(ATT_Y, py);
				}
				else{
					px -= 0.02;
					((MutableState)s).set(ATT_Y, py);
				}
			}
			else if(actionName.equals(ACTION_BACKWARDS)){
				if(direction == 0){
					py -= 0.02;
					((MutableState)s).set(ATT_X, px);
				}
				else if(direction == 1){
					px -= 0.02;
					((MutableState)s).set(ATT_X, px);
				}
				else if(direction == 2){
					py += 0.02;
					((MutableState)s).set(ATT_Y, py);
				}
				else{
					px += 0.02;
					((MutableState)s).set(ATT_Y, py);
				}
			}
			return s;
		}

		public List<StateTransitionProb> stateTransitions(State s, Action a) {
			return FullStateModel.Helper.deterministicTransition(this, s, (burlap.mdp.core.action.Action) a);
		}
		
	}
	
	
}
