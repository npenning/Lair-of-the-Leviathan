
public class PlacedEnemy extends SpecialTile{
	Monster enemy;
	public PlacedEnemy(NormalTile tile, World dungeon, Monster monster)
	{
		super(tile, dungeon);
		enemy = monster;
		super.setID(408, 0);
		super.setSpecialTileType("Monster");
		setIsSolid(0);
		setIsWall(0);
	}

	public void activate() {
		
		
	}

}
