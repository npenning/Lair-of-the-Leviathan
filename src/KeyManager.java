import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyManager implements KeyListener{
	
	private World dungeon;
	private boolean[] keyPressed = new boolean[256];
	
	public KeyManager(World dungeon)
	{
		this.dungeon = dungeon;
		for(boolean key: keyPressed)
		{
			key = false;
		}
	}
	
	public void recieveKey(int key)
	{
		switch(dungeon.getGameState())
		{
		//title menu
		case 0: 
			switch(key)
			{
			case 38:
			case 40: dungeon.titleScreen.moveCursor(key); break;
			case 88: dungeon.titleScreen.activate();
			}
			break;
		//overworld
		case 1:
		{
			if(!dungeon.getIsMoving())
			{
				switch(key)
				{
				case 10: dungeon.setGameState(3);
				dungeon.mainMenu.setCurrentState(0); 
				dungeon.mainMenu.setCursorLocation(0);break;
				case 37: dungeon.setMoveDirection(0, -1); 
				dungeon.setMoveDirection(1, 0); break;
				case 38: dungeon.setMoveDirection(1, -1);
				dungeon.setMoveDirection(0, 0); break;
				case 39: dungeon.setMoveDirection(0, 1);
				dungeon.setMoveDirection(1, 0); break;
				case 40: dungeon.setMoveDirection(1, 1);
				dungeon.setMoveDirection(0, 0); break;
				case 88: dungeon.interact(); break;
				case 90:
					if(dungeon.player.getEquippedTool() != null)
						dungeon.player.getEquippedTool().use(); break;
				case 70:
					dungeon.fight();
				}
			}
		}
		break;
		//text box
		case 2: 
			switch(key)
			{
			case 88:
			case 90: dungeon.panel.animate.setNextBox(true); break;
			}
		//pause menu
		case 3: 
		{
			switch(key)
			{
			case 10: dungeon.setGameState(1); break;
			case 38:
			case 40: dungeon.mainMenu.moveCursor(key); break;
			case 88: dungeon.mainMenu.activate(); break;
			case 90: dungeon.mainMenu.back();
			}
		}
		break;
		}
	}
	
	public void recieveKeyRelease(int key)
	{
		/*
		switch(key)
		{
		case 37: 
		case 39: dungeon.setMoveDirection(0, 0); break;
		case 38: 
		case 40: dungeon.setMoveDirection(1, 0); break;
		case 88: 
		}
		*/
	}
	
	public void setIsPressing(boolean isPressing, int key)
	{
		this.keyPressed[key] = isPressing;
	}
	
	public boolean getIsPressing(int key)
	{
		return keyPressed[key];
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
			if(!getIsPressing(key)){
				recieveKey(key);
		}
		setIsPressing(true, key);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		recieveKeyRelease(key);
		setIsPressing(false, key);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
