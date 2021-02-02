import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class TitleScreen {
	private World dungeon;
	private BufferedImage background;
	private boolean onMain = true;
	private int cursorLocation = 0;
	private String[] mainText = {"START", "INFINITE", "OPTIONS", "EXIT"};
	private String[] optionsText = {"VIEW:", "WINDOWED", "BACK"};
	
	
	public TitleScreen(World dungeon)
	{
		this.dungeon = dungeon;
		try {
			background = ImageIO.read(new File("smalltitleScreen.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public void activate()
	{
		if(onMain)
		{
			switch(cursorLocation)
			{
			case 0: dungeon.setGameState(1); break;
			case 1:	break;
			case 2: onMain = false; 
			cursorLocation = 1; break;
			case 3: System.exit(cursorLocation);
			}
		}
		else
		{
			switch(cursorLocation)
			{
			case 0: break;
			case 1: 
				dungeon.toggleFullScreen();
				optionsText[1] = dungeon.panel.getIsFullScreen()?"FULLSCREEN":"WINDOWED";
				break;
			case 2:	onMain = true;
				cursorLocation = 0; break;
			}
		}
	}
	
	public void moveCursor(int keyPressed)
	{
		if(keyPressed == 38 && ((onMain && cursorLocation != 0) || (!onMain && cursorLocation != 1)))
			cursorLocation -= 1;
		if(keyPressed == 40 && ((onMain && cursorLocation != mainText.length-1) || (!onMain && cursorLocation != optionsText.length-1)))
			cursorLocation += 1;
	}
	
	public BufferedImage getBackground()
	{
		return background;
	}
	
	public boolean getOnMain()
	{
		return onMain;
	}
	
	public void setOnMain(boolean newState)
	{
		onMain = newState;
	}
	
	public int getCursorLocation()
	{
		return cursorLocation;
	}
	
	public void setCursorLocation(int newLocation)
	{
		cursorLocation = newLocation;
	}
	
	public String[] getMainText()
	{
		return mainText;
	}
	
	public String[] getOptionsText()
	{
		return optionsText;
	}
}
