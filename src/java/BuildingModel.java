import jason.environment.grid.GridWorldModel;
import jason.environment.grid.Location;

public class BuildingModel extends GridWorldModel {

	public static final int DIRT = 100;
	private int[][] maze;
	private boolean[][] dirty;

	private int dirtyRooms;
	
	private int solutionCost;

	// Default building grid for traversal
	public BuildingModel(int sizex, int sizey) {
		super(sizex, sizey, 2);
		System.out.println("Gridworld dimensions "+sizex+":"+sizey);
		dirty = new boolean[sizex][sizey];
		maze = new int[sizex][sizey];
		dirtyRooms = 0;
		solutionCost = 0;
	}

	//Adds dirt to current position
	public void addDirt(int x, int y) {
		if (!dirty[x][y]) {
			add(DIRT, x, y);
			dirty[x][y] = true;
			dirtyRooms++;
			maze[x][y]=1;
		}
	}
	public void addDirt2(int x, int y) {
			//maze[x][y]=1;
			//dirty[x][y] = false;
		
	}

	//Finds out if the current position is dirty
	public boolean isDirty(int x, int y) {
		return dirty[x][y];
	}

	//Cleans dirt at the given position
	public boolean suck(int x, int y) {
		if (dirty[x][y]) {
			remove(DIRT, x, y);
			dirtyRooms--;
			dirty[x][y] = false;
			return true;
		}
		return false;
	}
	
	public boolean stopAgen(){
		if(dirtyRooms == 0){
			return false;
		}
		return true;
	}
	//Sets the position of agent to new location
	public boolean moveTowards(int x, int y){
		Location lRobot = getAgPos(0);
		lRobot.x = x;
		lRobot.y = y;
		setAgPos(0,lRobot);
		return true;
	}

	//Gets the number of current dirty rooms
	public int getDirtyRooms() {
		return dirtyRooms;
	}
	
	public int[][] getMaze(){
		return maze;
	}
	
	public void increaseSolutionCost() {
		solutionCost++;
	}
	
	public int getSolutionCost() {
		return solutionCost;
	}

}
