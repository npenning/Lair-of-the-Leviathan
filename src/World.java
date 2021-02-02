import java.util.ArrayList;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class World{
	
	public GameCanvas panel;
	public JFrame frame;
	public KeyManager manager;
	public Animator animate;
	public TitleScreen titleScreen = new TitleScreen(this);
	public Menu mainMenu = new Menu(this);
	public Player player = new Player(100);
	public Combat battle;
	
	private Tile[][] grid;
	private int playerX = 70;//70;
	private int playerY = 115;//115;
	private int gridX = 140;
	private int gridY = 140;
	private int[] nonSolidTiles = {0, 304, 363, 324, 325, 265, 295, 326, 393, 394, 395, 423, 424, 425, 453, 454, 455, 414, 415, 416, 444, 445, 446, 474, 475, 476};//"│┤╡╢╣║╝╜╛└┴├┼╞╟╚╩╠╬╨╙╘╫╪┘█▒®©■□";
	private int[] walls = {306, 307, 308, 336, 337, 338, 344, 345, 346, 171, 172, 172, 201, 202, 203, 294, 296, 409, 410, 378, 379, 411};
	private int[] wallToppers = {276,277,278};
	private int[] interactable = {389};//"■®";
	private int[] tilesWithOverlay = {176, 299, 344, 345, 346, 359};
	private BufferedImage tileSheet;
	private FontTile[] fontTiles;
	private BufferedImage[] tileSet;
	private BufferedImage[] playerSprites;
	private int tileSize = 16;
	private int fontSize = 8;
	private char playerDirection = 'r';
	private int[] moveDirection = {0, 0};
	private int[] previousMoveDirection = {0, 0};
	private boolean moving;
	private int playerHeight = 16;
	private int keyCount = 0;
	
	/*
	 * States:
	 * 0 - main menu
	 * 1 - walking around
	 * 2 - interacting
	 * 3 - menu
	 * 4 - combat
	 */
	private int gameState = 0, prevState;
	
	
	public World()
	{
		frame = new JFrame("Lair of the Leviathan");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(true);
		
		//frame.setLocationRelativeTo(null);
		/*
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setUndecorated(true);

		frame.getContentPane().setBackground(Color.BLUE);
		*/
		manager = new KeyManager(this);
		
		panel = new GameCanvas();
		panel.addKeyListener(manager);
		panel.setFocusable(true);
		panel.requestFocusInWindow();
		
		/*
		screen = new JTextArea(screenX, screenY);
		screen.setFont(new Font("monospaced", Font.PLAIN, 10));
		screen.setBackground(new Color(229, 255, 229));
		screen.setDisabledTextColor(new Color(81, 97, 81));
		screen.setEnabled(false);
		*/
		

		//panel.setPreferredSize(new Dimension(this.getTileSize() * panel.getColumns(), this.getTileSize()*panel.getRows()));
		
		frame.getContentPane().setPreferredSize(new Dimension(this.getTileSize() * panel.getColumns(), this.getTileSize()*panel.getRows()));
		
		
		panel.setWorld(this);
		panel.setScreen();
	    frame.getContentPane().add(panel);
		//panel.add(screen, BorderLayout.NORTH);
	    
		loadFontTiles();
		loadImageSet();
		loadGrid();
		loadSpecialTiles();	
		loadPlayerSprites();
	    
		frame.pack();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation((screenSize.width - frame.getWidth())/2, (screenSize.height - frame.getHeight())/2);
		frame.setVisible(true);
		panel.repaint();
		while(true)
		{
			update();
			
			try {
				Thread.sleep(15);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}	
	}
	
	public void update()
	{
		move();
		if(player.getEquippedTool() != null && player.getEquippedTool().getIsActive())
			player.getEquippedTool().update();
		panel.setTileSetImages(grid, playerX, playerY);
		panel.repaint();
		
	}
	
	void replaceChar(JTextArea screen, int row, int column, char replacement)
	{
	    int lineOffset;
		try 
		{
			lineOffset = screen.getLineStartOffset(row);
			screen.replaceRange(Character.toString(replacement), lineOffset + column, lineOffset + column + 1);
		} 
		catch (BadLocationException e) {
			e.printStackTrace();
		}
	    
	}
	
	public void move()
	{
		
		if((moveDirection[0] != 0 || moveDirection[1] != 0))
		{
			if(moveDirection[0] != previousMoveDirection[0] || moveDirection[1] == 0)
			{
				switch(moveDirection[0])
				{
					case -1: playerDirection = 'l'; break;
					case  1: playerDirection = 'r'; break;
				}
			}
			if(moveDirection[1] != previousMoveDirection[1] || moveDirection[0] == 0)
			{
				switch(moveDirection[1])
				{
					case -1: playerDirection = 'u'; break;
					case  1: playerDirection = 'd'; break;
				}
			}
			//fix this later (wall bump freeze)
			if(grid[playerX + moveDirection[0]][playerY + moveDirection[1]].getIsSolid())
			{
				moveDirection[0] = 0;
				moveDirection[1] = 0;
			}
			/*
			if(checkFor(grid[playerX + moveDirection[0]][playerY + moveDirection[1]].getID(0), "solidTiles")  || checkFor(grid[playerX + moveDirection[0]][playerY + moveDirection[1]].getID(1), "solidTiles")// || (moveDirection[1]==1 && checkFor(grid[playerX][playerY].getID(), "wallToppers")))
			{
					moveDirection[1] = 0;
			}
			*/
			if(moveDirection[0] != 0 || moveDirection[1] != 0)
				moving = true;
		}
	}
	
	public void interact()
	{
		Tile iTile;
		SpecialTile sTile;
		switch(playerDirection)
		{
			case 'u': iTile = grid[playerX][playerY-1]; break;
			case 'd': iTile = grid[playerX][playerY+1]; break;
			case 'l': iTile = grid[playerX-1][playerY]; break;
			case 'r': iTile = grid[playerX+1][playerY]; break;
			default:  iTile = grid[playerX][playerY]; break;
		}
		
		if(iTile.getIsSpecial()){
			sTile = (SpecialTile) iTile;
			if(sTile.getSpecialTileType() != "Bombable Wall")
				sTile.activate();
		}
			/*  Interactions:
			 *  0 - show message
			 *  1 - getItem
			 *  2 - toggle other tiles
			 *  3 - toggle self
			 *  4 - remove Key
			 */
		/*
			for(Integer[] effect: iTile.getEffects())
			{
				if(effect[0] == (iTile.getState()?1:0) || effect[0] == 2)
				{
					switch(effect[1])
					{
					case 0: panel.setMessage(iTile.getMessage(effect[0])); break;
					case 1: if(iTile.getObject() == 1)
								keyCount++;
						break;
					case 2: 
						for(Integer[] orderedPair: iTile.getAffectedTileSet()){
							grid[orderedPair[0]][orderedPair[1]].toggle(); 
						}
						break;
					case 3: iTile.toggle(); break;
					case 4: keyCount--; break;
					}
				}
			}
		*/
	}
	
	public void fight()
	{
		battle = new Combat(player, this);
	}
	
	private void loadFontTiles()
	{
		try {
		    tileSheet = ImageIO.read(new File("font3.png"));
		} 
		catch (IOException e) {
		}
		
		int height 	= tileSize;
		int width 	= fontSize;
		int rows 	= tileSheet.getHeight()/tileSize;
		int columns = tileSheet.getWidth()/fontSize;	
		fontTiles = new FontTile[rows*columns];
		String charLoadOrder = 	" 0123456789!?.-@"	+
								"#[]\"'♂♀$,*/ABCDE"	+
								"FGHIJKLMNOPQRSTU"	+
								"VWXYZabcdefghijk"	+
								"lmnopqrstuvwxyz►"	+
								":%()<>↑↓←→_=;&+{";
		/*
		String charLoadOrder = 	" ☻☺♥♦♣♠●○®©♂♀♪♫¤"  +
								"►◄↕‼¶§ž↨↑↓→←…↔▲▼"  +
								" !\"#$%&'()*+,-./" +
								"0123456789:;<=>?"  +
								"@ABCDEFGHIJKLMNO"  +
								"PQRSTUVWXYZ[\\]^_" +
								"`abcdefghijklmno"  +
								"pqrstuvwxyz{¦}~⌂"  +
								"ÇüéâäàåçêëèïîìÄÅ"  +
								"ÉæÆôöòûùÿÖÜ¢£¥€ƒ"  +
								"áíóúñÑ†‡¿⌐¬½¼¡«»"  +
								"░▒▓│┤╡╢╖╕╣║╗╝╜╛┐"  +
								"└┴┬├─┼╞╟╚╔╩╦╠═╬╧"  +
								"╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀"  +
								"αβΓπΣσμτΦΘΩδωØεΠ"  +
								"≡±≥≤⌠⌡÷≈°•∙√ⁿ²■□"  ;
		 */
		for(int i = 0; i < rows; i++) {
	         for(int j = 0; j < columns; j++) {
	        	fontTiles[(i * columns) + j] = new FontTile();
	        	fontTiles[(i * columns) + j].setImage(tileSheet.getSubimage(j * width, i * height, width, height));
	        	fontTiles[(i * columns) + j].setChar(charLoadOrder.charAt((i * columns) + j));
	         }
	    }		
	}
	
	private void loadImageSet()
	{
		try {
		    tileSheet = ImageIO.read(new File("tileSheet.png"));
		} 
		catch (IOException e) {
		}
	
		int height 	= tileSize;
		int width 	= tileSize;
		int rows 	= tileSheet.getHeight()/tileSize;
		int columns = tileSheet.getWidth()/tileSize;	
		tileSet = new BufferedImage[rows*columns];
		
		for(int i = 0; i < rows; i++) {
	         for(int j = 0; j < columns; j++) {
	        	tileSet[((i * columns) + j)] = (tileSheet.getSubimage(j * height, i * width, width, height));
	         }
		}
		System.out.print("");
	}
	
	private void loadGrid()
	{
		try 
		{
			Scanner scan = new Scanner(new File("Dungeon Map.txt"));
			Scanner toggleScan = new Scanner(new File("Dungeon Map Toggled.txt"));
			
			grid = new Tile[gridX][gridY];
			
			int i = 0, j = 0;
			while(scan.hasNextInt())
			{
				int falseID = scan.nextInt() - 1;
				int trueID = toggleScan.nextInt() - 1;
				
				grid[i][j] = new NormalTile();
				grid[i][j].setID(falseID, 0);
				
				if(checkFor(falseID, "tilesWithOverlay"))
					grid[i][j-1].setID(falseID-68, 2);
				if(checkFor(trueID, "tilesWithOverlay"))
					grid[i][j-1].setTrueID(trueID-68, 2);
				if(!checkFor(falseID, "nonSolidTiles"))
					grid[i][j].setIsSolid(0);
				if(!checkFor(trueID, "nonSolidTiles"))
					grid[i][j].setIsSolid(1);
				if(checkFor(falseID, "walls"))
					grid[i][j].setIsWall(0);
				if(checkFor(trueID, "walls"))
					grid[i][j].setIsWall(1);
				
				grid[i][j].setTrueID(trueID, 0);
				i++;
				if(i >= gridX){
					i = 0;
					j++;
				}
			}
			scan.close();
			toggleScan.close();
			panel.setTileSetImages(grid, playerX, playerY);
			/*
			int numOfLines = (mapString.length() - mapString.replace("\n", "").length())+1;
			int numOfColumns = mapString.indexOf('\n');
			
			for(int i = 0; i < gridX; i++)
			{
				for(int j = 0; j < gridY; j++)
				{
					grid[i][j] = new Tile();
					grid[i][j].setChar(mapString.charAt((j * gridX + i)+1));
					grid[i][j].setImage(getImageFor(grid[i][j].getChar()));
					if((interactable.contains(Character.toString(grid[i][j].getChar()))))
						grid[i][j].toggleIsInteractable();
					if(mapString.charAt((j * gridX + i)+1) != toggledMapString.charAt((j * gridX + i)+1)){
						grid[i][j].setTrueChar(toggledMapString.charAt((j * gridX + i)+1));
						grid[i][j].setTrueImage(getImageFor(toggledMapString.charAt((j * gridX + i)+1)));
					}
				}	
			}	
			panel.setTileSetImages(grid, playerX, playerY);
			*/
		}
		
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
	}
	
	private void loadPlayerSprites()
	{
		BufferedImage spriteSheet;
		try {
			spriteSheet = ImageIO.read(new File("player1.png"));
			playerSprites = new BufferedImage[14];
			for(int j = 0; j<2; j++)
			{
				for(int i = 0; i < 7; i++)
					playerSprites[i+(j*7)] = spriteSheet.getSubimage(i * tileSize, j * playerHeight, tileSize, playerHeight);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void loadSpecialTiles()
	{
		
		int[][] keys = {{59,101},{65,54}, {88, 81}, {88, 48}, {59, 47}};
		for(int[] key: keys)
		{
			grid[key[0]][key[1]] = new Pickup((NormalTile)grid[key[0]][key[1]], this, "key");
		}
		
		
		int[][] doors = {{50,78}, {64, 74}, {86, 76}, {74, 26}, {43, 38}, {44, 62}};
		for(int[] door: doors)
		{
			grid[door[0]][door[1]] = new Door((NormalTile)grid[door[0]][door[1]], this);
		}	
		((Door) grid[50][78]).setEffectArea(rectangleToArray(new int[][]{{49, 75, 2, 2}}));
		((Door) grid[64][74]).setEffectArea(rectangleToArray(new int[][]{{64, 73, 0, 0}, {64, 75, 0, 0}}));
		((Door) grid[86][76]).setEffectArea(rectangleToArray(new int[][]{{85, 74, 2, 1}}));
		((Door) grid[74][26]).setEffectArea(rectangleToArray(new int[][]{{73, 24, 2, 1}}));
		((Door) grid[44][62]).setEffectArea(rectangleToArray(new int[][]{{43, 61, 2, 3}, {44, 62, 0, 0}}));
		
		
		int[][] healthPotions = {{48, 80}, {52, 80}, {68, 90}, {84, 64}, {88, 64}, {72, 28}, {76, 28}, {30, 75}, {34, 75}};
		for(int[] potion: healthPotions)
		{
			grid[potion[0]][potion[1]] = new Pickup((NormalTile)grid[potion[0]][potion[1]], this, "potion");
		}
		
		
		int[][] switches = {{54, 94}, {44, 51}, {28, 89}, {34, 97}, {38, 106}};
		for(int[] bulb: switches)
		{
			//fix this up later (put isSolid in class)
			grid[bulb[0]][bulb[1]-1].setID(146, 2);
			grid[bulb[0]][bulb[1]-1].setTrueID(269, 2);
			grid[bulb[0]][bulb[1]] = new Switch((NormalTile)grid[bulb[0]][bulb[1]], this);
		}
		((Switch) grid[54][94]).setEffectArea(rectangleToArray(new int[][]{{61, 98, 1, 1}, {54, 93, 0, 0}}));
		((Switch) grid[44][51]).setEffectArea(rectangleToArray(new int[][]{{38, 57, 0, 2}, {44, 50, 0, 0}}));
		((Switch) grid[28][89]).setEffectArea(rectangleToArray(new int[][]{{30, 86, 0, 1}, {26, 90, 0, 1}, {32, 92, 0, 3}, {33, 100, 2, 2}, {34, 108, 0, 1}, {39, 112, 15, 2}, {54, 106, 0, 5}, {28, 88, 0, 0}, {34, 96, 0, 1}, {38, 105, 0, 1}}));
		((Switch) grid[34][97]).setEffectArea(rectangleToArray(new int[][]{{30, 86, 0, 1}, {26, 90, 0, 1}, {32, 92, 0, 3}, {33, 100, 2, 2}, {34, 108, 0, 1}, {39, 112, 15, 2}, {54, 106, 0, 5}, {28, 88, 0, 1}, {34, 96, 0, 0}, {38, 105, 0, 1}}));
		((Switch) grid[38][106]).setEffectArea(rectangleToArray(new int[][]{{30, 86, 0, 1}, {26, 90, 0, 1}, {32, 92, 0, 3}, {33, 100, 2, 2}, {34, 108, 0, 1}, {39, 112, 15, 2}, {54, 106, 0, 5}, {28, 88, 0, 1}, {34, 96, 0, 1}, {38, 105, 0, 0}}));
		
		
		int[][] chests = {{83, 115}, {86, 68}, {92, 31}, {44, 66}, {32, 71}};
		for(int[] chest: chests)
		{
			grid[chest[0]][chest[1]] = new Chest((NormalTile)grid[chest[0]][chest[1]], this);
		}
		
		((Chest)grid[86][68]).setTreasure(new Tool(05, "Bombs", "Blows things up.", this));
		((Chest)grid[32][71]).setTreasure(new Tool(05, "Bow", "Shoots an arrow.", this));
		
		int[][] signs = {{50, 81}, {86, 65}, {74, 29}, {29, 43}, {32, 74}, {122, 82}};
		for(int[] sign: signs)
		{
			grid[sign[0]][sign[1]] = new Sign((NormalTile)grid[sign[0]][sign[1]], this);
		}
		((Sign) grid[50][81]).setMessage(0, "Hey, there. Did you get the key in the last room? You'll need it to open doors like these. I'll try and give you a hand through your journey, but a simple sign like me can't give much more than advice. Grab the potions behind me; they may prove useful against the monsters deeper in... Good luck!");
		((Sign) grid[86][65]).setMessage(0, "Seems you're doing pretty well for yourself! Don't let up yet, though; you still have a long way to go. The chest in this room contains some bombs. They're too bulky to use in a fight, but they may prove useful tearing down thin walls... Good luck!");
		((Sign) grid[74][29]).setMessage(0, "Again so soon? You must be solving these rooms like a pro! That, or you just really like talking to me... Speaking of, we may not see each other for a while, but don't get discouraged! I know you can do it! Good luck!");
		((Sign) grid[29][43]).setMessage(0, "Ha Ha! So you've figured out the bombs! Just keep in mind that thin walls make for good target practice... Good luck!");
		((Sign) grid[32][74]).setMessage(0, "How are you doing? Keep up the good work, you're almost there! Take this bow. Not great for close quarters combat, but you can hit switches from a distance. I won't see you again until the end, but I know you can do it! Stay strong!");
		((Sign) grid[122][82]).setMessage(0, "Congratulations, you made it to the end! I hope you enjoyed playing this demo. Thanks for playing!"); 
		
		int[][] bWalls = {{74, 115}, {92, 44}, {58, 40}, {34, 30}};
		for(int[] wall: bWalls)
		{
			grid[wall[0]][wall[1]] = new BombWall((NormalTile)grid[wall[0]][wall[1]], this);
		}
		((BombWall) grid[74][115]).setEffectArea(rectangleToArray(new int[][]{{74, 113, 6, 3}}));
		((BombWall) grid[92][44]).setEffectArea(rectangleToArray(new int[][]{{91, 34, 2, 10}}));
		((BombWall) grid[58][40]).setEffectArea(rectangleToArray(new int[][]{{57, 38, 1, 3}}));
		((BombWall) grid[34][30]).setEffectArea(rectangleToArray(new int[][]{{28, 28, 6, 4}}));
		
		int[][] singleToppersT = {{42, 37}, {44, 37}, {38, 56}, {29, 86}, {31, 86}, {25, 90}, {27, 90}, {32, 92}, {32, 93}, {34, 108}};
		for(int[] topT: singleToppersT)
		{
			grid[topT[0]][topT[1]].setTrueID(246, 2);
		}
		
		int[][] singleToppers = {{38, 56}, {38, 57}, {38, 58}, {42, 37}, {44, 37}, {29, 86}, {30, 86}, {31, 86}, {25, 90}, {26, 90}, {27, 90}, {33, 100}, {34, 100}, {35, 100}};
		for(int[] top: singleToppers)
		{
			grid[top[0]][top[1]].setID(246, 2);
		}
	}

	private ArrayList<Integer[]> rectangleToArray(int[][] input){
		//width/length is zero-indexed
		ArrayList<Integer[]> selection = new ArrayList<Integer[]>();
		for(int[] rects: input)
		{
			int topLeftX = rects[0], topLeftY = rects[1], width = rects[2], length = rects[3];
		
			ArrayList<Integer[]> latest = new ArrayList<Integer[]>();
			for(int i = topLeftX; i <= topLeftX + width; i++)
			{
				for(int j = topLeftY; j <= topLeftY + length; j++){
					latest.add(new Integer[]{i, j});
				}
			}
			selection.addAll(latest);	
		}
		return selection;
	}
	
	public int getTileSize()
	{
		return tileSize;
	}
	
	public int getFontSize()
	{
		return fontSize;
	}
	
	public int getPlayerHeight()
	{
		return playerHeight;
	}
	
	public Tile getGrid(int x, int y)
	{
		return grid[x][y];
	}
	
	public int getPlayerX()
	{
		return playerX;
	}
	
	public int getPlayerY()
	{
		return playerY;
	}
	
	public boolean getIsMoving()
	{
		return moving;
	}
	
	public void setIsMoving(boolean m)
	{
		moving = m;
	}
	
	public void updatePlayerLocation()
	{
		playerX += moveDirection[0];
		playerY += moveDirection[1];
	}
	
	public void setMoveDirection(int position, int value)
	{
		previousMoveDirection[0] = moveDirection[0];
		previousMoveDirection[1] = moveDirection[1];
		moveDirection[position] = value;
	}
	
	public int getMoveDirection(int position)
	{
		return moveDirection[position];
	}
	
	public BufferedImage getImageFor(char letter)
	{
		for(FontTile t: fontTiles)
		{
			if(letter == t.getChar())
				return t.getImage();
		}
		return fontTiles[1].getImage();
	}
	
	public BufferedImage getImageFor(int id)
	{
		return tileSet[id];
	}
	
	public int getGameState()
	{
		return gameState;
	}
	
	public void setGameState(int newState)
	{
		prevState = gameState;
		gameState = newState;
	}
	
	public void toPreviousState()
	{
		gameState = prevState;
	}
	
	public int[] getWallToppers()
	{
		return wallToppers;
	}
	
	public void useKey()
	{
		keyCount--;
	}
	
	public void addKey()
	{
		keyCount++;
	}
	
	public int getKeyCount()
	{
		return keyCount;
	}
	
	public boolean checkFor(int id, String setName)
	{
		int[] set = {};
		switch(setName)
		{
		case "nonSolidTiles":    set = nonSolidTiles; break;
		case "wallToppers":   set = wallToppers; break;
		case "interactable" : set = interactable; break;
		case "tilesWithOverlay" : set = tilesWithOverlay; break;
		case "walls" : set = walls; break;
		}
		for(int value: set)
		{
			if(value == id)
				return true;
		}
		return false;
	}
	
	public BufferedImage getPlayerTile(int timing, boolean whichLeg)
	{
		int indexX = 0;
		int indexY = 0;
		switch(playerDirection)
		{
		case 'u': indexX = ((timing == 0 || timing >= tileSize/2)?1:4); indexY = whichLeg?1:0; break;
		case 'd': indexX = ((timing == 0 || timing >= tileSize/2)?0:3); indexY = whichLeg?1:0; break;
		case 'r': indexY = 1;
		case 'l': indexX = (timing == 0 || (timing >= tileSize/2)?2:(whichLeg?5:6)); break;
		default: indexX = 0; indexY = 0;
		}
		
		return playerSprites[indexX + (7*indexY)];
		
	}
	
	public char getPlayerDirection()
	{
		return playerDirection;
	}
	
	public void toggleFullScreen()
	{
		frame.dispose();
		if(!panel.getIsFullScreen())
		{
			frame = new JFrame();
			frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
			frame.setUndecorated(true);
			frame.setCursor(frame.getToolkit().createCustomCursor(new BufferedImage( 1, 1, BufferedImage.TYPE_INT_ARGB ), new Point(), null));
			frame.getContentPane().setBackground(new Color(62, 40, 86));
			frame.setLayout(new GridBagLayout());
		}
		else
		{
			frame = new JFrame("Lair of the Leviathan");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setResizable(false);
			frame.setLocationRelativeTo(null);
			frame.getContentPane().setPreferredSize(new Dimension(this.getTileSize() * panel.getColumns(), this.getTileSize()*panel.getRows()));
		}
		frame.getContentPane().add(panel);
		frame.pack();
		if(panel.getIsFullScreen()){
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			frame.setLocation((screenSize.width - frame.getWidth())/2, (screenSize.height - frame.getHeight())/2);
		}
		frame.setVisible(true);
		panel.repaint();
		
		panel.toggleIsFullScreen();
	}
	
}
