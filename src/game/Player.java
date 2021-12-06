package game;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

class Player extends Creature{
    private int hpMoves;
    private ArrayList<Item> items;
    private boolean packFlag = false;
    private int numMoves;
    private boolean hallucination;
    private int hallucinationSteps;
    
    // indicates wheter a player is weilding a sword or armor
    private Sword weapon;
    private Armor armor;

    private int weaponHit;

	public Player(String _name, String _room, String _serial){
        name = _name;
        room = _room;
        serial = _serial;
        
        actions = new ArrayList<CreatureAction>();
        items = new ArrayList<Item>();
        character = new Char('@');
        
        numMoves = 0;
        weaponHit = 0;
        hallucination = false;
        hallucinationSteps = 0;
    }

    public void setPackFlag(boolean flag){
        packFlag = flag;
    }
    
    public void setHpMoves(int _hpMoves){
        hpMoves = _hpMoves;
    }

    public void addItem(Item item) {
        items.add(item);
    }

	public ArrayList<Item> getItemList(){
		return items;
	}

    public void draw(ObjectDisplayGrid objectDisplayGrid){
        Stack<Displayable>[][] objectGrid = objectDisplayGrid.getObjectGrid();

        objectGrid[posX][posY].push(this);
        objectDisplayGrid.writeToTerminal(posX, posY);

        // displays player's HP, score, and info to the terminal
        displayHP(objectDisplayGrid);
        displayScore(objectDisplayGrid);
        displayInfo(objectDisplayGrid, "");

    }

    public void displayHP(ObjectDisplayGrid objectDisplayGrid) {
        Stack<Displayable>[][] objectGrid = objectDisplayGrid.getObjectGrid();

        // writes HP tag to display grid
        for (int i = 0; i < ("HP: ".length()); i++){
            objectGrid[i][0].push(new Char("HP: ".charAt(i)));
            objectDisplayGrid.writeToTerminal(i, 0);
        }

        // clears the current HP already being displayed
        for(int k = 0; k < 3; k++){
            objectGrid[k + 4][0].push(new Char(" ".charAt(0)));
            objectDisplayGrid.writeToTerminal(k + 4, 0);
        }
            
        // writes the current HP to the terminal
        for(int j = 0; j < String.valueOf(hp).length(); j++){
            objectGrid[j + 4][0].push(new Char(String.valueOf(hp).charAt(j)));
            objectDisplayGrid.writeToTerminal(j + 4, 0);
        }
    }

    public void displayScore(ObjectDisplayGrid objectDisplayGrid){
        Stack<Displayable>[][] objectGrid = objectDisplayGrid.getObjectGrid();

        // writes Score tag to display grid
        for (int i = 0; i < ("Score: 0".length()); i++){
            objectGrid[i + 8][0].push(new Char("Score: 0".charAt(i)));
            objectDisplayGrid.writeToTerminal(i + 8, 0);
        }
    }

    public void displayInfo(ObjectDisplayGrid objectDisplayGrid, String info){
        Stack<Displayable>[][] objectGrid = objectDisplayGrid.getObjectGrid();
        int width = Dungeon.getInstance().getWidth();
        int height = Dungeon.getInstance().getGameHeight() + Dungeon.getInstance().getBottomHeight();

        // writes info tag to display grid
        for(int i = 0; i < ("Info: ".length()); i++){
            objectGrid[i][height].push(new Char("Info: ".charAt(i)));
            objectDisplayGrid.writeToTerminal(i, height);
        }
        
        // clears the current info already being displayed
        for(int i = 0; i < width - 6; i++){
            objectGrid[i + 6][height].push(new Char(" ".charAt(0)));
            objectDisplayGrid.writeToTerminal(i + 6, height);
        }

        // prints the new info to the display grid
        for(int i = 0; i < info.length(); i++){
            objectGrid[i + 6][height].push(new Char(info.charAt(i)));
            objectDisplayGrid.writeToTerminal(i + 6, height);
        }
    }

