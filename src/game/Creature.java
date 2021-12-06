package game;
import java.util.ArrayList;

public class Creature extends Displayable{
    // These are public because they get used in both the Monster and Player class.
    protected String name; 
    protected String room;
    protected String serial;
    protected int hp;
    protected int maxHit;
    protected ArrayList<CreatureAction> actions;

    public String getName(){
        return name;
    }

    public void setHp(int _hp){
        hp = _hp;
    }

    public void setMaxHit(int _maxHit){
        maxHit = _maxHit;
    }
    
    public void addAction(CreatureAction action){
        actions.add(action);
    }

    public ArrayList<CreatureAction> getActions(){
        return actions;
    }

    public int getHp(){
        return hp;
    }

    public int getMaxHit(){
        return maxHit;
    }
}