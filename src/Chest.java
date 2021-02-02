

public class Chest extends SpecialTile{

	private Item treasure;
	
	public Chest(NormalTile nTile, World dungeon)
	{
		super(nTile, dungeon);
		super.setID(378, 1);
		super.setTrueID(379, 1);
		setIsSolid(1);
		setIsWall(0);
		setIsWall(1);
	}
	
	
	public Item getTreasure()
	{
		return treasure;
	}
	
	public void setTreasure(Item objectID)
	{
		this.treasure = objectID;
	}
	
	public void activate()
	{
		if(treasure != null)
			super.dungeon.player.addItem(treasure);
		toggleIsSpecial();
		super.dungeon.panel.setMessage(treasure == null?"The chest was empty...":"You got the " + treasure.getName() + "!");
		toggle();
	}
}