    public void displayPack(ObjectDisplayGrid objectDisplayGrid){
        Stack<Displayable>[][] objectGrid = objectDisplayGrid.getObjectGrid();
        int height = Dungeon.getInstance().getGameHeight() + Dungeon.getInstance().getBottomHeight() - 2; // height of where to display player's plack

        // displays the the player's pack if it not already visible
        if(!packFlag){
            int k;
            for(k = 0; k < ("Pack:".length()); k++){
                objectGrid[k][height].push(new Char("Pack: ".charAt(k)));
                objectDisplayGrid.writeToTerminal(k, height);
            }

            for(int j = 0; j < items.size(); j++){
                objectGrid[j + k][height].push(items.get(j).getCharacter());
                objectDisplayGrid.writeToTerminal(j + k, height);
                
                // if item is an instance of sword or armor
                // do annother for loop, and for the amount of characters in the item int value, write to object display grid and update k
                if(items.get(j) instanceof Sword || items.get(j) instanceof Armor){
                    String itemValue = String.valueOf(items.get(j).getItemValue());
                    
                    for(int i = 0; i < itemValue.length(); i++){
                        objectGrid[j + ++k][height].push(new Char(itemValue.charAt(i)));
                        objectDisplayGrid.writeToTerminal(j + k, height);
                    }
                }
            }

            packFlag = true;
        }

        // clears the player's pack from the display grid
        else{
            int i = 0;
            Char chr = (Char) objectGrid[i][height].peek();
            while(chr.getChar() != ' '){
                objectGrid[i][height].pop();
                objectDisplayGrid.writeToTerminal(i, height);
                i += 1;
                chr = (Char) objectGrid[i][height].peek();
            }

            packFlag = false;
        }
    }

