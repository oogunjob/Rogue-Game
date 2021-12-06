package game;
import java.util.ArrayList;
import java.util.Stack;

public class Item extends Displayable{
	protected String name;
    protected String room;
    protected String serial;
    protected int itemIntValue;
	protected ArrayList<ItemAction> actions;

	public Item(){
		character = new Char('w');
	}

	// sets the name of the Item
	public void setName(String _name){
		name = _name;
	}
	
	// gets the name of the Item
	public String getName(){
		return name;
	}
	
	// sets the name of the room
	public void setRoom(String _room){
		room = _room;
	}

	public void addAction(ItemAction action){
        actions.add(action);
    }

	public ArrayList<ItemAction> getAction(){
		return actions;
	}
	
	// gets the room of the Item
	public String getRoom(){
		return room;
	}
	
	// sets the serial number of the item
	public void setSerial(String _serial){
		serial = _serial;
	}
	
	// gets the name of the serial
	public String getSerial(){
		return serial;
	}

	// sets the visiblilty of the Item
	public void setVisibility(int _visible){
		visible = _visible;
	}
	
	// gets the visiblilty of the Item
	public int getVisibility(){
		return visible;
	}
	
	// sets the item value
	public void setItemValue(int _itemIntValue){
		itemIntValue = _itemIntValue;
	}
	
	// gets the item value
	public int getItemValue(){
		return itemIntValue;
	}

	public void draw(ObjectDisplayGrid objectDisplayGrid){
		Stack<Displayable>[][] objectGrid = objectDisplayGrid.getObjectGrid();

		objectGrid[posX][posY].push(this);
		objectDisplayGrid.writeToTerminal(posX, posY);
	}
}
