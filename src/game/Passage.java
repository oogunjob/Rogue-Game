package game;
import java.util.ArrayList;
import java.util.Stack;

public class Passage extends Displayable{
    private String room1; // source room
	private String room2; // destination room
    
	private ArrayList<Integer> XPositions;
    private ArrayList<Integer> YPositions;
	
	// Passage constructor
	public Passage(String _room1, String _room2){
		room1 = _room1;
		room2 = _room2;
		XPositions = new ArrayList<Integer>();
		YPositions = new ArrayList<Integer>();
		character = new Char('#');
	}

	// Passage constructor for display grid, may or may not be necessary
	public Passage(char _character){
		character = new Char(_character);
	}

	// sets the String values of both rooms
	public void setRooms(String _room1, String _room2){
		room1 = _room1;
		room2 = _room2;
	}
	
	// sets the String value of room 1
	public void setRoom1(String _room1){
		room1 = _room1;
	}
	
	// sets the String value of room 2
	public void setRoom2(String _room2){
		room1 = _room2;
	}
	
	// returns the String value of room 1
	public String getRoom1(){
		return room1;
	}
	
	// returns the String value of room 2
	public String getRoom2(){
		return room2;
	}
	
	// adds the x-position to the list of x-positions
	public void addXPosition(int xPos){
        XPositions.add(xPos);
    }

	// adds the y-position to the list of y-positions
	public void addYPosition(int yPos){
        YPositions.add(yPos);
    }

	public ArrayList<Integer> getXpositions(){
		return XPositions;
	}

	public ArrayList<Integer> getYpositions(){
		return YPositions;
	}

    @Override
    public String toString(){
    	String str = "\n  Passage from room " + room1 + " to " + room2 + "\n";
		str += "    visible: " + visible + "\n";
	
		for (int index = 0; index < XPositions.size(); index++) { 		      
        	str += "    <Xposition , Ypostion>: " + XPositions.get(index) + " , " + YPositions.get(index) + "\n"; 		
      	}
        return str;
    }
	
	public void draw(ObjectDisplayGrid objectDisplayGrid, int topHeight){

		Stack<Displayable>[][] objectGrid = objectDisplayGrid.getObjectGrid();
            
		int size = XPositions.size(); // size of the passages coordinates list

		for(int i = 0; i < size; i++){
			// checks to see if the next coordinate is equivalent to the X or Y position
			if(i + 1 < size){
				if(XPositions.get(i) == XPositions.get(i + 1)){
					
					// Y coordinates change accordingly
					// if the second Y coordinate is greater than the first Y coordinate, then start from the first one
					if(YPositions.get(i + 1) > YPositions.get(i)){

						for(int j = YPositions.get(i); j <= YPositions.get(i + 1); j++){
							
							if(objectGrid[XPositions.get(i)][j + topHeight].peek() instanceof Char){
								// the stack is currently empty so add a passage way floor
								objectGrid[XPositions.get(i)][j + topHeight].push(new Passage('#'));
								objectDisplayGrid.writeToTerminal(XPositions.get(i), j + topHeight);
							}
							else if(objectGrid[XPositions.get(i)][j + topHeight].peek() instanceof RoomWall){
								// the top of the stack currently contains a room wall, so add a door
								objectGrid[XPositions.get(i)][j + topHeight].push(new Passage('+'));
								objectDisplayGrid.writeToTerminal(XPositions.get(i), j + topHeight);
							}
						}
					}

					// if the first Y coordinate is greater than the second Y coordinate, then start from the second one
					else{
						for(int j = YPositions.get(i + 1); j <= YPositions.get(i); j++){

							if(objectGrid[XPositions.get(i)][j + topHeight].peek() instanceof Char){
								objectGrid[XPositions.get(i)][j + topHeight].push(new Passage('#'));
								objectDisplayGrid.writeToTerminal(XPositions.get(i), j + topHeight);
							}
							else if(objectGrid[XPositions.get(i)][j + topHeight].peek() instanceof RoomWall){
								objectGrid[XPositions.get(i)][j + topHeight].push(new Passage('+'));
								objectDisplayGrid.writeToTerminal(XPositions.get(i), j + topHeight);
							}
						}   
					}
				}

				else{
					// X coordinates change accordingly
					// if the second Y coordinate is greater than the first Y coordinate, then start from the first one
					if(XPositions.get(i + 1) > XPositions.get(i)){
						
						for(int j = XPositions.get(i); j <= XPositions.get(i + 1); j++){

							if(objectGrid[j][YPositions.get(i) + topHeight].peek() instanceof Char){
								objectGrid[j][YPositions.get(i) + topHeight].push(new Passage('#'));
								objectDisplayGrid.writeToTerminal(j, YPositions.get(i) + topHeight);
							}
							else if(objectGrid[j][YPositions.get(i) + topHeight].peek() instanceof RoomWall){
								objectGrid[j][YPositions.get(i) + topHeight].push(new Passage('+'));
								objectDisplayGrid.writeToTerminal(j, YPositions.get(i) + topHeight);
							}
						}
					}

					// if the first Y coordinate is greater than the second Y coordinate, then start from the second one
					else{
						for(int j = XPositions.get(i + 1); j <=  XPositions.get(i); j++){

							if(objectGrid[j][YPositions.get(i + 1) + topHeight].peek() instanceof Char){
								objectGrid[j][YPositions.get(i + 1) + topHeight].push(new Passage('#'));
								objectDisplayGrid.writeToTerminal(j, YPositions.get(i + 1) + topHeight);
							}
							else if(objectGrid[j][YPositions.get(i + 1) + topHeight].peek() instanceof RoomWall){
								objectGrid[j][YPositions.get(i + 1) + topHeight].push(new Passage('+'));
								objectDisplayGrid.writeToTerminal(j, YPositions.get(i + 1) + topHeight);
							}
						}
					}
				}
			}
		}
	}
}
