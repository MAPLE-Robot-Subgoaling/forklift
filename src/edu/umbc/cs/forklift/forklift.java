package edu.umbc.cs.forklift;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import burlap.mdp.auxiliary.DomainGenerator;
import burlap.mdp.core.TerminalFunction;
import burlap.mdp.core.action.Action;
import burlap.mdp.core.action.ActionType;
import burlap.mdp.core.action.UniversalActionType;
import burlap.mdp.core.oo.propositional.PropositionalFunction;
import burlap.mdp.core.oo.state.MutableOOState;
import burlap.mdp.core.oo.state.OOState;
import burlap.mdp.core.oo.state.ObjectInstance;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.common.GoalBasedRF;
import burlap.mdp.singleagent.environment.EnvironmentOutcome;
import burlap.mdp.singleagent.model.RewardFunction;
import burlap.mdp.singleagent.model.SampleModel;
import burlap.mdp.singleagent.oo.OOSADomain;
import edu.umbc.cs.forklift.state.FLAgent;
import edu.umbc.cs.forklift.state.FLArea;
import edu.umbc.cs.forklift.state.FLBlock;
import edu.umbc.cs.forklift.state.FLBlock.FLBox;
import edu.umbc.cs.forklift.state.FLBlock.FLWall;
import edu.umbc.cs.forklift.state.FLState;

public class forklift implements DomainGenerator{

	public static final String ATT_X = "x";
	public static final String ATT_Y = "y";
	public static final String ATT_VX = "vx";
	public static final String ATT_VY = "vy";
	public static final String ATT_VR = "vr";
	public static final String ATT_D = "d";
	public static final String ATT_L = "l";
	public static final String ATT_W = "w";
	public static final String ATT_N = "n";
	public static final String ATT_B = "b";
	public static final String PREFIX_ACCEL = "A_";
	public static final String PREFIX_ROTATE_ACCEL = "AR_";
	public static final String MOVE_FORWARD = PREFIX_ACCEL+"forward";
	public static final String MOVE_BACKWARD = PREFIX_ACCEL+"backward";
	public static final String ROTATE_CLOCKWISE = PREFIX_ROTATE_ACCEL+"clockwise";
	public static final String ROTATE_COUNTERCLOCKWISE = PREFIX_ROTATE_ACCEL+"counterclockwise";
	public static final String BRAKE = "brake";
	public static final String IDLE = "idle";
	public static final String PICKUP = "pickup";
	public static final String DROP = "drop";
	public static final String CLASS_AGENT = "agent";
	public static final String CLASS_BLOCK = "block";
	public static final String CLASS_WALL = "wall";
	public static final String CLASS_BOX = "box";
	public static final String CLASS_AREA = "area";
	
	public static final double xBound = 40;
	public static final double yBound = 40;
	
	protected RewardFunction rf;
	protected TerminalFunction tf;
	static double forwardAccel = .05;
	static double backwardAccel = .025;
	static double rotAccel = 2;
	static double friction = .01;
	static double rotFriction = 1;
	static double brakeFriction = .05;
	static double brakeRotFriction = 10;
	
	public List<Double> goalArea; //xmin,xmax,ymin,ymax
	
	public static int captured = 0; 
	
	public forklift()
	{
	}
	public List<PropositionalFunction> generatePfs(){
		return Arrays.asList(
				(PropositionalFunction)new BoxesInArea("goalpf"));
//		return Arrays.asList(
//				new OnPadPF(PF_ON_PAD),
//				new TouchPadPF(PF_TOUTCH_PAD),
//				new TouchSurfacePF(PF_TOUCH_SURFACE),
//				new TouchGroundPF(PF_ON_GROUND, this.physParams.ymin));
	}
	
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
	
