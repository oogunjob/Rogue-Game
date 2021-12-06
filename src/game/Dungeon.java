package game;
import java.util.ArrayList;

public class Dungeon extends Displayable{
	private String name;
	private int width;
	private int topHeight;
	private int gameHeight;
	private int bottomHeight;

	private ArrayList<Room> rooms;
	private ArrayList<Passage> passages;
	private ArrayList<Monster> monsters;
	private ArrayList<Item> items;
	
	private Player player;
	private static Dungeon dungeonInstance;

	public Dungeon(){
		rooms = new ArrayList<Room>();
		passages = new ArrayList<Passage>();
		monsters = new ArrayList<Monster>();
		items = new ArrayList<Item>();
	}

	// setter methods
	public void setName(String _name){
		name = _name;
	}

	public void setWidth(int _width){
		width = _width;
	}

	public void setTopHeight(int _topHeight){
		topHeight = _topHeight;
	}

	public void setGameHeight(int _gameHeight){
		gameHeight = _gameHeight;
	}

	public void setBottomHeight(int _bottomHeight){
		bottomHeight = _bottomHeight;
	}

	// getter methods
	public static Dungeon getInstance(){
    	if (dungeonInstance == null)
            dungeonInstance = new Dungeon();
 
        return dungeonInstance;
    }

	public int getWidth() {
		return width;
	}

	public int getGameHeight(){
		return gameHeight;
	}

	public int getTopHeight(){
		return topHeight;
	}

	public int getBottomHeight(){
		return bottomHeight;
	}

	// adds monster to the dungeon
	public void addMonster(Monster monster){
		monsters.add(monster);
	}

	// adds room to the dungeon
	public void addRoom(Room room){
		rooms.add(room);
	}
	
	// adds passage to the dungeon
	public void addPassage(Passage passage){
		passages.add(passage);
	}

	public void addItem(Item item){
		items.add(item);
	}

	// returns list of all monsters in the room
	public ArrayList<Monster> getMonsterList() {
        return monsters;
    }

	public ArrayList<Room> getRoomList() {
		return rooms;
	}

	// adds the player to the dungeon
	public void setPlayer(Player _player){
		player = _player;
	}

	public Player getPlayer(){
        return player;
    }

	public void draw(ObjectDisplayGrid objectDisplayGrid){
		// draws every room in the dungeon
		for (Room room : rooms){
			room.draw(objectDisplayGrid, topHeight, bottomHeight);
		}

		// draws every item in the dungeon
		for(Item item : items){
			item.draw(objectDisplayGrid);
		}

		// draws every passage in the dungeon
		for (Passage passage : passages){
			passage.draw(objectDisplayGrid, topHeight);
		}

		// draws every monster in the dungeon
		for (Monster monster : monsters){
			monster.draw(objectDisplayGrid);
		}

		// draws the player in the dungeon
		player.draw(objectDisplayGrid);

	}

	@Override
	public String toString(){
		String str = "\nDungeon: \n";
		str += "  Name: " + name + "\n";
		str += "  Width: " + width + "\n";
		str += "  TopHeight: " + topHeight + "\n";
		str += "  GameHeight: " + gameHeight + "\n";
		str += "  BottomHeight: " + bottomHeight + "\n";

		for (Room room : rooms){
			str += room.toString() + "\n";
		}
		
		for (Passage passage : passages){
			str += passage.toString() + "\n";
		}
		
		return str;
	}
}
