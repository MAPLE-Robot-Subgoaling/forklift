package edu.umbc.cs.forklift;

import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import burlap.mdp.auxiliary.DomainGenerator;
import burlap.mdp.auxiliary.common.SinglePFTF;
import burlap.mdp.auxiliary.stateconditiontest.StateConditionTest;
import burlap.mdp.core.TerminalFunction;
import burlap.mdp.core.action.Action;
import burlap.mdp.core.action.ActionType;
import burlap.mdp.core.action.UniversalActionType;
import burlap.mdp.core.oo.OODomain;
import burlap.mdp.core.oo.propositional.PropositionalFunction;
import burlap.mdp.core.oo.state.MutableOOState;
import burlap.mdp.core.oo.state.OOState;
import burlap.mdp.core.oo.state.ObjectInstance;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.common.GoalBasedRF;
import burlap.mdp.singleagent.common.SingleGoalPFRF;
import burlap.mdp.singleagent.environment.EnvironmentOutcome;
import burlap.mdp.singleagent.model.FactoredModel;
import burlap.mdp.singleagent.model.RewardFunction;
import burlap.mdp.singleagent.model.SampleModel;
import burlap.mdp.singleagent.model.statemodel.SampleStateModel;
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
	public static final String ATT_O = "o";
	public static final String ATT_VM = "vm";
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
	public static final String BOXES_IN_AREA = "depot";

	
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
	static double brakeRotFriction = 5;
	
	public List<Double> goalArea; //xmin,xmax,ymin,ymax
	
	public static int captured = 0; 
	
	public forklift()
	{
	}
	public List<PropositionalFunction> generatePfs(){
		return Arrays.asList(
				(PropositionalFunction)new BoxesInArea(forklift.BOXES_IN_AREA));
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
		OODomain.Helper.addPfsToDomain(domain, this.generatePfs());
	
		domain.addStateClass(CLASS_AGENT, FLAgent.class)
//this will have marginal utility, and should be supported in FLState
//		.addStateClass(CLASS_BLOCK, FLBlock.class)
		.addStateClass(CLASS_WALL, FLBlock.FLWall.class)
		.addStateClass(CLASS_BOX, FLBlock.FLBox.class);
		
		domain.addActionTypes(new UniversalActionType(MOVE_FORWARD), 
				new UniversalActionType(MOVE_BACKWARD),
				new UniversalActionType(ROTATE_CLOCKWISE),
				new UniversalActionType(ROTATE_COUNTERCLOCKWISE),
				new UniversalActionType(BRAKE),
				new UniversalActionType(PICKUP),
				new UniversalActionType(DROP),
				new UniversalActionType(IDLE)
				);
		
		FLModel fmodel = new FLModel(forwardAccel, backwardAccel, rotAccel);
		this.rf = new SingleGoalPFRF(domain.propFunction(forklift.BOXES_IN_AREA));
		this.tf = new SinglePFTF(domain.propFunction(forklift.BOXES_IN_AREA));

		FactoredModel factorModel = new FactoredModel(fmodel, this.rf, this.tf);
		domain.setModel(factorModel);
		
		return domain;
	}
	
	public static class FLModel implements SampleStateModel
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
			double rfric = rotFriction;
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
			double vMag = (Double)agent.get(ATT_VM);
			
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
				rfric = brakeRotFriction;
			}else if(actionName.equals(DROP)){
				b.putDown();
				b = null;				
			}
			else if(actionName.equals(PICKUP))
			{
				if(vx == 0 && vy == 0 && vr == 0)
				{
					
				}
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
			
			if(collisionCheck(s, npx, npy, w, l, direction) == false){
				FLAgent newAgent = new FLAgent(npx, npy, vx, vy, vr, direction, l, w, "agent", (FLBox)agent.get(ATT_B), newVelocity);
				((MutableOOState) s).set(CLASS_AGENT, newAgent);
			}
			else{
				//if collision, zero all velocities and revert to previous position
				//TODO: is it possible to store a previous state's position and jump further backwards? 
				FLAgent newAgent = new FLAgent(px, py, direction, l, w, "agent", (FLBox)agent.get(ATT_B), newVelocity);
				((MutableOOState) s).set(CLASS_AGENT, newAgent);
			}
			return s;
		}

		public State sample(State s, Action a) {
			s = s.copy();
			return move(s, a);
		}

		public boolean terminal(State s) {
			return false;
		}
		
		public boolean collisionCheck(State s, double x, double y, double w, double l, double direction)
		{
			List<ObjectInstance> blocks =  ((MutableOOState) s).objectsOfClass(CLASS_WALL);
			blocks.addAll(((MutableOOState) s).objectsOfClass(CLASS_BOX));
			ArrayList<Line2D.Double> test = new ArrayList<Line2D.Double>(); 
			Double x1, x2, x3, x4, y1, y2, y3, y4;
			Double dx = Math.cos(Math.toRadians(360-direction)) * w/2 + Math.cos(Math.toRadians(450-direction)) * l/2;
			Double dy = Math.sin(Math.toRadians(360-direction)) * w/2 + Math.sin(Math.toRadians(450-direction)) * l/2;
			x1 = x + dx;
			y1 = y + dy;
			x2 = x - dx;
			y2 = y + dy;
			x3 = x + dx;
			y3 = y - dy;
			x4 = x - dx;
			y4 = y - dy;
			test.add(new Line2D.Double(x1, y1, x2, y2));
			test.add(new Line2D.Double(x1, y1, x3, y2));
			test.add(new Line2D.Double(x2, y2, x4, y4));
			test.add(new Line2D.Double(x3, y3, x4, y4));
			for(ObjectInstance block: blocks)
			{
				
				double blockX = (Double)block.get(ATT_X);
				double blockY = (Double)block.get(ATT_Y);
				double blockW = (Double)block.get(ATT_W);
				double blockL = (Double)block.get(ATT_L);
				
				ArrayList<Point2D.Double> forkliftPoints = new ArrayList<Point2D.Double>();
				
				forkliftPoints.add(new Point2D.Double(x1, y1));
				forkliftPoints.add(new Point2D.Double(x2, y2));
				forkliftPoints.add(new Point2D.Double(x3, y3));
				forkliftPoints.add(new Point2D.Double(x4, y4));
				
				ArrayList<Point2D.Double> blockPoints = new ArrayList<Point2D.Double>();
				
				blockPoints.add(new Point2D.Double(blockX, blockY));
				blockPoints.add(new Point2D.Double(blockX, blockY + blockL));
				blockPoints.add(new Point2D.Double(blockX + blockW, blockY));
				blockPoints.add(new Point2D.Double(blockX + blockW, blockY + blockL));
				int sides = 0;
				for(Line2D side: test)
				{
					double deltaX = side.getX2() - side.getX1();
					double slope = 0;
					double vectorY, vectorX;
					if(deltaX != 0){
						if(side.getY2() - side.getY1() != 0){
							slope = (side.getY2() - side.getY1())/deltaX;
							vectorY = (1/slope) * 80;
							vectorX = 80;
						}
						else
						{
							vectorX = 0;
							vectorY = 80;
						}
					}
					else{
						vectorX = 80;
						vectorY = 0;
					}
					
					
					ArrayList<Point2D.Double> projBlock = new ArrayList<Point2D.Double>();
					for(Point2D corner: blockPoints){
						double vectorDotCorner = vectorX * corner.getX() + vectorY * corner.getY();
						double vectorDotVector = vectorX * vectorX + vectorY * vectorY;
						double scalar = vectorDotCorner / vectorDotVector;
						projBlock.add(new Point2D.Double(vectorX * scalar, vectorY * scalar ));
					}
					
					ArrayList<Point2D.Double> projForklift = new ArrayList<Point2D.Double>();
					for(Point2D corner: forkliftPoints){
						double vectorDotCorner = vectorX * corner.getX() + vectorY * corner.getY();
						double vectorDotVector = vectorX * vectorX + vectorY * vectorY;
						double scalar = vectorDotCorner / vectorDotVector;
						projForklift.add(new Point2D.Double(vectorX * scalar, vectorY * scalar ));
					}
					double forkliftMax = 0.0, forkliftMin = 80.0;
					for(Point2D proj: projForklift){
						double mag = Math.sqrt(proj.getX()*proj.getX() + proj.getY()*proj.getY());
						if(mag > forkliftMax)
							forkliftMax = mag;
						if(mag < forkliftMin)
							forkliftMin = mag;
					}
					
					double blockMax = 0.0, blockMin = 80.0;
					for(Point2D proj: projBlock){
						double mag = Math.sqrt(proj.getX()*proj.getX() + proj.getY()*proj.getY());
						if(mag > blockMax)
							blockMax = mag;
						if(mag < blockMin)
							blockMin = mag;
					}
					if((blockMin > forkliftMin && blockMin < forkliftMax) || 
							(blockMax > forkliftMin && blockMax < forkliftMax)||
							(forkliftMax > blockMin && forkliftMax < blockMax)||
							(forkliftMin > blockMin && forkliftMin < blockMax)){
						sides++;
					}
				}
				if(sides == 4)
					return true;
			}
			return false;
		}
		
		public FLBox pickup(State s, double x, double y, double w, double l){
			List<ObjectInstance> boxes = ((MutableOOState)s).objectsOfClass(CLASS_BOX);
			for(ObjectInstance box: boxes)
			{
				Rectangle b = new Rectangle();
				b.setRect((Double)box.get(ATT_X),(Double)box.get(ATT_Y), (Double)box.get(ATT_W), (Double)box.get(ATT_L));
				Rectangle f = new Rectangle();
				f.setRect(x, y, w, l);
				
				if (f.x < b.x + b.width &&
						   f.x + f.width > b.x)
				{
					
				}
			}
			return null;
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

			if(farX < (Double)area.get(0)||
			   nearX > (Double)area.get(1)||
			   nearY > (Double)area.get(2)||
			   farY < (Double)area.get(3))
				return false;
		return true;
		}
	}
	public static class BoxesInArea extends PropositionalFunction{
		private PropositionalFunction pf;
		public BoxesInArea(String name){
			super(name, new String[]{CLASS_BOX});
		}
		
		@Override
		public boolean isTrue(OOState s, String... params) {
			//System.out.println("Is it true?");
			pf = new BoxInArea("boxchecker");
			boolean allInArea = true;
			List<ObjectInstance> boxes = (List<ObjectInstance>)s.objectsOfClass(CLASS_BOX);
			for( ObjectInstance b : boxes)
				if (!pf.isTrue(s, ((FLBlock.FLBox)b).name()))
					allInArea = false;
				
			//System.out.println(allInArea);
			return allInArea;
		}
	}
//	public static class FLTF implements TerminalFunction
//	{
//		
//		private BoxesInArea pf;
//		public FLTF()
//		{
//		pf = new BoxesInArea("prop");
//		}
//		public boolean isTerminal(State s) {
//			return pf.isTrue((OOState)s);
//		}
//		
//	}
	
//	public static class FLRF implements RewardFunction
//	{
//		
//		public FLRF()
//		{
//		}
//		
//
//		public double reward(State s, Action a, State sprime) {
//			FLArea goal = (FLArea)s.get("goal");
//			ArrayList<FLBox> boxes = (ArrayList<FLBox>)s.get(CLASS_BOX); 
//			int captured = 0;
//			for(FLBox b: boxes)
//			{
//				if((Double)b.get(forklift.ATT_X) >(Double) goal.get(0) && 
//						(Double)b.get(forklift.ATT_X) <(Double) goal.get(1) &&
//						(Double)b.get(forklift.ATT_Y) >(Double) goal.get(2) &&
//						(Double)b.get(forklift.ATT_Y) <(Double) goal.get(3)){
//					captured++;
//				}
//			}
//			return captured;
//		}
//		
//	}
	
	
}
