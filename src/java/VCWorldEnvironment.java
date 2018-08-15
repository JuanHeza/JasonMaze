import java.util.Random;
import jason.asSyntax.*;
import jason.environment.*;
import jason.environment.grid.Location;
import vacuumgoals.MazeGenerator;

import java.util.logging.*;
import java.util.*;

public class VCWorldEnvironment extends Environment {

	private Logger logger = Logger.getLogger("vacuumcleaner." + VCWorldEnvironment.class.getName());
    double A,B,C;
	static List<SearchNode> Wall = new LinkedList<SearchNode>();
	static List<SearchNode> Path = new LinkedList<SearchNode>();
	static List<SearchNode> Visited = new LinkedList<SearchNode>();
	static List<SearchNode> CrossRoads = new LinkedList<SearchNode>();
	static List<SearchNode> Goals = new LinkedList<SearchNode>();
	int WorldEnvironment=51;
	int y=(WorldEnvironment-1)/2;
	int x=(WorldEnvironment-1)/2;
	int [][] maze2;
	int [][] maze3 = new int[WorldEnvironment][WorldEnvironment];
	static SearchNode Goal; 
	static SearchNode Start; 

	public int[][] solution;
	ArrayList<ArrayList<Integer>> arraySucias= new ArrayList<ArrayList<Integer>>();
	public static final int gridSizeDefault = 10;
	public static final int dirtRatio = 3;

	public 
	Random r = new Random();

	BuildingModel model;

	LocalSearch searchEngine;
	
	/** Called before the MAS execution with the args informed in .mas2j */
	@Override
	public void init(String[] args) {

		solution = new int[WorldEnvironment][WorldEnvironment];
	    for (int i = 0; i < WorldEnvironment; i++) {
	      for (int j = 0; j < WorldEnvironment; j++) {
	        solution[i][j] = 0;
	      }
	    }
		int rows = gridSizeDefault;
		int cols = gridSizeDefault;
		if (args.length == 3) {
			try {
				System.out.println(args);
				rows = Integer.parseInt(args[1]);
				cols = Integer.parseInt(args[2]);
			} catch (NumberFormatException e) {
				logger.info("Error in generating the environment. It is required the 2d dimension of the building.");
				logger.info("Proceeds with default configuration");
			}
		}
		logger.info("Initializing the environment...");
		model = new BuildingModel(rows, cols);
		if (args.length > 0 && args[0].equals("gui")) {
			BuildingView view = new BuildingView(model);
			model.setView(view);
		}
		initDirtLocations(rows, cols);

		System.out.println("Goal: ("+Goal.getX()+","+Goal.getY()+")");
        System.out.println("Start: ("+Start.getX()+","+Start.getY()+")");
		//______________________________________________________________________________________________________________________________
		Date now = new Date();
		A = now.getTime();//System.nanoTime();
        System.out.println();        
		initRobotLocation(rows, cols);
		searchEngine = new LocalSearch(model);

		updatePercepts();
		logger.info("Finish initialization ...");
		}

