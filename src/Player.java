
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Player extends Actor{
	
	private Weapon equippedWeapon;
	private Tool equippedTool;
	private ArrayList<Item> inventory = new ArrayList<Item>();
	private int level;
	private int xp;
	private BufferedImage[] attackFrames, dodgeFrames;
	
	public Player(int maxHp)
	{
		super(maxHp);
		setAttackFrames();
		
		inventory.add(new Weapon(01, "OldSwrd", "Old Iron Sword", 14));
		inventory.add(new Consumable(02, "HlthPtn", "Heals 20 Hp", 0, 20));
		inventory.add(new Weapon(03, "GoodSwrd", "Sharp Steel Sword", 18));
		inventory.add(new Weapon(04, "LongSwrd", "A Classic Claymore", 20));
		
		useItem(inventory.get(0));
		//takeDamage(50);
		level = 1;
	}
	
	private void setAttackFrames()
	{
		attackFrames = new BufferedImage[25];
		dodgeFrames = new BufferedImage[3];
		try {
			BufferedImage attackSource = ImageIO.read(new File("playerAttack.png"));
			for(int i = 0; i<5; i++)
				for(int j = 0; j<5; j++)
				{
					attackFrames[i*5+j] = attackSource.getSubimage(i*90, j*60, 90, 60);
				}
			BufferedImage dodgeSource = ImageIO.read(new File("playerDodge.png"));
			for(int i = 0; i<3; i++)
				dodgeFrames[i] = dodgeSource.getSubimage(i*56, 0, 56, 60);
				
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Weapon getEquippedWeapon()
	{
		return equippedWeapon;
	}
	
	public Tool getEquippedTool()
	{
		return equippedTool;
	}
	
	public void addItem(Item thing)
	{
		inventory.add(thing);
	}
	
	public void removeItem(int id)
	{
		for(Item thing: inventory)
		{
			if(thing.getID() == id)
				inventory.remove(thing);
		}
	}
	
	public void useItem(Item thing)
	{
		switch(thing.getItemType())
		{
		case "Consumable": super.heal(thing.getEffectiveness());
		inventory.remove(thing); break;
		case "Weapon" : equippedWeapon = (Weapon) thing;
		super.setAttackDamage(thing.getEffectiveness()); break;
		case "Tool": equippedTool = (Tool) thing;
		}
	}
	
	public String[] getItemList(String itemType)
	{
		ArrayList<String> selection = new ArrayList<String>();
		selection.clear();
		for(Item thing: inventory)
		{
			if(thing.getItemType().equals(itemType))
				selection.add(thing.getName());
		}
		String[] SelectionArray = new String[selection.size()];
		selection.toArray(SelectionArray);
		return SelectionArray;
	}
	
	public ArrayList<Item> getInventory()
	{
		return inventory;
	}
	
	public int getLevel()
	{
		return level;
	}
	
	public void levelUp()
	{
		level++;
	}
	
	public int getXp()
	{
		return xp;
	}
	
	public void addXp(int amount)
	{
		xp += amount;
	}
}
