package game;
import java.util.Stack;

public class Room extends Displayable{
	private String roomName;
    private int width;
    private int height;

    public Room(String _roomName){
        roomName = _roomName;
    }

    public String getRoomName(){
        return roomName;
    }

    public void setWidth(int _width){
        width = _width;
    }

    public void setHeight(int _height){
        height = _height;
    }
    
    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    public void draw(ObjectDisplayGrid objectDisplayGrid, int topHeight, int bottomHeight){
        // draws the room
        Stack<Displayable>[][] objectGrid = objectDisplayGrid.getObjectGrid();
            
        // Iterate over each row in the room
        for(int i = posX; i <= width + posX - 1; i++){
            // Iterate over each column in the room
            for(int j = posY; j <= height + posY - 1; j++){
                
                if(i == posX || i == width + posX - 1 || j == posY || j == height + posY - 1){
                    // Prints the wall of the room
                    objectGrid[i][j].push(new RoomWall());
                    objectDisplayGrid.writeToTerminal(i, j);
                }
                else{
                    // Prints the floor the room
                    objectGrid[i][j].push(new RoomFloor()); 
                    objectDisplayGrid.writeToTerminal(i,j);
                }
            }
        }
    }   

    @Override
    public String toString(){
        String str = "\n  Room " + roomName + "\n";
        str += "    visible: " + visible + "\n";
        str += "    posX: " + posX + "\n";
        str += "    posY: " + posY + "\n";
        str += "    width: " + width + "\n";
        str += "    height: " + height + "\n";
        
        return str;
    }
}