	private void initRobotLocation(int rows, int cols) {
		int rc = Start.getX();
		int rn = Start.getY();
		int Bc = Goal.getX();
		int Bn = Goal.getY();
		model.setAgPos(0, new Location(rc, rn));
		model.setAgPos(1, new Location(Bc, Bn));
		logger.info("Initialized agentR position to: [" + rc + "," + rn + "]");
		logger.info("Initialized agentB position to: [" + Bc + "," + Bn + "]");
	}
	private void initDirtLocations(int rows, int cols) {
		MazeGenerator maze = new MazeGenerator(x, y);
		maze2 = (int[][]) model.getMaze();
		int l=0;
		
		for (int i = 0; i < y; i++) {
			int m=0;
			for (int j = 0; j < x; j++) {
				if((maze.maze[j][i] & 1) == 0) {
					
					model.addDirt(m, l);
					
					Wall.add(new SearchNode(m,l));
					model.addDirt(m+1, l);
					
					Wall.add(new SearchNode(m+1,l));
					
				}else {
					model.addDirt(m, l);
					Wall.add(new SearchNode(m,l));
					model.addDirt2(m+1, l);
				}
				m+=2;
			}
			for (int j = 0; j < WorldEnvironment-1; j++) {
				model.addDirt(WorldEnvironment-1,j);
				
				Wall.add(new SearchNode(WorldEnvironment-1,j));
			}
			m=0;
			for (int j = 0; j < x; j++) {
				if((maze.maze[j][i] & 8) == 0) {
					model.addDirt(m, l);
					
					Wall.add(new SearchNode(m,l));

					model.addDirt(m, l+1);
					
					Wall.add(new SearchNode(m,l+1));

					model.addDirt2(m+1, l);
					model.addDirt2(m+1, l+1);
				}else {
					model.addDirt2(m, l);
					model.addDirt2(m+1, l);
				}
				m+=2;
			}
			
			l+=2;

		}
		for (int j = 0; j < WorldEnvironment; j++) {
			model.addDirt(j, l);
			
			Wall.add(new SearchNode(j,l));
			model.addDirt2(0, 0);
		}
		for(int i =0;i<WorldEnvironment;i++){
			for(int j =0;j<WorldEnvironment;j++){
				if(maze2[j][i]==1){
					maze3[i][j] = 0;
				}else{
					maze3[i][j] = 1;
				}
			}
		}
		Random rnd = new Random();
		int a = (rnd.nextInt(4));
		int b = (rnd.nextInt(4));
		int c = (rnd.nextInt(WorldEnvironment));
		int d = (rnd.nextInt(WorldEnvironment));
        System.out.println();
        if(a != b || c != d) {
        	int QR;
        	int GJ;

        	switch(b) {
	        	case 0: 
	        		GJ = 0; 
	            	model.suck(GJ+1, d);
	            	model.suck(GJ, d);
					Goals.add(new SearchNode(GJ+1,d));
	            	Start = new SearchNode(GJ,d);
	            	break;
	        	case 1: 
	        		GJ = WorldEnvironment-1; //realacionado a X, Y de arriba
	            	model.suck(GJ, d);
	            	model.suck(GJ-1, d);
					Goals.add(new SearchNode(GJ-1,d));
	                Start = new SearchNode(GJ,d);
	                break;
	        	case 2: 
	        		GJ = 0; 
	            	model.suck(d, GJ+1);
	            	model.suck(d, GJ);
					Goals.add(new SearchNode(d,GJ+1));
	                Start = new SearchNode(d,GJ);
	        		break;
	        	case 3: 
	        		GJ = WorldEnvironment-1; //realacionado a X, Y de arriba
	            	model.suck(d, GJ-1);
	            	model.suck(d, GJ);
					Goals.add(new SearchNode(d,GJ-1));
	                Start = new SearchNode(d,GJ);
	        		break;
        	}
        	switch(a) {
	        	case 0: 
	        		QR = 0; 
	            	model.suck(QR, c);
	            	model.suck(QR+1, c);
					Goals.add(new SearchNode(QR,c));
					Goals.add(new SearchNode(QR+1,c));
	                Goal = new SearchNode(QR,c); 
	            	break;
	        	case 1: 
	        		QR = WorldEnvironment-1; //realacionado a X, Y de arriba
	            	model.suck(QR, c);
	            	model.suck(QR-1, c);
					Goals.add(new SearchNode(QR,c));
					Goals.add(new SearchNode(QR-1,c));
	                Goal = new SearchNode(QR,c);
	        		break;
	        	case 2: 
	        		QR = 0; 
	            	model.suck(c,QR+1);
	            	model.suck(c,QR);
					Goals.add(new SearchNode(c,QR));
					Goals.add(new SearchNode(c,QR+1));
	                Goal = new SearchNode(c,QR);
	        		break;
	        	case 3: 
	        		QR = WorldEnvironment-1; //realacionado a X, Y de arriba
	            	model.suck(c, QR-1);
	            	model.suck(c, QR);
					Goals.add(new SearchNode(c,QR));
					Goals.add(new SearchNode(c,QR-1));
	                Goal = new SearchNode(c,QR);
	        		break;
        	}
        }
		int xg = Goal.getX();
	   	int yg = Goal.getY();
	   	int q = Clean(xg,yg);
   		int xs = Start.getX();
   		int ys = Start.getY();
   		int r = Clean(xs,ys);
   		System.out.println("R"+r+"Q"+q);
	}

