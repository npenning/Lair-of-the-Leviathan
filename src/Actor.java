


public class Actor {
	
	private int maxHealth;
	private int health;
	private int refID;
	private int attackDamage;
	
	public Actor(int maxHp)
	{
		maxHealth = maxHp;
		health = maxHealth;
	}
	
	/*
	 * 
	 * Health Management
	 * 
	 */
	public void takeDamage(int amountDamaged)
	{
		health -= amountDamaged;
	}
	
	public void heal(int amountHealed)
	{
		health += amountHealed;
		if(health > maxHealth)
			health = maxHealth;
	}
	
	public int getHp()
	{
		return health;
	}
	
	public String getHealth()
	{
		if(health >= 100)
			return Integer.toString(health);
		else
			return " " + Integer.toString(health);
	}
	
	public int getMaxHp()
	{
		return maxHealth;
	}
	
	public String getMaxHealth()
	{
		return Integer.toString(maxHealth);
	}
	
	
	
	
	
	/*
	 * 
	 * Attack Damage getter/setter
	 * 
	 */
	
	public int getAttackDamage()
	{
		return attackDamage;
	}
	
	public void setAttackDamage(int damage)
	{
		attackDamage = damage;
	}
	
}
