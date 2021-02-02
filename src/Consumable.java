
public class Consumable extends Item{

	private int effectType;
	private int effectAmount;
	
	
	
	public Consumable(int id, String name, String description, int type, int amount) {
		super(id, name, description);
		super.setItemType("Consumable");
		effectType = type;
		effectAmount = amount;
	}

	
	
	
	@Override
	public int getEffectiveness() {
		return effectAmount;
	}

}