	/**
	 * update the agent's percepts based on the current state of the world model
	 */
	private void updatePercepts() {
		logger.info("Updating percepts of agents...");

		super.clearPercepts("robota");

		Location lRobot = model.getAgPos(0);
		addPercept("robota", Literal.parseLiteral("at(robota," + lRobot.x + "," + lRobot.y + ")"));

		logger.info("Updating percept of agentR position: " + lRobot.x + "," + lRobot.y);

		// It only updates the percept of current location since robot is blind
		if (model.isDirty(lRobot.x, lRobot.y)) {
			addPercept("robota", Literal.parseLiteral("dirty(" + lRobot.x + "," + lRobot.y + ")"));
			logger.info("Finding dirtiness in: " + lRobot.x + "," + lRobot.y);
		} else {
			addPercept("robota", Literal.parseLiteral("clean(" + lRobot.x + "," + lRobot.y + ")"));
			logger.info("Did not find dirtiness in: " + lRobot.x + "," + lRobot.y);
		}
		//////////////////////////////////////////
		super.clearPercepts("robotb");

		Location kRobot = model.getAgPos(1);
		addPercept("robotb", Literal.parseLiteral("at(robotb," + kRobot.x + "," + kRobot.y + ")"));

		logger.info("Updating percept of agentB position: " + kRobot.x + "," + kRobot.y);

		// It only updates the percept of current location since robot is blind
		if (model.isDirty(kRobot.x, kRobot.y)) {
			addPercept("robotb", Literal.parseLiteral("dirty(" + kRobot.x + "," + kRobot.y + ")"));
			logger.info("Finding dirtiness in: " + kRobot.x + "," + kRobot.y);
		} else {
			addPercept("robotb", Literal.parseLiteral("clean(" + kRobot.x + "," + kRobot.y + ")"));
			logger.info("Did not find dirtiness in: " + kRobot.x + "," + kRobot.y);
		}
	}

	@Override
public boolean executeAction(String agName, Structure action) {
		logger.info("----------------- Open Execution Loop ------------------------");
		logger.info("AgentR: " + agName + " executing action: " + action);

		if (action.getFunctor().equals("move")) {
			Location lRobot = model.getAgPos(0);
			int X = model.getAgAtPos(lRobot);
			SearchNode nextPosition = searchEngine.search(new SearchNode(lRobot.x, lRobot.y),X);
			if(nextPosition!=null) {
				model.moveTowards(nextPosition.getX(), nextPosition.getY());
				model.increaseSolutionCost();
				logger.info("RobotR moving to destination: [" + nextPosition.getX() + "," + nextPosition.getY() + "]");
			}else {
				Date now = new Date();
				B = now.getTime();//System.nanoTime();
				C = (B-A)/ 1000;
				System.out.println("SALIDA ENCONTRADA");
				System.out.println("Tiempo "+C+" Segundos");  
				System.out.println("Goal: ("+Goal.getX()+","+Goal.getY()+")");
				System.out.println("Start: ("+Start.getX()+","+Start.getY()+")");  					
			}
		} else {
			logger.info("Action " + action + " is not implemented!");
			return false;
		}
			///////////////////////////////////////////////////////////////////////////////////////////////////////////
		logger.info("AgentB: " + agName + " executing action: " + action);

		if (action.getFunctor().equals("move")) {
			Location kRobot = model.getAgPos(1);
			int Y = model.getAgAtPos(kRobot);
			SearchNode nextPositionb = searchEngine.search(new SearchNode(kRobot.x, kRobot.y),Y);
			if(nextPositionb!=null) {
				model.moveTowards(nextPositionb.getX(), nextPositionb.getY());
				model.increaseSolutionCost();
				logger.info("RobotB moving to destination: [" + nextPositionb.getX() + "," + nextPositionb.getY() + "]");
			}else {
				Date now = new Date();
				B = now.getTime();//System.nanoTime();
				C = (B-A)/ 1000;
				System.out.println("SALIDA ENCONTRADA");
				System.out.println("Tiempo "+C+" Segundos");  
				System.out.println("Goal: ("+Goal.getX()+","+Goal.getY()+")");
				System.out.println("Start: ("+Start.getX()+","+Start.getY()+")");  					
			}
		} else {
			logger.info("Action " + action + " is not implemented!");
			return false;
		}

		updatePercepts(); // update the agent's percepts for the new
							// state of the world (after this action)
		logger.info("Current solution cost: " + model.getSolutionCost());
		logger.info("----------------- Close Execution Loop ------------------------");
		return true; // in this model, all actions succeed
	}
	
public List<SearchNode> GetLast(int x, int y) {
		for(int i = 2 ; i<Visited.size();i++) {
			SearchNode X = Visited.get(Visited.size()-i); 
			if (IsCrossroad(X.getX(),X.getY())){
				Path.add(X);
				System.out.println("----CrossRoad----"+X.getX()+","+X.getY()+"----");
				System.out.println("----PATH----"+Path.size()+"----");
				for (int k=Path.size();k>0;k--){
					SearchNode Q = Path.get(k-1);
					System.out.println("X"+Q.getX()+"Y"+Q.getY());
				}
				for(SearchNode Obj : CrossRoads){
					int XQ = Obj.getX();
					int YQ = Obj.getY();
					System.out.println("CR "+XQ+","+YQ);
				}
				return Path;
			}
			Path.add(X);
		}
		return null;
}

public void CrossRoads(int x, int y, int z) {
	System.out.println("CrossRoad:"+x+","+y);
	for (int Q = 1 ; Q < z; Q++){
		CrossRoads.add(new SearchNode(x,y));
	}
}
	
