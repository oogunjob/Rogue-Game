package game;
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;


public class Rogue implements Runnable {

    private Thread keyStrokePrinter;
    public static final int FRAMESPERSECOND = 60;
    public static final int TIMEPERLOOP = 1000000000 / FRAMESPERSECOND;
    private static ObjectDisplayGrid displayGrid = null;
    private static Dungeon dungeon = null;

    public Rogue(int width, int height) {
        displayGrid = new ObjectDisplayGrid(width, height);
    }

    public static void main(String[] args) {

        String fileName = null;
        switch (args.length) {
        case 1:
            fileName = args[0];
            break;
        default:
            System.out.println("java Test <xmlfilename>");
        return;
        }
    
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
    
        try {
            SAXParser saxParser = saxParserFactory.newSAXParser();
            DungeonXMLHandler handler = new DungeonXMLHandler();
            saxParser.parse(new File(fileName), handler);

            dungeon = handler.getDungeon();

            
            Rogue rogue = new Rogue(dungeon.getWidth(), dungeon.getGameHeight());
            Thread testThread = new Thread(rogue);
            testThread.start();

            rogue.keyStrokePrinter = new Thread(new KeyStrokePrinter(displayGrid));
            rogue.keyStrokePrinter.start();
            rogue.keyStrokePrinter.join();
            
            // start other threads
            testThread.join();

        } catch (ParserConfigurationException | SAXException | IOException | InterruptedException e){
            e.printStackTrace(System.out);
        }
    }

    @Override
    public void run() {
        displayGrid.fireUp();
        dungeon.draw(displayGrid);

        displayGrid.initializeDisplay();
    }
}