package game;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class DungeonXMLHandler extends DefaultHandler{
    private StringBuilder parsedData = null;
    
    private Dungeon dungeonBeingParsed = null;
    private Room roomBeingParsed = null;
    private Monster monsterBeingParsed = null;
    private Player playerBeingParsed = null;
    private Passage passageBeingParsed = null;
    private Scroll scrollBeingParsed = null;
    
    private CreatureAction creatureActionBeingParsed = null;
    private ItemAction itemActionBeingParsed = null;
    private Sword swordBeingParsed = null;
    private Armor armorBeingParsed = null;

    // Flag checks what we are currently parsing. 
    // 1 -> Room
    // 2 -> Monster
    // 3 -> Player
    // 4 -> Passage 
    // 5 -> Scroll
    // 6 -> CreatureAction
    // 7 -> ItemAction
    // 8 -> Sword
    // 9 -> Armor
    private int flag = 0;
    private int CreatureBeingParsedFlag = 0;

    public Dungeon getDungeon(){
        return dungeonBeingParsed;
    }

    public DungeonXMLHandler(){
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if(qName.equalsIgnoreCase("Dungeon")){
            String name = attributes.getValue("name");
            int width = Integer.parseInt(attributes.getValue("width"));
            int topHeight = Integer.parseInt(attributes.getValue("topHeight"));
            int gameHeight = Integer.parseInt(attributes.getValue("gameHeight"));
            int bottomHeight = Integer.parseInt(attributes.getValue("bottomHeight"));
            
            Dungeon dungeon = Dungeon.getInstance(); // creates dungeon instance
            dungeon.setName(name);
            dungeon.setWidth(width);
            dungeon.setTopHeight(topHeight);
            dungeon.setGameHeight(gameHeight);
            dungeon.setBottomHeight(bottomHeight);

            dungeonBeingParsed = dungeon;
        }
        else if(qName.equalsIgnoreCase("Room")){
            String roomName = attributes.getValue("room");
            Room room = new Room(roomName);
            roomBeingParsed = room;
            flag = 1;
        }
        else if(qName.equalsIgnoreCase("Monster")){
            String name = attributes.getValue("name");
            String room = attributes.getValue("room");
            String serial = attributes.getValue("serial");
            Monster monster = new Monster(name, room, serial);
            monsterBeingParsed = monster;
            flag = 2;
            CreatureBeingParsedFlag = 2;
        }
        else if(qName.equalsIgnoreCase("Player")){
            String name = attributes.getValue("name");
            String room = attributes.getValue("room");
            String serial = attributes.getValue("serial");
            Player player = new Player(name, room, serial);
            playerBeingParsed = player;
            flag = 3;
            CreatureBeingParsedFlag = 3;
        }
        else if (qName.equalsIgnoreCase("Passage")){
            String room1 = attributes.getValue("room1");
            String room2 = attributes.getValue("room2");
            Passage passage = new Passage(room1, room2);
            passageBeingParsed = passage;
            flag = 4;
        }
        else if (qName.equalsIgnoreCase("Scroll")){
            String name = attributes.getValue("name");
            String room = attributes.getValue("room");
            String serial = attributes.getValue("serial");
            Scroll scroll = new Scroll(name, room, serial);
            scrollBeingParsed = scroll;
            flag = 5;
        }
        else if (qName.equalsIgnoreCase("CreatureAction")){
            String name = attributes.getValue("name");
            String type = attributes.getValue("type");
            CreatureAction creatureAction = new CreatureAction(name, type);
            creatureActionBeingParsed = creatureAction;
            flag = 6;
        }
        else if(qName.equalsIgnoreCase("ItemAction")){
            String name = attributes.getValue("name");
            String type = attributes.getValue("type");
            ItemAction itemAction = new ItemAction(name, type);
            itemActionBeingParsed = itemAction;
            flag = 7;
        }
        else if(qName.equalsIgnoreCase("Sword")){
            String name = attributes.getValue("name");
            String room = attributes.getValue("room");
            String serial = attributes.getValue("serial");
            Sword sword = new Sword(name, room, serial);
            swordBeingParsed = sword;
            flag = 8;
        }
        else if(qName.equalsIgnoreCase("Armor")){
            String name = attributes.getValue("name");
            String room = attributes.getValue("room");
            String serial = attributes.getValue("serial");
            Armor armor = new Armor(name, room, serial);
            armorBeingParsed = armor;
            flag = 9;
        }
        parsedData = new StringBuilder();
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {

        // sets the X positions of all possible objects
        if (qName.equalsIgnoreCase("posX")){
            if(flag == 1){
                roomBeingParsed.setPosX(Integer.parseInt(parsedData.toString()));
            }
            else if(flag == 2){
                monsterBeingParsed.setPosX(Integer.parseInt(parsedData.toString()) + roomBeingParsed.getPosX());
            }
            else if(flag == 3){
                playerBeingParsed.setPosX(Integer.parseInt(parsedData.toString()) + roomBeingParsed.getPosX());
            }
            else if(flag == 4){
                passageBeingParsed.addXPosition(Integer.parseInt(parsedData.toString()));
            }
            else if(flag == 5){
                scrollBeingParsed.setPosX(Integer.parseInt(parsedData.toString()) + roomBeingParsed.getPosX());
            }
            else if(flag == 8){
                swordBeingParsed.setPosX(Integer.parseInt(parsedData.toString()) + roomBeingParsed.getPosX());
            }
            else if(flag == 9){
                armorBeingParsed.setPosX(Integer.parseInt(parsedData.toString()) + roomBeingParsed.getPosX());
            }
        }

        // sets the Y positions of all possible objects
        else if(qName.equalsIgnoreCase("posY")){
            if(flag == 1){
                roomBeingParsed.setPosY(Integer.parseInt(parsedData.toString()) + dungeonBeingParsed.getTopHeight());
            }
            else if(flag == 2){
                monsterBeingParsed.setPosY(Integer.parseInt(parsedData.toString()) + roomBeingParsed.getPosY());
            }
            else if(flag == 3){
                playerBeingParsed.setPosY(Integer.parseInt(parsedData.toString()) + roomBeingParsed.getPosY());
            }
            else if(flag == 4){
                passageBeingParsed.addYPosition(Integer.parseInt(parsedData.toString()));
            }
            else if(flag == 5){
                scrollBeingParsed.setPosY(Integer.parseInt(parsedData.toString()) + roomBeingParsed.getPosY());
            }
            else if(flag == 8){
                swordBeingParsed.setPosY(Integer.parseInt(parsedData.toString()) + roomBeingParsed.getPosY());
            }
            else if(flag == 9){
                armorBeingParsed.setPosY(Integer.parseInt(parsedData.toString()) + roomBeingParsed.getPosY());
            }
        }

        // sets either the width or height of a room
        else if(qName.equalsIgnoreCase("width")){
            roomBeingParsed.setWidth(Integer.parseInt(parsedData.toString()));   
        }
        else if(qName.equalsIgnoreCase("height")){
            roomBeingParsed.setHeight(Integer.parseInt(parsedData.toString()));   
        }

        // sets the HP level of the respective monster or player
        else if(qName.equalsIgnoreCase("hp")){
            if(flag == 2){
                monsterBeingParsed.setHp(Integer.parseInt(parsedData.toString()));
            }
            else if(flag == 3){
                playerBeingParsed.setHp(Integer.parseInt(parsedData.toString()));
            }
        }

        // adds found object to respective object based on status of parsing
        else if(qName.equalsIgnoreCase("Monster")){
            dungeonBeingParsed.addMonster(monsterBeingParsed);
            monsterBeingParsed = null;
        }
        else if(qName.equalsIgnoreCase("Player")){
            dungeonBeingParsed.setPlayer(playerBeingParsed);
            playerBeingParsed = null;
        }
        else if(qName.equalsIgnoreCase("Room")){
            dungeonBeingParsed.addRoom(roomBeingParsed);
            roomBeingParsed = null;
        }
        else if(qName.equalsIgnoreCase("Passage")){
            dungeonBeingParsed.addPassage(passageBeingParsed);
            passageBeingParsed = null;
        }
        else if(qName.equalsIgnoreCase("Sword")){
            // if the player is still being parsed, add the sword to the player's items
            if(playerBeingParsed != null){
                playerBeingParsed.addItem(swordBeingParsed);
            }
            // if the room is still being parsed, add the sword to the room's items
            else if(roomBeingParsed != null){
                dungeonBeingParsed.addItem(swordBeingParsed);
            }
            swordBeingParsed = null;
        }
        else if(qName.equalsIgnoreCase("Scroll")){
            dungeonBeingParsed.addItem(scrollBeingParsed);
            scrollBeingParsed = null;
        }
        else if(qName.equalsIgnoreCase("Armor")){
            // if the player is still being parsed, add the armor to the player's items
            if(playerBeingParsed != null){
                playerBeingParsed.addItem(armorBeingParsed);
            }
            // if the room is still being parsed, add the armro to the room's items
            else if(roomBeingParsed != null){
                dungeonBeingParsed.addItem(armorBeingParsed);
            }
            armorBeingParsed = null;
        }

        // sets the max hit of either monster or player in the room
        else if(qName.equalsIgnoreCase("maxhit")){
            if(flag == 2){
                monsterBeingParsed.setMaxHit(Integer.parseInt(parsedData.toString()));
            }
            else if(flag == 3){
                playerBeingParsed.setMaxHit(Integer.parseInt(parsedData.toString()));
            }
        }

        // sets the item action message and action int value of either the creature action or item action in the room
        else if (qName.equalsIgnoreCase("actionMessage")){
            if(flag == 6){
                creatureActionBeingParsed.setActionMessage(parsedData.toString());
            }
            else if(flag == 7){
                itemActionBeingParsed.setActionMessage(parsedData.toString());
            }
        }
        else if (qName.equalsIgnoreCase("actionIntValue")){
            if(flag == 6){
                creatureActionBeingParsed.setActionIntValue(Integer.parseInt(parsedData.toString()));
            }
            else if(flag == 7){
                itemActionBeingParsed.setActionIntValue(Integer.parseInt(parsedData.toString()));
            }
        }
        else if(qName.equalsIgnoreCase("actionCharValue")){
            if(flag == 6){
                creatureActionBeingParsed.setActionCharValue(parsedData.toString().charAt(0));
            }
            else if(flag == 7){
                itemActionBeingParsed.setActionCharValue(parsedData.toString().charAt(0));
            }
        }

        // adds the creature action to either the monster or player in the room
        else if (qName.equalsIgnoreCase("CreatureAction")){
            if(CreatureBeingParsedFlag == 2){
                monsterBeingParsed.addAction(creatureActionBeingParsed);
                creatureActionBeingParsed = null;
            }
            else if(CreatureBeingParsedFlag == 3){
                playerBeingParsed.addAction(creatureActionBeingParsed);
                creatureActionBeingParsed = null;
            }
        }

        // adds the item action to the scroll in the room
        else if (qName.equalsIgnoreCase("ItemAction")){
            scrollBeingParsed.addAction(itemActionBeingParsed);
        }

        // adds the item itn value to either the scroll, sword, or armor object
        else if(qName.equalsIgnoreCase("ItemIntValue")){
            if(flag == 8){
                swordBeingParsed.setItemValue(Integer.parseInt(parsedData.toString()));
            }
            else if(flag == 9){
                armorBeingParsed.setItemValue(Integer.parseInt(parsedData.toString()));
            }
        }

        // adds the hp moves to the player
        else if(qName.equalsIgnoreCase("hpMoves")){
            playerBeingParsed.setHpMoves(Integer.parseInt(parsedData.toString()));
        }

        // sets the visible state of the object being parsed
        else if(qName.equalsIgnoreCase("visible")){
            if(flag == 1){
                roomBeingParsed.setVisible(Integer.parseInt(parsedData.toString()));
            }
            else if (flag == 2){
                monsterBeingParsed.setVisible(Integer.parseInt(parsedData.toString()));
            }
            else if (flag == 3){
                playerBeingParsed.setVisible(Integer.parseInt(parsedData.toString()));
            }
            else if (flag == 4){
                passageBeingParsed.setVisible(Integer.parseInt(parsedData.toString()));
            }
            else if (flag == 5){
                scrollBeingParsed.setVisible(Integer.parseInt(parsedData.toString()));
            }
            else if(flag == 8){
                swordBeingParsed.setVisible(Integer.parseInt(parsedData.toString()));
            }
            else if(flag == 9){
                armorBeingParsed.setVisible(Integer.parseInt(parsedData.toString()));
            }
        }

        // adds the monster type to the monster object
        else if(qName.equalsIgnoreCase("type")){
            monsterBeingParsed.setType(parsedData.toString());
        }
    }

    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
        parsedData.append(new String(ch, start, length));
    }
}
