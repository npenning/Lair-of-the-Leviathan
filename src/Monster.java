

public abstract class Monster extends Actor{
	
	
	public Monster(int maxHp) {
		super(maxHp);
		// TODO Auto-generated constructor stub
	}
	
	private String species;
	
	public abstract void attack();
	
	public abstract void dodge();
	
	public String getSpecies()
	{
		return species;
	}
	
	public void setSpecies(String s)
	{
		species = s;
	}
	
}