    public boolean move(ObjectDisplayGrid objectDisplayGrid, int x, int y, boolean gameOver){
        Stack<Displayable>[][] objectGrid = objectDisplayGrid.getObjectGrid();
        
        // checks if the next positon is empty space or a room wall
        if(!(objectGrid[posX + x][posY + y].peek() instanceof Char) && !(objectGrid[posX + x][posY + y].peek() instanceof RoomWall)  || (hallucination && !(objectGrid[posX + x][posY + y].peek() instanceof RoomWall))){
            
            // if the player attempts to land on a monster
            if(objectGrid[posX + x][posY + y].peek() instanceof Monster){
                Random rand = new Random();
                Monster monster = (Monster) objectGrid[posX + x][posY + y].peek();
    
                rand.nextInt(maxHit + 1);
                
                int playerHit = rand.nextInt(maxHit + 1) + weaponHit;
                int monsterHit = monster.getHp() > 0 ? rand.nextInt(monster.getMaxHit() + 1) : 0;

                // checks if the player is wearing armor
                if(armor != null){
                    // if the monster's hit breaks the armor, remove the armor from the player
                    if(monsterHit > armor.getItemValue()){
                        hp -= monsterHit - armor.getItemValue();

                        items.remove(items.indexOf(armor) + 1); // indicates the weapon is no longer being wielded
                        items.remove(armor); // removes the armor from player's inventory
                        armor = null;
                    }
                    // if the monster's hit damages the armor
                    else{
                        armor.setItemValue(armor.getItemValue() - monsterHit); // if the monster's hit was less than the current value, remove it from the item's current value
                    }
                }

                // if the player is currently not wearing armor, remove it from the player's current hp
                else{
                    hp -= monsterHit; // sets player's HP
                }
                
                // if the HP of the player is less than or equal to 0, set the player's HP equal to 0
                if(hp <= 0)
                    hp = 0;

                monster.setHp(monster.getHp() - playerHit); // sets monster's hp
                
                displayInfo(objectDisplayGrid, "You inflcited " + String.valueOf(playerHit) + " damage, Monster inflicted " + String.valueOf(monsterHit) + " damage!");
                displayHP(objectDisplayGrid);

                // if the creature is able to teleport after being hit, move it to another location in the dungeon
                for(CreatureAction action : monster.getActions()){
                    if(action.getName().equalsIgnoreCase("Teleport")){
                        displayInfo(objectDisplayGrid, action.getActionMessage());
                        
                        // removes itself from the current stack position and redraws what was previously there
                        objectGrid[monster.getPosX()][monster.getPosY()].pop();
                        objectDisplayGrid.writeToTerminal(monster.getPosX(), monster.getPosY());
                        
                        // sets the monster's new X and Y position
                        monster.setPosX(30);
                        monster.setPosY(18);
                        
                        // pushes itself on to the new stack position
                        objectGrid[monster.getPosX()][monster.getPosY()].push(monster);
                        objectDisplayGrid.writeToTerminal(monster.getPosX(), monster.getPosY());
                    } 
                }

                // if the player has a dropPack action, drop an item from the player's inventory
                for(CreatureAction action : actions){
                    if(action.getName().equalsIgnoreCase("DropPack")){
                        if(!items.isEmpty()){
                            drop(objectDisplayGrid, 1); // drops an item from the player's inventory
                            displayInfo(objectDisplayGrid, action.getActionMessage()); // displays drop pack action message
                        }
                    }
                }

                // checks if the player is still alive
                if(hp == 0){
                    // ends the game
                    character = new Char('+'); // changes the player's character to '+'
                    draw(objectDisplayGrid); // redraws the player to the screen

                    for(CreatureAction action : actions){
                        if(action.getName().equalsIgnoreCase("EndGame")){
                            displayInfo(objectDisplayGrid, action.getActionMessage());
                            return true;
                        } 
                    }

                    displayInfo(objectDisplayGrid, "Monster inflicted " + String.valueOf(monsterHit) + " damage. Game over. You lose!");
                    return true;
                }

                // checks if the monster is still alive
                if(monster.getHp() <= 0){
                    
                    displayInfo(objectDisplayGrid, "You have killed " + monster.getName() + "!");

                    // removes the monster from the dungeon and display grid
                    for(CreatureAction action : monster.getActions()){
                        if(action.getName().equalsIgnoreCase("Remove")){
                            
                            objectGrid[posX + x][posY + y].remove(2);
                            Dungeon.getInstance().getMonsterList().remove(monster);

                            // redraws the dungeon
                            objectDisplayGrid.writeToTerminal(posX + x, posY + y);
                        } 
                    }
                }

                // game is not over yet, because player's HP is still greater than 0
                return false;
            }

            else{
                // if the player is hallucinating, increment the number of hallucination steps
                if(hallucination){
                    hallucinationSteps++;
                    
                    // if the hallucination steps has matched the number of max hallucination steps, turn off hallucination
                    if(hallucinationSteps == 5){
                        hallucination = false;
                        hallucinate(objectDisplayGrid, hallucination);
                        displayInfo(objectDisplayGrid, "You are no longer hallucinating!");
                    }
                }

                // removes itself from the current stack position and redraws what was previously there
                objectGrid[posX][posY].pop();
                objectDisplayGrid.writeToTerminal(posX, posY);
                
                // sets the player's new X and Y position
                setPosX(posX + x);
                setPosY(posY + y);
                
                // pushes itself on to the new stack position
                objectGrid[posX][posY].push(this);
                objectDisplayGrid.writeToTerminal(posX, posY);

                numMoves++; // increments the number of moves that a player has made
                
                // if the player has moved a number of times equivalent to hpMoves, increment the player's hp by one and reset number of moves
                if(numMoves == hpMoves){
                    hp++;
                    numMoves = 0;
                    displayHP(objectDisplayGrid);
                }

                // game is not over yet, because player's HP is still greater than 0
                return false;
            }      
        }

        // game is not over yet, because player's HP is still greater than 0
        return false;
    }

