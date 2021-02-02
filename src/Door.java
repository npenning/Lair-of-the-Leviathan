

public class Door extends SpecialTile{

	public Door(NormalTile nTile, World dungeon)
	{
		super(nTile, dungeon);
		setIsWall(0);
	}

	
	public void activate() {
		if(super.dungeon.getKeyCount() > 0)
		{
			super.dungeon.useKey();
			if(super.getAffectedTileSet() != null){
				for(Integer[] coordinates: super.getAffectedTileSet())
				{
					super.dungeon.getGrid(coordinates[0], coordinates[1]).toggle();
				}
			}
			super.dungeon.panel.setMessage("The door opened with a click!");
			toggleIsSpecial();
			toggle();
		}
		else
		{
			super.dungeon.panel.setMessage("The door is locked...");
		}
		
	}
}
