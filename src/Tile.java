
public abstract class Tile {

	private int[][] tileID;
	private boolean state;
	private boolean isSpecial;
	private boolean[] isSolid, isWall;
		

	
	public Tile()
	{
		tileID = new int[2][3];
		state = false;
		isSolid = new boolean[2];
		isWall = new boolean[2];
	}
	
	public void setIsSolid(int state)
	{
		isSolid[state] = true;
	}
	
	public boolean getIsSolid()
	{
		return isSolid[state?1:0];
	}
	
	public void setIsWall(int state)
	{
		isWall[state] = true;
	}
	
	public boolean getIsWall()
	{
		return isWall[state?1:0];
	}
	
	public void setID(int id, int layer)
	{
		tileID[0][layer] = id;
	}
	public void setTrueID(int id, int layer)
	{
		tileID[1][layer] = id;
	}
	
	public int getID(int layer)
	{
		return tileID[state?1:0][layer];
	}
	
	public void toggle()
	{
		state = !state;
	}
	
	public int getState()
	{
		return state?1:0;
	}
	
	public boolean getIsSpecial()
	{
		return isSpecial;
	}
	
	public void toggleIsSpecial()
	{
		isSpecial = !isSpecial;
	}
	
	

}
