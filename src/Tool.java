
public class Tool extends Item{

	private boolean isActive;
	private char direction;
	private int[] coordinates;
	private int timer;
	private int image;
	private boolean firstPass;
	
	public Tool(int id, String name, String description, World dungeon) {
		super(id, name, description, dungeon);
		
		coordinates = new int[2];
		super.setItemType("Tool");
		firstPass = true;
	}

	public void update()
	{
		if(firstPass){
			coordinates[0] = super.dungeon.getPlayerX();
			coordinates[1] = super.dungeon.getPlayerY();
			direction = super.dungeon.getPlayerDirection();
			firstPass = false;
			switch(super.getName())
			{
			case "Bombs":
				timer = 200;
				image = 438;
				break;
			case "Bow":
				switch(direction)
				{
				case 'u': image = 468; break;
				case 'd': image = 469; break;
				case 'r': image = 470; break;
				case 'l': image = 471; break;
				}
				timer = 0;
			}
		}
		else
		{
			switch(super.getName())
			{
			case "Bombs": timer--;
				if(timer%5 == 0)
					image = (image == 438?439:438);
				if(timer == 0)
				{
					for(int i = -1; i<2; i++){
						for(int j = -1; j<2; j++){
							Tile t = dungeon.getGrid(coordinates[0] + i, coordinates[1] + j);
							if(t.getIsSpecial())
								if(((SpecialTile)t).getSpecialTileType() == "Bombable Wall" || ((SpecialTile)t).getSpecialTileType() == "Switch")
									((SpecialTile)t).activate();
						}
					}
					stop();
				}
				break;
			case "Bow" :
				timer++;
				if(timer%2 == 0){
					if(dungeon.getGrid(coordinates[0], coordinates[1]).getIsSpecial() && ((SpecialTile)dungeon.getGrid(coordinates[0], coordinates[1])).getSpecialTileType() == "Switch")
					{
						((Switch)dungeon.getGrid(coordinates[0], coordinates[1])).activate();
						stop();
					}
					else{
						switch(direction)
						{
						case 'u': 
							if(dungeon.getGrid(coordinates[0], coordinates[1]-1).getIsWall())
								stop();
							else
								coordinates[1]--;
							break;
						case 'd': 
							if(dungeon.getGrid(coordinates[0], coordinates[1]+1).getIsWall())
								stop();
							else
							coordinates[1]++;
							break;
						case 'r':
							if(dungeon.getGrid(coordinates[0]+1, coordinates[1]).getIsWall())
								stop();
							else
							coordinates[0]++;
							break;
						case 'l':
							if(dungeon.getGrid(coordinates[0]-1, coordinates[1]).getIsWall())
								stop();
							else
							coordinates[0]--;
							break;
						}
					}
				}
				break;
			}
			
			
		}
	}
	
	private void stop()
	{
		isActive = false;
		firstPass = true;
		image = 0;
	}
	
	public void use()
	{
		isActive = true;
	}
	
	public int[] getCoordinates()
	{
		return coordinates;
	}
	
	public boolean getIsActive()
	{
		return isActive;
	}
	
	public void toggleIsActive()
	{
		isActive = !isActive;
	}
	
	public int getImageID()
	{
		return image;
	}

	@Override
	public int getEffectiveness() {
		// TODO Auto-generated method stub
		return 0;
	}

}
