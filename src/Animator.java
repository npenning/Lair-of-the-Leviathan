import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public final class Animator {
	
	private GameCanvas panel;
	private World dungeon;
	private int textBoxHeight;
	private int messageCount;
	private String[] msg;
	private int walkCount = 0;
	private boolean whichLeg = false;
	private boolean nextBox = true;
	private Graphics g;
	
	public Animator(GameCanvas panel, World dungeon, Graphics g2d)
	{
		this.panel = panel;
		this.dungeon = dungeon;
		textBoxHeight = 4;
		this.g = g2d;
	}
	
	public void overWorld()
	{
		if(dungeon.getIsMoving()){
			walkCount++;
		}
		for(int pass = 0; pass < 3; pass++)
		{
			for(int i=0; i < panel.getColumns() + 2; i++)
			{
				for(int j = 0; j < panel.getRows() + 2; j++)
				{			
					if(pass == 0){
					g.drawImage(dungeon.getImageFor(' '), (i-1) * dungeon.getTileSize() - walkCount*dungeon.getMoveDirection(0), (j-1)*dungeon.getTileSize() - walkCount*dungeon.getMoveDirection(1), null);
					}
					//if(panel.getTileImage(i, j, pass) != null)
						g.drawImage(panel.getTileImage(i, j, pass), (i-1) * dungeon.getTileSize() - walkCount*dungeon.getMoveDirection(0), (j-1)*dungeon.getTileSize() - walkCount*dungeon.getMoveDirection(1) - (pass == 1?dungeon.getTileSize()/2:0), null);
				}
			}
			if(pass == 1){
				if(dungeon.player.getEquippedTool() != null)
					g.drawImage(dungeon.getImageFor(dungeon.player.getEquippedTool().getImageID()), ((panel.getColumns()-1)/2 - (dungeon.getPlayerX() - dungeon.player.getEquippedTool().getCoordinates()[0])) * dungeon.getTileSize() - walkCount*dungeon.getMoveDirection(0), (((panel.getRows()-1)/2 - (dungeon.getPlayerY() - dungeon.player.getEquippedTool().getCoordinates()[1])) * dungeon.getTileSize())- (dungeon.getPlayerHeight()/2) - walkCount*dungeon.getMoveDirection(1), null);
				g.drawImage(dungeon.getPlayerTile(walkCount, whichLeg), (panel.getColumns()-1)/2 * dungeon.getTileSize(), ((panel.getRows()-1)/2 * dungeon.getTileSize()) - (dungeon.getPlayerHeight() - dungeon.getTileSize()) - dungeon.getPlayerHeight()/2, null);
			}else if(pass == 2){
				drawBox(0, 0, 5, 2);
				typeOnScreen(((Integer.parseInt(dungeon.player.getHealth()) > 99)?"":" ") + dungeon.player.getHealth() + "/" + dungeon.player.getMaxHealth(), 15, 8);
			}
		}

		if(walkCount == dungeon.getTileSize()){
			walkCount = 0;
			
			dungeon.setIsMoving(false);
			dungeon.updatePlayerLocation();
			whichLeg = !whichLeg;
			
			for(int i = 0; i<2; i++)
			{
				if(dungeon.manager.getIsPressing(37+i)){
					dungeon.setMoveDirection(i, -1);
					dungeon.setMoveDirection(i==1?0:1, 0);
				}else if(dungeon.manager.getIsPressing(39+i)){
					dungeon.setMoveDirection(i, 1);
					dungeon.setMoveDirection(i==1?0:1, 0);
				}
				else
					dungeon.setMoveDirection(i, 0);
			}
			
		}
		
	}
	
	public void combat()
	{
		g.drawImage(dungeon.battle.getBackground(), 0, 0, null);
	}
	
	public void titleScreen()
	{
		TitleScreen tScreen = dungeon.titleScreen;
		g.drawImage(tScreen.getBackground(), 0, 0, null);
		String[] text = tScreen.getOnMain()?tScreen.getMainText():tScreen.getOptionsText();
		for(int i = 0; i < text.length; i++){
			int xCoordinate = ((tScreen.getBackground().getWidth() - dungeon.getFontSize()*text[i].length())/2)+2;
			int yCoordinate = 90 + i*dungeon.getTileSize();
			typeOnScreen(text[i], xCoordinate, yCoordinate);
			if(tScreen.getCursorLocation() == i){
				typeOnScreen(">", xCoordinate - dungeon.getFontSize(), yCoordinate);
				typeOnScreen("<", xCoordinate + text[i].length()*dungeon.getFontSize(), yCoordinate);
			}
		}
	}
	
	public void menu()
	{
		Menu menu = dungeon.mainMenu;
		drawBox(0, dungeon.panel.getRows() - textBoxHeight, dungeon.panel.getColumns() - menu.getWidth() + 1, textBoxHeight);
		drawBox(dungeon.panel.getColumns() - menu.getWidth(), 0, menu.getWidth(), dungeon.panel.getRows());
		if(menu.getCurrentState() >=0 && menu.getCurrentState() <= 6)
		{
			typeOnScreen(menu.getCurrentStateName(), (dungeon.panel.getColumns()-(menu.getWidth()-1))*dungeon.getTileSize()+dungeon.getFontSize(), dungeon.getTileSize());
			if(menu.getCurrentState() == 6)
				typeOnScreen(menu.getSelectedItem().getItemType().equals("Consumable")?"Use?":"Equip?", (dungeon.panel.getColumns()-(menu.getWidth()-1))*dungeon.getTileSize()+dungeon.getFontSize(), dungeon.getTileSize()*2);
			for(int i = 0; i < menu.getList().length; i++)
			{
				typeOnScreen(menu.getList()[i], (dungeon.panel.getColumns()-(menu.getWidth()-1))*dungeon.getTileSize()+dungeon.getFontSize(), dungeon.getTileSize()*(i+3));
			}
			if(menu.getCurrentState() == 0)
				typeOnScreen(menu.getStateDescription(), dungeon.getTileSize(), (dungeon.panel.getRows() - textBoxHeight +1)*dungeon.getTileSize());
			else if(menu.getSelectedItem() != null)
				typeOnScreen(menu.getSelectedItem().getDescription(), dungeon.getTileSize(), (dungeon.panel.getRows() - textBoxHeight +1)*dungeon.getTileSize());
		}
		typeOnScreen(">", (dungeon.panel.getColumns()-(menu.getWidth()-1))*dungeon.getTileSize(), dungeon.getTileSize()*(menu.getCursorLocation()+3));
		drawBox(0, 0, 5, 2);
		typeOnScreen(((Integer.parseInt(dungeon.player.getHealth()) > 99)?"":" ") + dungeon.player.getHealth() + "/" + dungeon.player.getMaxHealth(), 15, 8);
	}
	
	public void drawBox(int x, int y, int width, int height)
	{
		int boxPart = 285;
		for(int i = 0; i < height; i++)
		{
			for(int j = 0; j < width; j++)
			{
				if(i == 0){
					boxPart = 255;
					if(j == 0)
						boxPart = 254;
					if(j == width-1)
						boxPart = 256;
				}
				if(i != 0 && i != height-1){
					boxPart = 285;
					if(j == 0)
						boxPart = 284;
					if(j == width-1)
						boxPart = 286;
				}
				if(i == height-1){
					boxPart = 315;
					if(j == 0)
						boxPart = 314;
					if(j == width-1)
						boxPart = 316;
				}
				g.drawImage(dungeon.getImageFor(boxPart), (x+j)*dungeon.getTileSize(), (y+i)*dungeon.getTileSize(), null);
			}
		}
	}
	
	public void typeOnScreen(String message, int x, int y)
	{
		char[] box = message.toCharArray();
		for(int i = 0; i < box.length; i++)
		{
			g.drawImage(dungeon.getImageFor(box[i]), x + i*dungeon.getFontSize(), y, null);
		}
	}
	
	public void textBox(String message)
	{
		if(messageCount == 0)
		{
			List<String> text = new ArrayList<String>();
			int startPoint = 0;
			int cutPoint = 0;
			int interval = (panel.getColumns()-2)*2;
			for(int i = 0; i < message.length(); i++)
			{
				if(message.charAt(i) == ' ' || i >= message.length()-1){
					if(i - startPoint < interval){
						cutPoint = i+1;
					}
					else{
						text.add(message.substring(startPoint, cutPoint));
						startPoint = cutPoint;
					}
					if(i >= message.length()-1)
						text.add(message.substring(startPoint));
				}	

			}
			msg = new String[text.size()];
			text.toArray(msg);
		}
		if(nextBox)
		{
			nextBox = false;
			if(messageCount > msg.length - 1 && messageCount != 0)
			{
				messageCount = 0;
				dungeon.setGameState(1);
				return;
			}
				
			drawBox(0, panel.getRows()-textBoxHeight, panel.getColumns(), textBoxHeight);

			int countX = 1, countY = panel.getRows()-textBoxHeight+1;
			while(countY <= panel.getRows()-2)
			{
				typeOnScreen(msg[messageCount], countX * dungeon.getTileSize(), countY * dungeon.getTileSize());
			
				countX = 1;
				countY++;
				messageCount++;
				if(messageCount >= msg.length || messageCount == 0)
					break;				
			}
		}
	}
	
	public void setNextBox(boolean state)
	{
		nextBox = state;
	}
	
}
