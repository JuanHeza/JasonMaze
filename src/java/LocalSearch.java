/*
 * Simple random local search for vacuum cleaner project
 * Author: Romeo Sanchez
 */
import java.util.LinkedList;
import java.util.List;


public class LocalSearch {
	VCWorldEnvironment VCW = new VCWorldEnvironment();
	List<SearchNode> unvisited = new LinkedList<SearchNode>();
	List<SearchNode> Path = new LinkedList<SearchNode>();
	List<SearchNode> PBlue = new LinkedList<SearchNode>();
	List<SearchNode> PRed = new LinkedList<SearchNode>();
	List<SearchNode> Way = new LinkedList<SearchNode>();
	List<SearchNode> WBlue = new LinkedList<SearchNode>();
	List<SearchNode> WRed = new LinkedList<SearchNode>();

	private BuildingModel bmodel;

	
	public LocalSearch(BuildingModel model){
		bmodel = model;
	}
	
	public SearchNode search(SearchNode currentNode,int Q){
		if(VCW.GOAL(currentNode.getX(),currentNode.getY(),Q)) {
			return null;
		}
		if(Q == 0){
				System.out.println("RED 1");

			Path = PRed;
		}else{
				System.out.println("BLUE 1");

			Path = PBlue;
		}
		if(!Path.isEmpty()) {
				System.out.println("GREEN");
			SearchNode X = Path.get(0);
			Path.remove(0);
		 	return X;
		}else {
			VCW.Visited(currentNode.getX(),currentNode.getY());
		 	Way = getNeighborHood(currentNode);
		 	if (Way.size()>0) {
					if(Q == 0){
						System.out.println("RED 2");
						WRed = Way;
					}else{
						System.out.println("BLUE 2");
						WBlue = Way;				
					}
		 		SearchNode X = Way.get(0);
				return X;
		 	}else {
				if(Path.isEmpty()) {
					Path = Retorno(currentNode.getX(),currentNode.getY());
				}
				System.out.println("PATH "+Path.isEmpty());
				System.out.println("PATH "+Path.size());
				if(Q == 0){
					System.out.println("RED 3");
					PRed = Path;
				}else{
					System.out.println("BLUE 3");
					PBlue = Path;
				}
				SearchNode X = Path.get(0);
				Path.remove(0);	
			 	return X;
		 	}
		// 	Way.remove(0);
		 }
	}
	private List<SearchNode>  Retorno(int x, int y){
		return VCW.GetLast(x,y);
	}
	private List<SearchNode> getNeighborHood(SearchNode currentNode){
		List<SearchNode> neighbors = new LinkedList<SearchNode>();
		int x = currentNode.getX();
		int y = currentNode.getY();
			if(y>0){
				if(!VCW.IsVisited(x,y-1)&&!VCW.IsWall(x,y-1))
					neighbors.add(new SearchNode(x,y-1));	
			}
			if(x>0){
				if(!VCW.IsVisited(x-1,y)&&!VCW.IsWall(x-1,y))
					neighbors.add(new SearchNode(x-1,y));	
			}
			if(y<bmodel.getWidth()-1){
				if(!VCW.IsVisited(x,y+1)&&!VCW.IsWall(x,y+1))
					neighbors.add(new SearchNode(x,y+1));	
			}
			if(x<bmodel.getWidth()-1){
				if(!VCW.IsVisited(x+1,y)&&!VCW.IsWall(x+1,y))
					neighbors.add(new SearchNode(x+1,y));	
			}
			if(neighbors.size()>1) {
				VCW.CrossRoads(x,y,neighbors.size());
			}
			neighbors = VCW.IsGoal(neighbors);
		System.out.println("X"+x+"Y"+y);
		return neighbors;
	}
}
