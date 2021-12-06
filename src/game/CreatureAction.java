package game;

public class CreatureAction{
	private String name;
	private String type;
	private String actionMessage;
    private int actionIntValue;
	private char actionCharValue;

	public CreatureAction(String _name, String _type){
		name = _name;
		type = _type;
	}

	// gets the name of the Creature Action
	public String getName(){
		return name;
	}

	// sets the action char value
	public void setActionCharValue(char _actionCharValue){
		actionCharValue = _actionCharValue;
	} 

	public char getActionCharValue(){
		return actionCharValue;
	}
	
	// gets the type of the Creature Action
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

	public String toString(){
        String str = "\n      Creature Action: \n";
        str += "        name: " + name + "\n";
		str += "        type: " + type + "\n";
		
		if (actionMessage != null){
			str += "        ActionMessage: " + actionMessage + "\n";
		}
		
		if (actionIntValue != 0){
			str += "        ActionIntValue: " + actionIntValue + "\n";
		}
		
		return str;
	}
}
