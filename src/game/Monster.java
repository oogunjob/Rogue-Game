package game;
import java.util.ArrayList;
import java.util.Stack;

public class Monster extends Creature{
    private String type;

    public Monster(String _name, String _room, String _serial){
        name = _name;
        room = _room;
        serial = _serial;
        actions = new ArrayList<CreatureAction>();
    }

    public void setType(String _type){
        type = _type;
        character = new Char(_type.charAt(0));
    }

    public void draw(ObjectDisplayGrid objectDisplayGrid){
        Stack<Displayable>[][] objectGrid = objectDisplayGrid.getObjectGrid();
        
        // draws every monster in the room
        objectGrid[posX][posY].push(this);
        objectDisplayGrid.writeToTerminal(posX, posY);
    }
    
    @Override
    public String toString(){
    String str = "\n    Monster: \n";
    str += "      name: " + name + "\n";
    str += "      room: " + room + "\n";
    str += "      serial: " + serial + "\n";
    str += "      visible: " + visible + "\n";
    str += "      posX: " + posX + "\n";
    str += "      posY: " + posY + "\n";
    str += "      type: " + type + "\n";
    str += "      hp: " + hp + "\n";
    str += "      maxhit: " + maxHit;

    for(CreatureAction action : actions){
        str += action.toString( ) + "\n";
    }
    return str;
    }
}