	public OOSADomain generateDomain() {
		
		OOSADomain domain = new OOSADomain();
		
		FLModel fmodel = new FLModel(forwardAccel, backwardAccel, rotAccel);
		
//		tf = new FLTF(Boxes, goalArea);
//		rf = new GoalBasedRF(new FLRF(Boxes, goalArea), 1, 0);
		
		domain.setModel(fmodel);
		
		domain.addActionTypes(new UniversalActionType(MOVE_FORWARD), 
				new UniversalActionType(MOVE_BACKWARD),
				new UniversalActionType(ROTATE_CLOCKWISE),
				new UniversalActionType(ROTATE_COUNTERCLOCKWISE),
				new UniversalActionType(BRAKE),
				new UniversalActionType(PICKUP),
				new UniversalActionType(DROP),
				new UniversalActionType(IDLE)
				);
		
		domain.addStateClass(CLASS_AGENT, FLAgent.class)
//this will have marginal utility, and should be supported in FLState
//		.addStateClass(CLASS_BLOCK, FLBlock.class)
		.addStateClass(CLASS_WALL, FLBlock.FLWall.class)
		.addStateClass(CLASS_BOX, FLBlock.FLBox.class);
		
		return domain;
	}
	
//	public class FLAction implements Action
//	{
//		private String name;
//
//		public FLAction(String n)
//		{
//			name = n;
//		}
//		
//		public String actionName() {
//			return name;
//		}
//
//		public Action copy() {
//			return this;
//		}
//		
//	}
	
//	public class FLActionType implements ActionType
//	{
//		String name;
//		List<Action> actions;
//
//		public FLActionType(String name, List<Action> actions)
//		{
//			this.name = name;
//			this.actions = actions;
//		}
//		
//		public List<Action> allApplicableActions(State s) {
//			return actions;
//		}
//
//		public Action associatedAction(String s) {
//			for(Action a: actions)
//			{
//				if(s.equals(a.actionName())){
//					return a;
//				}
//			}
//			
//			return null;
//		}
//
//		public String typeName() {
//			return name;
//		}
//		
//	}
	
	public static class FLModel implements SampleModel
	{
		double forwardAcceleration;
		double backwardAcceleration;
		double rotationalAcceleration;
		
		public FLModel(double fAcc, double bAcc, double rAcc)
		{
			this.forwardAcceleration= fAcc;
			this.backwardAcceleration = bAcc;
			this.rotationalAcceleration = rAcc;
		}

		public State move(State s, Action a) {
			
			double realForwardAccel = 0.0;
			double realClockRotateAccel = 0.0;
			double fric =friction;
			double rfric=rotFriction;
			FLAgent agent = (FLAgent) s.get(CLASS_AGENT);
			double direction = (Double)agent.get(ATT_D);
			double px = (Double)agent.get(ATT_X);
			double py = (Double)agent.get(ATT_Y);
			double vx = (Double)agent.get(ATT_VX);
			double vy = (Double)agent.get(ATT_VY);
			double vr = (Double)agent.get(ATT_VR);
			double w = (Double)agent.get(ATT_W);
			double l = (Double)agent.get(ATT_L);
			FLBox b = (FLBox)agent.get(ATT_B);
			
			String actionName = a.actionName();
			//check for new acceleration actions
			if(actionName.startsWith(PREFIX_ROTATE_ACCEL)){
				
				if(actionName.equals(ROTATE_CLOCKWISE))
					realClockRotateAccel-=rotationalAcceleration;
				else if(actionName.equals(ROTATE_COUNTERCLOCKWISE))
					realClockRotateAccel+=rotationalAcceleration;

			}else if(actionName.startsWith(PREFIX_ACCEL)){
				if(actionName.equals(MOVE_FORWARD)){
					realForwardAccel+=forwardAcceleration;
					//System.out.println(realForwardAccel);
				}
				else if(actionName.equals(MOVE_BACKWARD)){
					realForwardAccel-=backwardAcceleration;
					//System.out.println(realForwardAccel);
				}
			}else if(actionName.equals(BRAKE)){
				fric = brakeFriction;
				rfric=brakeRotFriction;
			}else if(actionName.equals(DROP)){
				b = null;				
			}				
				
			//calculate acceleration based on input
			//friction is modeled as an acceleration
			//in the opposite direction of velocity
			//TODO add dynamic friction as a function of velocity on top
			
			//for some reason this makes it clockwise
			vr -= realClockRotateAccel;
			
			if (vr >= rfric)
				vr -= rfric;
			else if(vr <= -rfric)
				vr += rfric;
			else
				vr = 0;
			direction += vr;
			direction %= 360;
			if(direction < 0)
				direction += 360;
			//now that the real accelerations are calculated
			//calculate the old real velocity
			
			double oldVelocity = (double) Math.sqrt(vx*vx+vy*vy);
			double newVelocity = oldVelocity+realForwardAccel;
			if (newVelocity >=  fric)
				newVelocity -= fric;
			else if(newVelocity <= -fric)
				newVelocity += fric;
			else
				newVelocity = 0;
			vx = (double) (Math.cos(Math.toRadians(360-direction)) * newVelocity);
			vy = (double) (Math.sin(Math.toRadians(360-direction)) * newVelocity);
			double npx = px+vx;
			double npy = py+vy;
			
			if(collisionCheck(s, npx, npy, w, l) == false){
				FLAgent newAgent = new FLAgent(npx, npy, vx, vy, vr, direction, l, w, "agent", (FLBox)agent.get(ATT_B));
				((MutableOOState) s).set(CLASS_AGENT, newAgent);
			}
			else{
				//if collision, zero all velocities and revert to previous position
				//TODO: is it possible to store a previous state's position and jump further backwards? 
				FLAgent newAgent = new FLAgent(px, py, direction, l, w, "agent", (FLBox)agent.get(ATT_B));
				((MutableOOState) s).set(CLASS_AGENT, newAgent);
			}
			return s;
		}

