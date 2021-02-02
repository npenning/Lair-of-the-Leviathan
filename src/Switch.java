
public class Switch extends SpecialTile{

	public Switch(NormalTile nTile, World dungeon) {
		super(nTile, dungeon);
		setIsSolid(1);
		setID(176, 0);
		setTrueID(299, 0);
		super.setSpecialTileType("Switch");
	}


	public void activate() {
		for(Integer[] coordinates: super.getAffectedTileSet())
		{
			super.dungeon.getGrid(coordinates[0], coordinates[1]).toggle();
		}
		toggle();
		
	}

}
