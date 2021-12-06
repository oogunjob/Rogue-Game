package game;

public class Sword extends Item{

    public Sword(String _name, String _room, String _serial) {
        name = _name;
        room = _room;
        serial = _serial;
        character = new Char(')');
    }
}