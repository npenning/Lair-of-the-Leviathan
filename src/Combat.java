import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Combat {
	private Player player;
	private Monster enemy;
	private World dungeon;
	private BufferedImage background;
	private int state = 0;
	/*
	 * 0-idle
	 * 1-attack
	 * 2-dodge
	 */
	
	
	//never had time to get to the combat system

	public Combat(Player player, World dungeon)
	{
		
		try {
			background = ImageIO.read(new File("Combat Screen.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.dungeon = dungeon;
		this.player = player;
		enemy = new Skeleton();
		dungeon.setGameState(4);
	}
	
	public BufferedImage getBackground()
	{
		return background;
	}
}
