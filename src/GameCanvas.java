import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class GameCanvas extends JPanel{
	private int panelX = 15;
	private int panelY = 11;
	public Animator animate;
	private BufferedImage[][][] screenTiles;
	private World dungeon;
	private String message;
	private BufferedImage screen;
	private boolean isFullScreen;
	private Graphics2D g2d;

	
	public GameCanvas()
	{
		screenTiles = new BufferedImage[panelX+2][panelY+2][3];
		
		/*
		for(BufferedImage[] t: screenTiles)
		{
			for(BufferedImage tile: t)
			{
				tile = new BufferedImage(panelX, panelX, panelX);
			}
		}
		*/
	}
	
	
	@Override
	public Dimension getPreferredSize()
	{
		return new Dimension((int) ((this.getParent().getHeight())*((double)panelX/panelY)), this.getParent().getHeight());
	}
	
	
	@Override
	public void paint(Graphics g) 
	{
		//animate.textBox("Hello there!", g);

		switch(dungeon.getGameState())
		{
		case 0: animate.titleScreen();break;
		case 1: animate.overWorld(); break;
		case 2: animate.textBox(message); break;
		case 3: animate.menu(); break;
		case 4: animate.combat(); break;
		}
		
		g.drawImage(screen.getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_DEFAULT), 0, 0, null);
		/*
		for(int i=0; i < panelY; i++)
		{
			for(int j = 0; j < panelX; j++)
			{
				Tile currentTile = dungeon.getGrid(dungeon.getPlayerX()-((panelX-1)/2)+j, dungeon.getPlayerY()-((panelY-1)/2)+i);
				g.drawImage(dungeon.getImageFor(' '), j*tileSize, i*tileSize, null);
				g.drawImage(currentTile.getImage(), j * tileSize, i*tileSize, null);
				if(i == (panelY-1)/2 && j==(panelX-1)/2){
					g.drawImage(dungeon.getPlayerTile(), (panelX-1)/2 * tileSize, (panelY-1)/2 * tileSize, null);
				}
				if(currentTile.getIsForeGround())
					g.drawImage(currentTile.getImage(), j * tileSize, i*tileSize, null);
				
			}
		}
		*/
	}
	
	public int getRows()
	{
		return panelY;
	}
	
	public int getColumns()
	{
		return panelX;
	}
	
	public void toggleIsFullScreen()
	{
		isFullScreen = !isFullScreen;
	}
	
	public boolean getIsFullScreen(){
		return isFullScreen;
	}
	
	public void setAnimator(Animator animate)
	{
		this.animate = animate;
	}
	
	public BufferedImage getTileImage(int x, int y, int layer)
	{
		return screenTiles[x][y][layer];
	}
	
	public void setTileImage(BufferedImage image, int x, int y, int layer)
	{
		screenTiles[x][y][layer] = image;
	}
	
	public void setTileSetImages(Tile[][] grid, int playerX, int playerY)
	{
		for(int layer = 0; layer<3; layer++)
		{
			int x = playerX -1 - (panelX-1)/2;
			int y = playerY -1 - (panelY-1)/2;
			for(int i = 0; i < panelX + 2; i++)
			{
				for(int j = 0; j < panelY + 2; j++)
				{
					int num = grid[x+i][y+j].getID(layer);
					if(num < 0)
						num = 0;
					setTileImage( dungeon.getImageFor(num), i, j, layer);
				}
			}
		}
	}
	
	public void setMessage(String message)
	{
		this.message = message;
		dungeon.panel.animate.setNextBox(true);
		dungeon.setGameState(2);
	}
	
	public void setScreen()
	{
		screen = new BufferedImage(dungeon.getTileSize() * getColumns(), dungeon.getTileSize()*getRows(), BufferedImage.TYPE_INT_RGB);
		g2d = (Graphics2D) screen.getGraphics();
		animate = new Animator(this, dungeon, g2d);
	}
	
	public void setWorld(World dungeon)
	{
		this.dungeon = dungeon;
	}

}