	public boolean IsCrossroad(int x, int y) {
		for(SearchNode Obj : CrossRoads){
			int X = Obj.getX();
			int Y = Obj.getY();
			if(x==X && y==Y){	
				CrossRoads.remove(Obj);
				return true;
			}
		}
		return false;
	}
	
	public void Visited(int x, int y) {
		Visited.add(new SearchNode(x,y));
		System.out.println("Visited.size "+Visited.size());
	}

	public boolean IsVisited(int x, int y) {
		for(SearchNode Obj : Visited){
			int X = Obj.getX();
			int Y = Obj.getY();
			if(x==X && y==Y)
				return true;
		}
		return false;
	}
	
	public boolean IsWall(int x, int y) {
		for(SearchNode Obj : Goals){
			int X = Obj.getX();
			int Y = Obj.getY();
			if(x==X && y==Y)
				return false;
		}
		for(SearchNode Obj : Wall){
			int X = Obj.getX();
			int Y = Obj.getY();
			if(x==X && y==Y)
				return true;
		}
		return false;
	}	
	
	public List<SearchNode> IsGoal(List<SearchNode> neighbors) {
		for(SearchNode Obj : neighbors){
			int X = Goal.getX();
			int Y = Goal.getY();
			int x = Obj.getX();
			int y = Obj.getY();
			if(x==X && y==Y) {
				List <SearchNode> G = new LinkedList<SearchNode>();
				G.add(new SearchNode(x,y));
				return G;
			}
		}
		return neighbors;
	}
	
	public boolean GOAL(int x, int y, int Q) {
		int X;
		int Y;
		System.out.println("Q:"+Q);
		switch(Q) {
		case 0:
			X = Goal.getX();
			Y = Goal.getY();
			if(x==X && y==Y) {
				return true;
			}
			break;
		case 1:
			X = Start.getX();
			Y = Start.getY();
			if(x==X && y==Y) {
				return true;
			}
			break;
		}
		return false;
	}
	
	 public int Clean(int x, int y) {
		for(SearchNode Obj : Wall){
			int X = Obj.getX();
			int Y = Obj.getY();
			if(x==X && y==Y)
				Wall.remove(Obj);
				return 1;
			}
		return 0;
	 }
	/** Called before the end of MAS execution */
	@Override
	public void stop() {
		super.stop();
	}
	
}
