
public class Sign extends SpecialTile{

	public Sign(NormalTile nTile, World dungeon) {
		super(nTile, dungeon);
		super.setID(411, 1);
		setIsWall(0);
	}

	
	public void activate() {
		super.dungeon.panel.setMessage(super.getMessage(getState()));
		
	}

}
