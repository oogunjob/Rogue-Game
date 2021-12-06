package game;

public class ItemAction{
	private String name;
	private String type;
	private String actionMessage;
	
	private int actionIntValue; 
    private char actionCharValue;

	// ItemAction constructor
	public ItemAction(String _name, String _type){
		name = _name;
		type = _type;
	}
	
	// sets the name of the Item Action
	public void setName(String _name){
		name = _name;
	}
	
	// gets the name of the Item Action
	public String getName(){
		return name;
	}
	
	// sets the type of the Item Action
	public void setType(String _type){
		type = _type;
	}
	
	// gets the type of the Item Action
	public String getType(){
		return type;
	}
	
	// sets the action message
	public void setActionMessage(String _actionMessage){
		actionMessage = _actionMessage;
	}
	
	// gets the action message
	public String getActionMessage(){
		return actionMessage;
	}
	
	// sets the action int value
	public void setActionIntValue(int _actionIntValue){
		actionIntValue = _actionIntValue;
	}
	
	// gets the action int value
	public int getActionIntValue(){
		return actionIntValue;
	}
	
	// sets the action char value
	public void setActionCharValue(char _actionCharValue){
		actionCharValue = _actionCharValue;
	}
	
	// gets the action char value
	public char getActionCharValue(){
		return actionCharValue;
	}
}