    public void pickUp(ObjectDisplayGrid objectDisplayGrid) {
        Stack<Displayable>[][] objectGrid = objectDisplayGrid.getObjectGrid();
        
        // checks if the player is standing on an item
        if(objectGrid[posX][posY].get(2) instanceof Item){
            Item item = (Item) objectGrid[posX][posY].get(2);
            items.add(item); // adds the item to the player's pack

            // removes the item from its current position in the room and redraws the floor
            objectGrid[posX][posY].remove(2);
            objectDisplayGrid.writeToTerminal(posX, posY);

            // prints the item that the player has picked up
            String info = "You have picked up " + item.getName() + ".";

            // displays the item to the board
            displayInfo(objectDisplayGrid, info);
        }
    }

    public void drop(ObjectDisplayGrid objectDisplayGrid, int index) {
        
        // if the index number is not valid, print "invalid position of item"
        if((index - 1 < 0) || index - 1 > items.size() - 1 || items.get(index - 1).getCharacter().getChar() == 'w'){
            displayInfo(objectDisplayGrid, "Invalid index");
            return;
        }

        Stack<Displayable>[][] objectGrid = objectDisplayGrid.getObjectGrid();

        // checks if the player is standing on a room floor or passage
        if(objectGrid[posX][posY].get(1) instanceof RoomFloor || objectGrid[posX][posY].get(1) instanceof Passage){

            // checks if the item is the current sword being weilded
            if(items.get(index - 1) == weapon){
                items.remove(index); // indicates that the sword is no no longer being used
                weapon = null;
            }

            // checks if the item is the current armor being used
            else if(items.get(index - 1) == armor){
                items.remove(index); // indicates that the armor is no no longer being used
                armor = null;
            }

            objectGrid[posX][posY].add(2, items.get(index - 1));

            // removes the item from the player's pack and redraws the spot
            items.remove(index - 1);
            objectDisplayGrid.writeToTerminal(posX, posY);
        }
    }

    public void read(ObjectDisplayGrid objectDisplayGrid, int index) {

        // if the index number is not valid, print "invalid position of scroll"
        if((index - 1 < 0) || index - 1 > items.size() - 1 || !(items.get(index - 1) instanceof Scroll)){
            displayInfo(objectDisplayGrid, "Invalid index of scroll");
            return;
        }

        // prints the info of the scroll
        String info = items.get(index - 1).getAction().get(0).getActionMessage() + ".";
        displayInfo(objectDisplayGrid, info);

        // if the scroll is a hallucination scroll, activate hallucinations
        if(items.get(index - 1).getAction().get(0).getName().equalsIgnoreCase("Hallucinate")){
            hallucination = true;
            hallucinate(objectDisplayGrid, hallucination);
        }

        // if the scroll is weakened armor, add it to the player's inventory
        if(items.get(index - 1).getAction().get(0).getName().equalsIgnoreCase("BlessArmor")){
            Armor armor = new Armor("BlessArmor", room, "1");
            armor.setItemValue(0);
            items.add(armor);
        }
        
        // removes the scroll from the player's pack
        items.remove(index - 1);
    }

    public void useWeapon(ObjectDisplayGrid objectDisplayGrid, int index) {

        // if the index number is not valid, print "invalid position of weapon"
        if((index - 1 < 0) || index - 1 > items.size() - 1 || !(items.get(index - 1) instanceof Sword)){
            displayInfo(objectDisplayGrid, "Invalid index of sword");
        }

        // equips the player with a sword
        else{
            // checks if a weapon is already being wielded
            if(weapon != null){
                items.remove(items.indexOf(weapon) + 1); // indicates the weapon currently being used in no longer equipped
            }

            weapon = (Sword) items.get(index - 1); // sets the player's current weapon equal to the weapon in the pack
            items.add(index, new Item()); // indicates that the current weapon is being wielded

            weaponHit = weapon.getItemValue(); // adds the weapon's int value to the player's hit

            displayInfo(objectDisplayGrid, "You have equipped " + weapon.getName()); // informs the player which weapon is equipped
        }
    }

