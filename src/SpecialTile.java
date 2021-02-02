import java.util.ArrayList;

public abstract class SpecialTile extends Tile{

	private ArrayList<Integer[]> effectArea;
	private String specialTileType;
	private String[] message;
	protected World dungeon;
	
	public SpecialTile(NormalTile nTile, World dungeon)
	{
		super();
		for(int i=0; i<3; i++)
		{
			super.setID(nTile.getID(i), i);
		}
		nTile.toggle();
		for(int i=0; i<3; i++)
		{
			super.setTrueID(nTile.getID(i), i);
		}
		super.toggleIsSpecial();
		message = new String[2];
		this.dungeon = dungeon;
		setIsSolid(0);
	}
	
	public abstract void activate();
	
	public void setMessage(int state, String newMessage)
	{
		message[state] = newMessage;
	}
	
	public String getMessage(int state)
	{
		return message[state];
	}
	
	public ArrayList<Integer[]> getAffectedTileSet()
	{
		return effectArea;
	}
	
	public void setEffectArea(ArrayList<Integer[]> list)
	{
		this.effectArea = list;
	}
	
	public void setSpecialTileType(String type)
	{
		specialTileType = type;
	}
	
	public String getSpecialTileType()
	{
		return specialTileType;
	}
	
}
