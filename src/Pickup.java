

public class Pickup extends SpecialTile{

	private String type;
	
	public Pickup(NormalTile nTile, World dungeon, String type)
	{
		super(nTile, dungeon);
		super.setSpecialTileType("Pickup");
		this.type = type;
		if(type.equals("potion")){
			setID(380, 1);
			setMessage(0, "You got a Health Potion!");
		}
		if(type.equals("key")){
			setID(381, 1);
			setMessage(0, "You got a Key!");
		}
		
	}

	public void activate() {
		switch(type)
		{
			case "key":
				super.dungeon.addKey(); break;
			case "potion":
				if(super.dungeon.player.getItemList("Consumable").length < 7){
					super.dungeon.player.addItem(new Consumable(02, "HlthPtn", "Heals 20 Hp", 0, 20)); break;
				}else{
					super.dungeon.panel.setMessage("You can't hold any more potions!");
					return;
				}
		}
		super.dungeon.panel.setMessage(this.getMessage(super.getState()));
		toggleIsSpecial();
		toggle();
	}
	
}
