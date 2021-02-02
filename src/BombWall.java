
public class BombWall extends SpecialTile{

	public BombWall(NormalTile nTile, World dungeon) {
		super(nTile, dungeon);
		super.setSpecialTileType("Bombable Wall");
		setIsWall(0);
	}
	public void activate()
	{
		for(Integer[] coordinates: super.getAffectedTileSet())
		{
			super.dungeon.getGrid(coordinates[0], coordinates[1]).toggle();
		}
		toggleIsSpecial();
	}
}
