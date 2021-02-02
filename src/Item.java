
public abstract class Item {
	private int itemID;
	private String itemType;
	private String name;
	private String description;
	protected World dungeon;
	private static int inventoryCount;
	
	public Item(int id, String name, String description)
	{
		inventoryCount = 0;
		itemID = id;
		this.name = name;
		this.description = description;
		this.dungeon = dungeon;
	}
	
	public Item(int id, String name, String description, World dungeon)
	{
		inventoryCount = 0;
		itemID = id;
		this.name = name;
		this.description = description;
		this.dungeon = dungeon;
	}
	
	public String getName()
	{
		return name;
	}
	
	public int getID()
	{
		return itemID;
	}
	
	public String getDescription()
	{
		return description;
	}
	
	public void setItemType(String type)
	{
		itemType = type;
	}
	
	public String getItemType()
	{
		return itemType;
	}
	
	public abstract int getEffectiveness();
}
