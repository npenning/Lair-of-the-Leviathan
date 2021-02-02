
public class Weapon extends Item{

	private int attackDamage;
	
	public Weapon(int id, String weaponName, String description, int power)
	{
		super(id, weaponName, description);
		super.setItemType("Weapon");
		attackDamage = power;
	}
	
	public int getEffectiveness()
	{
		return attackDamage;
	}
	
	public void setDamage(int power)
	{
		attackDamage = power;
	}
}