		public EnvironmentOutcome sample(State s, Action a) {
			s = s.copy();
			//what is this?
			//State next = move(s,a);
			return new EnvironmentOutcome(s, a, move(s, a), -1, false);
		}

		public boolean terminal(State s) {
			return false;
		}
		
		public boolean collisionCheck(State s, double x, double y, double w, double l)
		{
			List<ObjectInstance> blocks =  ((MutableOOState) s).objectsOfClass(CLASS_WALL);
			blocks.addAll(((MutableOOState) s).objectsOfClass(CLASS_BOX));
			for(ObjectInstance block: blocks)
			{
				Rectangle b = new Rectangle();
				b.setRect((Double)block.get(ATT_X),(Double)block.get(ATT_Y), (Double)block.get(ATT_W), (Double)block.get(ATT_L));
				Rectangle f = new Rectangle();
				f.setRect(x, y, w, l);
				
				if (f.x < b.x + b.width &&
						   f.x + f.width > b.x &&
						   f.y < b.y + b.height &&
						   f.height + f.y > b.y) {
							return true;
						}
				
			}
			return false;
		}
		
	}
	public static class BoxInArea extends PropositionalFunction{
		public BoxInArea(String name){
			super(name, new String[]{CLASS_BOX});
		}

		@Override
		public boolean isTrue(OOState s, String... params) {
			FLBlock.FLBox box = (FLBlock.FLBox)s.object(params[0]);
			FLArea area = (FLArea)s.object("goal");
			Double nearX = (Double)box.get(forklift.ATT_X);
			Double farX = nearX+(Double)box.get(forklift.ATT_W);
			Double nearY = (Double)box.get(forklift.ATT_Y);
			Double farY = nearY+(Double)box.get(forklift.ATT_L);

			if(nearX < (Double)area.get(0)||
			   farX > (Double)area.get(1)||
			   nearY < (Double)area.get(2)||
			   farY > (Double)area.get(3))
				return false;
		return true;
		}
	}
	public static class BoxesInArea extends PropositionalFunction{
		private BoxInArea pf;
		public BoxesInArea(String name){
			super(name, new String[]{CLASS_BOX});
		}
		
		@Override
		public boolean isTrue(OOState s, String... params) {
			pf = new BoxInArea("boxchecker");
			boolean allInArea = true;
			List<ObjectInstance> boxes = (List<ObjectInstance>)s.objectsOfClass(CLASS_BOX);
			for( ObjectInstance b : boxes)
				if (!pf.isTrue(s, ((FLBlock.FLBox)b).name()))
					allInArea = false;
				
			return allInArea;
		}
	}
	public static class FLTF implements TerminalFunction
	{
		
		private BoxesInArea pf;
		public FLTF()
		{
		pf = new BoxesInArea("prop");
		}
		public boolean isTerminal(State s) {
			return pf.isTrue((OOState)s);
		}
		
	}
//	
//	public static class FLRF implements TerminalFunction
//	{
//		private ArrayList<FLState> Boxes;
//		private List<Double> goal;
//		
//		public FLRF(ArrayList<FLState> Boxes, List<Double> goal)
//		{
//			this.Boxes = Boxes;
//			this.goal = goal;
//		}
//		
//		public boolean isTerminal(State arg0) {
//			int captured = 0;
//			for(FLState b: Boxes)
//			{
//				if((Double)b.get(forklift.ATT_X) > goal.get(0) && 
//						(Double)b.get(forklift.ATT_X) < goal.get(1) &&
//						(Double)b.get(forklift.ATT_Y) > goal.get(2) &&
//						(Double)b.get(forklift.ATT_Y) < goal.get(3)){
//					captured++;
//				}
//			}
//			if(captured > forklift.captured)
//			{
//				forklift.captured = captured;
//				return true;
//			}
//			return false;
//		}
//		
//	}
//	
//	
}
