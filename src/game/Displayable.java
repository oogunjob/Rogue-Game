package game;
public abstract class Displayable{
    protected int posX;
    protected int posY;

    protected int visible = 1;
    protected Char character; 

    public void setPosX(int _posX){
        posX = _posX;
    }

    public void setPosY(int _posY){
        posY = _posY;
    }

    public int getPosX(){
        return posX;
    }

    public int getPosY(){
        return posY;
    }

    public void setVisible(int _visible){
        visible = _visible;
    }

    public Char getCharacter(){
        return character;
    }

    @Override
    public String toString(){
        String str = "";
        str += "   PosX: "+ posX + "\n";
        str += "   posY: "+ posY + "\n";

        return str;
    }
}