package root;

import acm.graphics.*;
import java.awt.Color;

/**
 * A convenience class for representing tiles used in the Scrabble.java file
 * 
 * @author Rishav Kundu
 *
 */
public class Tile extends GCompound
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3214800033385855139L;
	// Represents the score for each letter. scoreCount[0] is the score for
	// 'A' and so on...
	int scoreCount[] =
	{ 1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 5, 1, 3, 1, 1, 3, 10, 1, 1, 1, 1, 4, 4, 8, 4, 10 };

	/**
	 * Returns a Scrabble tile with the character passed inscribed on it along
	 * with it's score'
	 * 
	 * @param c
	 *              The letter on the tile desired.
	 */
	public Tile(char c)
	{
		// call the GCompound constructor
		super();
		c = Character.toUpperCase(c);
		// Create the background for the tile.
		GRoundRect tile = new GRoundRect(0, 0, 35, 35);
		tile.setFillColor(Color.YELLOW);
		tile.setFilled(true);
		super.add(tile);
		// The text on the tile
		GLabel text;
		if (c != '#')
		{
			// Read and understand. Impossible to comment!! Sorry.
			text = new GLabel((new Character(c)).toString() + "(" + scoreCount[c - 'A'] + ")");
		}
		else
			text = new GLabel((new Character(c)).toString() + "(0)");
		// Add the text to the tile
		super.add(text, (35 - text.getWidth()) / 2, (35 + text.getAscent()) / 2);
	}
}