    public void wearArmor(ObjectDisplayGrid objectDisplayGrid, int index){
        // if the index number is not valid, print "invalid position of armor"
        if((index - 1 < 0) || index - 1 > items.size() - 1 || !(items.get(index - 1) instanceof Armor)){
            displayInfo(objectDisplayGrid, "Invalid index of armor");
        }

        // equips the player with armor
        else{
            // checks if a armor is already being used
            if(armor != null){
                items.remove(items.indexOf(armor) + 1); // indicates the armor currently being used in no longer equipped
            }

            armor = (Armor) items.get(index - 1); // sets the player's current armor equal to the armor in the pack
            items.add(index, new Item()); // indicates that the current armor is being used

            displayInfo(objectDisplayGrid, "You have equipped " + armor.getName()); // informs the player which weapon is equipped
        }
    }

    public void changeArmor(ObjectDisplayGrid objectDisplayGrid){
        
        // if the player is currently not equipped in armor, display an error message
        if(armor == null){
            displayInfo(objectDisplayGrid, "You are currently not equipped in armor.");
        }

        // removes the armor from the player
        else{
            items.remove(items.indexOf(armor) + 1);
            armor = null;
            displayInfo(objectDisplayGrid, "You have unequppied armor.");
        }
    }

    public void hallucinate(ObjectDisplayGrid objectDisplayGrid, boolean hullucination){
        
        Stack<Displayable>[][] objectGrid = objectDisplayGrid.getObjectGrid();
        
        // gets the attributes of the room
        Room room = Dungeon.getInstance().getRoomList().get(0);

        int width = room.getWidth();
        int height = room.getHeight();
        int roomX = room.getPosX();
        int roomY = room.getPosY();

        // if the player is hallucinating, change the display characters of the displayables in the dungeon
        if(hullucination){
            
            for(int i = roomX; i <= width + roomX - 1; i++){
                for(int j = roomY; j <= height + roomY - 1; j++){

                    // for every position that is not a player, change the displayable to something else
                    if(!(objectGrid[i][j].peek() == this) && !(objectGrid[i][j].peek() instanceof RoomWall)){
                        objectGrid[i][j].push(new Char('M'));
                        objectDisplayGrid.writeToTerminal(i, j);
                    }
                    
                    // add a new Char M on the spot right below the player
                    else if(objectGrid[i][j].peek() == this){
                        objectGrid[i][j].add(2, new Char('M'));
                        objectDisplayGrid.writeToTerminal(i, j);
                    }
                }
            }
        }

        // if the player is no longer hallucinating, change the display characters of the displayables back to the original
        else{
            for(int i = roomX; i <= width + roomX - 1; i++){
                for(int j = roomY; j <= height + roomY - 1; j++){

                    // for every position that is not a player and room wall, remove it from the display grid
                    if(!(objectGrid[i][j].peek() == this) && !(objectGrid[i][j].peek() instanceof RoomWall)){
                        objectGrid[i][j].pop();
                        objectDisplayGrid.writeToTerminal(i, j);
                    }
                    
                    // removes the character underneath the player
                    else if(objectGrid[i][j].peek() == this){
                        objectGrid[i][j].remove(2);
                        objectDisplayGrid.writeToTerminal(i, j);
                    }
                }
            }
        }
    }

    @Override
	public String toString(){
        String str = "\n    Player: \n";
        str += "      name: " + name + "\n";
        str += "      room: " + room + "\n";
        str += "      serial: " + serial + "\n";
        str += "      visible: " + visible + "\n";
        str += "      posX: " + posX + "\n";
        str += "      posY: " + posY + "\n";
        str += "      hp: " + hp + "\n";
        str += "      maxhit: " + maxHit + "\n";
        str += "      hpMoves: " + hpMoves + "\n";
        
        for(CreatureAction action : actions){
            str += action.toString( ) + "\n";
        }

        return str;
    }
}
