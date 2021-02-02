
public class Menu {
	
	private World dungeon;
	private String[] menuStates = {"Player", "Item", "Tool", "Weapon", "Armor", "Quit", "Back"};
	private String[] stateDescriptions = {"About the player", "Consumable Items", "Overworld Items", "Combat Weapons", "Combat Armor", "Exit the Game", "Return to Game"};
	private int currentState;
	private String currentStateName;
	private int previousState;
	private int selection;
	private int cursorLocation;
	private int width;
	private Item selectedItem;
	private boolean empty = false;
	
	public Menu(World dungeon)
	{
		this.dungeon = dungeon;
		currentState = 0;
		currentStateName = "Menu";
		cursorLocation = 0;
		selection = 0;
		width = 6;
	}
	
	public void moveCursor(int keyPressed)
	{
		if(currentState != 1)
		{
			if(keyPressed == 38 && cursorLocation != 0)
				cursorLocation -= 1;
			if(keyPressed == 40 && cursorLocation != getList().length-1)
				cursorLocation += 1;
			if(currentState != 0 && currentState != 6){
				setSelectedItem();
			}
		}
	}
	
	public void activate()
	{
		switch(currentState){
		case 0:
			switch(cursorLocation){
			case 0: 
				currentState = cursorLocation+1; 
				cursorLocation = 6;
				break;
			case 1: 
			case 2: 
			case 3: 
			case 4: 
				currentState = cursorLocation+1; 
				cursorLocation = 0;
				setSelectedItem(); break;
			case 5: System.exit(0); break;
			case 6: dungeon.setGameState(1);
			break;
			}
			break;
		case 1: 
			currentState = 0;
			cursorLocation = 0; break;
		case 2:
		case 3:
		case 4: 
		case 5:
			if(empty){
				currentState = 0;
				break;
			}
			previousState = currentState;
			currentState = 6; 
			cursorLocation = 0;
			break;
		case 6:
			if(cursorLocation == 0)
			{
				dungeon.player.useItem(selectedItem);
				dungeon.panel.setMessage("You " + ((selectedItem.getItemType() == "Consumable")?"used ":"equipped ") + "the " + selectedItem.getName() + ".");
			}
			currentState = previousState;
			cursorLocation = 0;
			
		}
		
		
	}
	
	public void back()
	{
		switch(currentState)
		{
			case 0: dungeon.setGameState(1); break;
			case 1:
			case 2:
			case 3:
			case 4:
			case 5: currentState = 0; break;
			case 6: currentState = previousState;
		}
		cursorLocation = 0;
				
	}
	
	public int getCursorLocation()
	{
		return cursorLocation;
	}
	
	public void setCursorLocation(int newPosition)
	{
		cursorLocation = newPosition;
	}
	
	public int getCurrentState()
	{
		return currentState;
	}
	
	public String getCurrentStateName()
	{
		if(currentState == 0)
			return "Menu";
		else if(currentState <= 5){
			return menuStates[currentState-1];
		}
		else if(currentState == 6)
			return selectedItem.getName();
		else return null;
	}
	
	public void setCurrentState(int newState)
	{
		currentState = newState;
		cursorLocation = 0;
	}
	
	public String[] getList()
	{
		String[] list = {};
		switch(currentState)
		{
			case 0: list = menuStates; break;
			case 1: 
				list = new String[7];
				list[0] = "Lv:" + dungeon.player.getLevel();
				list[1] = "Xp:" + dungeon.player.getXp();
				list[2] = "Next:";
				list[3] = "  ";
				list[4] = "";
				list[5] = "";
				list[6] = "Back";
				break;
			case 2: list = dungeon.player.getItemList("Consumable"); break;
			case 3:
			case 4:
			case 5: list = dungeon.player.getItemList(menuStates[currentState-1]); break;
			case 6: 
				list = new String[2];
				list[0] = "Yes";
				list[1] = "No";
		}
		int p = list.length;
		if(list.length == 0){
			list = new String[1];
			list[0] = "Back";
			empty = true;
		}
		else
			empty = false;
		return list;
	}
	
	public int getWidth()
	{
		return width;
	}
	
	public String getStateDescription()
	{
		return stateDescriptions[cursorLocation];
	}
	
	public Item getSelectedItem()
	{
		return selectedItem;
	}
	
	public void setSelectedItem()
	{
		int typeCount = 0;
		for(Item thing: dungeon.player.getInventory())
		{
			if(thing.getItemType().equals(menuStates[currentState-1]) || (thing.getItemType().equals("Consumable") && currentState == 2))
			{
				if(typeCount == cursorLocation){
					selectedItem = thing;
					break;
				}
				else 
					typeCount++;
			}
		}
	}
}
