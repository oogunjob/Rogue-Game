package game;

public class Armor extends Item{

    public Armor(String _name, String _room, String _serial) {
        name = _name;
        room = _room;
        serial = _serial;
        character = new Char(']');
    }
}