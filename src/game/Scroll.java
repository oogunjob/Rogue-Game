package game;
import java.util.ArrayList;

public class Scroll extends Item{

    public Scroll(String _name, String _room, String _serial){
        name = _name;
        room = _room;
        serial = _serial;
        character = new Char('?');
        actions = new ArrayList<ItemAction>();
    }

    @Override
    public String toString(){
        String str = "\n    Scroll: \n";
        str += "      posX: " + posX + "\n";
        str += "      posY: " + posY + "\n";

        return str;

    }
}