package game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.swing.JFrame;

public class ObjectDisplayGrid extends JFrame implements KeyListener, InputSubject {

    private static final String CLASSID = ".ObjectDisplayGrid";

    private static game.asciiPanel.AsciiPanel terminal;

    private Stack<Displayable>[][] objectGrid = null;
    private List<InputObserver> inputObservers = null;

    private static int height;
    private static int width;
    
    private static boolean gameOver = false; // used to indicate if the game is still running
    private static boolean help = false; // used to indicated if the player has requested what a certain command does
    private static boolean endGame = false; // used to indicate if the player has attempted to end the game
    private static boolean scroll = false; // used to indicated if a player wants to read a scroll
    private static boolean drop = false; // used to indicate that a player wants to drop an iteme
    private static boolean weapon = false; // used to indicate that a player wants to weild a weapon
    private static boolean armor = false; // used to indicate if the player wants to wear armor

    public ObjectDisplayGrid(int _width, int _height) {
        width = _width;
        height = _height + (Dungeon.getInstance().getBottomHeight() * 2); // could just pass bottom height to constructor

        terminal = new game.asciiPanel.AsciiPanel(width, height);
        objectGrid = new Stack[width][height];
        
        for(int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                objectGrid[i][j] = new Stack<Displayable>();
                objectGrid[i][j].push(new Char(' '));
            }
        }

        initializeDisplay();

