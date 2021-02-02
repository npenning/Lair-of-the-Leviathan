import java.awt.image.BufferedImage;

public class FontTile {

	private BufferedImage image;
	private char charCode;
	
	public FontTile()
	{
	}
	
	public BufferedImage getImage()
	{
		return image;
	}
	
	public char getChar()
	{
		return charCode;
	}
	
	public void setChar(char c)
	{
		charCode = c;
	}
	
	
	public void setImage(BufferedImage img)
	{
		image = img;
	}
}
