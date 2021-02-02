
public class Dice {

	public static boolean roll(int odds)
	{
		boolean result;
		if(Math.random()*100 <= odds)
			result = true;
		else
			result = false;
		return result;
	}
}