        super.add(terminal);
        super.setSize(width * 9, height * 16);
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // super.repaint();
        // terminal.repaint( );
        super.setVisible(true);
        terminal.setVisible(true);
        super.addKeyListener(this);
        inputObservers = new ArrayList<>();
        super.repaint();
        terminal.addKeyListener(this);
    }

    public Stack<Displayable>[][] getObjectGrid() { 
        return objectGrid;
    }

    @Override
    public void registerInputObserver(InputObserver observer) {
        inputObservers.add(observer);
    }


    @Override
    public void keyTyped(KeyEvent e) {
        KeyEvent keypress = (KeyEvent) e;
        notifyInputObservers(keypress.getKeyChar());

        Player player = Dungeon.getInstance().getPlayer(); // player
    
        // if the game is not over, allow the player to still run commands
        if(!gameOver){
            // if the help button was pressed, display the command that the player needs more information about
            
            if(help){
                String control = "";

                switch (keypress.getKeyChar()) {
                    case 'h': control += "h = move left";
                              break;
                    case 'j': control += "j = move down";
                              break;
                    case 'k': control += "k = move up";
                              break;
                    case 'l': control += "l = move down";
                              break;
                    case 'c': control += "c = change armor";
                              break;
                    case 'p': control += "p = pick up item";
                              break;
                    case 'd': control += "d = drop item";
                              break;
                    case 'i': control += "i = display inventory";
                              break;
                    case 'r': control += "r = read scroll";
                              break;
                    case 'T': control += "T = take out weapon";
                              break;
                    case 'w': control += "w = wear armor";
                              break;
                    case 'E': control += "E = End game";
                              break;
                    default: control += "Invalid control";
                             break;
                }
                player.displayInfo(this, control);

                help = false;
            }
            
            // checks if the player has attempted to end the game
            else if(endGame){
                // if the player opts to end the game, end the game
                if(keypress.getKeyChar() == 'Y' || keypress.getKeyChar() == 'y'){
                    player.displayInfo(this, "Game over.");
                    gameOver = true;
                }
                // if the player opts to continue the game, continue the game
                else if(keypress.getKeyChar() == 'N' || keypress.getKeyChar() == 'n'){
                    endGame = false;
                    player.displayInfo(this, "");
                }
                // assumes the player wants to continue playing the game
                else{
                    endGame = false;
                    player.displayInfo(this, "");
                }
            }

            else if(weapon){
                char ch = keypress.getKeyChar();
                
                // chceks if the charcter entered is a digit
                if (ch >= '0' && ch <= '9'){
                    int index = Character.getNumericValue(ch);
                    player.useWeapon(this, index);
                }
                weapon = false; 
            }

            else if(armor){
                char ch = keypress.getKeyChar();
                
                // chceks if the charcter entered is a digit
                if (ch >= '0' && ch <= '9'){
                    int index = Character.getNumericValue(ch);
                    player.wearArmor(this, index);
                }
                armor = false;             
            }

            // checks which scroll the player wants to read
            else if(scroll){
                char ch = keypress.getKeyChar();
                
                // chceks if the charcter entered is a digit
                if (ch >= '0' && ch <= '9'){
                    int index = Character.getNumericValue(ch);
                    player.read(this, index);
                }
                scroll = false;
            }

            // checks which item the player wants to drop
            else if(drop){
                char ch = keypress.getKeyChar();

                // chceks if the charcter entered is a digit
                if (ch >= '0' && ch <= '9'){
                    int index = Character.getNumericValue(ch);
                    player.drop(this, index);
                }
                drop = false;
            }

            else if(keypress.getKeyChar() == 'w'){
                armor = true;
            }

            // changes the player's armor
            else if(keypress.getKeyChar() == 'c'){
                player.changeArmor(this);
            }

            // moves the player left
            else if(keypress.getKeyChar() == 'h'){
                gameOver = player.move(this, -1, 0, gameOver);
            }

            // moves the player right
            else if(keypress.getKeyChar() == 'l'){
                gameOver = player.move(this, 1, 0, gameOver);
            }

            // moves the player up
            else if(keypress.getKeyChar() == 'k'){
                gameOver = player.move(this, 0, -1, gameOver);
            }

            // moves the player down
            else if(keypress.getKeyChar() == 'j'){
                gameOver = player.move(this, 0, 1, gameOver);
            }

            // picks up an item
            else if(keypress.getKeyChar() == 'p'){
                player.pickUp(this);
            }

            // drops an item
            else if(keypress.getKeyChar() == 'd'){
                drop = true;
            }

            // displays the pack
            else if(keypress.getKeyChar() == 'i'){
                player.displayPack(this);
            }

            else if(keypress.getKeyChar() == '?'){
                // displays the controls, ask about this
                String controls = "h, j, k, l, c, p, d, i, r, T, w, E";
                player.displayInfo(this, controls);
            }

            else if(keypress.getKeyChar() == 'H'){
                help = true;
            }

            else if(keypress.getKeyChar() == 'E'){
                endGame = true;
                player.displayInfo(this, "Are you sure you want to end the game? Enter Y to confirm, N to cancel.");
            }
            
            else if(keypress.getKeyChar() == 'r'){
                scroll = true;
            }

            else if(keypress.getKeyChar() == 'T'){
                weapon = true;
            }
        }
    }

    private void notifyInputObservers(char ch) {
        for (InputObserver observer : inputObservers) {
            observer.observerUpdate(ch);
        }
    }

    // we have to override, but we don't use this
    @Override
    public void keyPressed(KeyEvent even) {
    }

    // we have to override, but we don't use this
    @Override
    public void keyReleased(KeyEvent e) {
    }

    public final void initializeDisplay() {
        terminal.repaint();
    }

    public void fireUp() {
        if (terminal.requestFocusInWindow()) {
            System.out.println(CLASSID + ".ObjectDisplayGrid(...) requestFocusInWindow Succeeded");
        } else {
            System.out.println(CLASSID + ".ObjectDisplayGrid(...) requestFocusInWindow FAILED");
        }
    }

    public void addObjectToDisplay(Displayable object){
    }

    // writes character to terminal given x and y coordinates
    public void writeToTerminal(int x, int y){
        char ch;
        // if the Displayable is already an instance of Char, use the getChar() method on it
        if(objectGrid[x][y].peek() instanceof Char){
            Char character = (Char) objectGrid[x][y].peek();
            ch = character.getChar();
        }
        else{
            ch = objectGrid[x][y].peek().getCharacter().getChar(); 
        }

        terminal.write(ch, x, y);
        terminal.repaint();
    }
}
