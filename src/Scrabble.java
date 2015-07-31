

import acm.program.*;
import acm.util.RandomGenerator;
import acm.graphics.*;

import java.awt.Color;
import java.awt.event.MouseEvent;

/** An implementation of the classic Scrabble Board Game using ACM Java. */
public class Scrabble extends GraphicsProgram
{
	private static final long serialVersionUID = 3654346345815245656L;
	// Represents the configuration of the board in a traditional Scrabble
	// game.
	// 0=Blank,21=2*Letter,22=2*Word,31=3*Letter,32=3*Word
	private int[][] boardConfig =
	{
	{ 32, 0, 0, 21, 0, 0, 0, 32, 0, 0, 0, 21, 0, 0, 32 },
	{ 0, 22, 0, 0, 0, 31, 0, 0, 0, 31, 0, 0, 0, 22, 0 },
	{ 0, 0, 22, 0, 0, 0, 21, 0, 21, 0, 0, 0, 22, 0, 0 },
	{ 21, 0, 0, 22, 0, 0, 0, 21, 0, 0, 0, 22, 0, 0, 21 },
	{ 0, 0, 0, 0, 22, 0, 0, 0, 0, 0, 22, 0, 0, 0, 0 },
	{ 0, 31, 0, 0, 0, 31, 0, 0, 0, 31, 0, 10, 0, 31, 0 },
	{ 0, 0, 21, 0, 0, 0, 21, 0, 21, 0, 0, 0, 21, 0, 0 },
	{ 32, 0, 0, 21, 0, 0, 0, 99, 0, 0, 0, 21, 0, 0, 32 },
	{ 0, 0, 21, 0, 0, 0, 21, 0, 21, 0, 0, 0, 21, 0, 0 },
	{ 0, 31, 0, 0, 0, 31, 0, 0, 0, 31, 0, 0, 0, 31, 0 },
	{ 0, 0, 0, 0, 22, 0, 0, 0, 0, 0, 22, 0, 0, 0, 0 },
	{ 21, 0, 0, 22, 0, 0, 0, 21, 0, 0, 0, 22, 0, 0, 21 },
	{ 0, 0, 22, 0, 0, 0, 21, 0, 21, 0, 0, 0, 22, 0, 0 },
	{ 0, 22, 0, 0, 0, 31, 0, 0, 0, 31, 0, 0, 0, 22, 0 },
	{ 32, 0, 0, 21, 0, 0, 0, 32, 0, 0, 0, 21, 0, 0, 32 } };
	// Represents the frequency of each letter. tileCount[0] is the frequency
	// of 'A' and so on.
	private int tileCount[] =
	{ 9, 2, 2, 4, 12, 2, 3, 2, 9, 1, 1, 4, 2, 6, 8, 2, 1, 6, 4, 6, 4, 2, 2, 1, 2, 1 };
	// An array to hold all the Tiles.
	private Tile[] tiles;
	// Random Generator for randomly picking tiles.
	private RandomGenerator rgen = new RandomGenerator();
	// The GLabel in the New Tile Circle
	private GLabel dumpText;
	// A variable to hold the X co-ordinate of the tile being dragged.
	private double lastX;
	// A variable to hold the Y co-ordinate of the tile being dragged.
	private double lastY;
	// A reference to the tile being dragged. If none, then null.
	private GObject gobj;
	// Check whether the user has started the game after clicking on the
	// instructions.
	private boolean gameActive = false;
	// A GLabel to hold the instructions for the game.
	private GLabel instructions;
	// A counter for the number of bricks used.
	// Required by the getRandomTiles() function but declared here for
	// required persistence.
	private int used;

	public static void main(String args[])
	{
		new Scrabble().start(args);
	}

	public void run()
	{
		// Modify the screen size so that the user can see everything.
		setSize(1200, 1200);
		// Pause to allow the screen size update to kick in.
		pause(500);
		// Each square of the board
		GRect temp;
		// Set the background color
		this.setBackground(Color.BLACK);
		for (int i = 0; i < 15; i++) // x co-ordinate (column)
		{
			for (int j = 0; j < 15; j++)// y co-ordinate (row)
			{
				temp = new GRect(i * 40 + 50, j * 40 + 50, 40, 40);
				// Look at boardConfig to see how this square looks.
				int current = boardConfig[j][i];
				// Label to hold the text which goes inside special
				// squares.
				GLabel scr = new GLabel("");
				scr.setColor(Color.WHITE);
				scr.setFont("Open Sans Light-15");
				if (current == 32)// 3 * Word
				{
					temp.setFillColor(Color.RED);
					scr.setLabel("3W");
				}
				else if (current == 31)// 3*Letter
				{
					temp.setFillColor(Color.BLUE);
					scr.setLabel("3L");
				}
				else if (current == 22)// 2*Word
				{
					temp.setFillColor(Color.GREEN);
					scr.setLabel("2W");
				}
				else if (current == 21)// 2*Letter
				{
					temp.setFillColor(Color.CYAN);
					scr.setLabel("2L");
				}
				else if (current == 99)// Start Square
				{
					temp.setFillColor(Color.YELLOW);
				}
				else
				// Blank squares
				{
					temp.setFillColor(Color.WHITE);
				}
				// Add the type string
				add(scr, (i) * 40 + 60, (j) * 40 + 75);
				temp.setFilled(true);
				add(temp);
				// Bring the String Description in front of the square
				scr.sendToFront();
			}
		}
		// Counter hold the index value for the tiles[] array.
		int c = 0;
		tiles = new Tile[100];
		for (int i = 0; i < tileCount.length; i++)// for every letter in
									// tileCount[]...
		{
			for (int j = 0; j < tileCount[i]; j++)
			{
				tiles[c] = new Tile((char) ('A' + i));
				// add that letter that many time to tiles[]
				c++;
			}
		}
		// Last two tiles are blanks. Here, I've used # to represent blanks.
		tiles[98] = new Tile('#');
		tiles[99] = new Tile('#');
		// The New Tile Creator
		GOval dump = new GOval(200, 200);
		dump.setFillColor(Color.WHITE);
		dump.setFilled(true);
		add(dump, 950, getHeight() / 2 - 100);
		dumpText = new GLabel("100 Tiles Left.");
		dumpText.setColor(Color.BLACK);
		add(dumpText, 1000, 350);
		// The first tile rack
		GRect rack1 = new GRect(750, 50, 40, 600);
		rack1.setFillColor(Color.GREEN);
		rack1.setFilled(true);
		add(rack1);
		// The second tile rack
		GRect rack2 = new GRect(860, 50, 40, 600);
		rack2.setFillColor(Color.GREEN);
		rack2.setFilled(true);
		add(rack2);
		// Fill the first tile rack
		for (int i = 50; i < 50 + (8 * 35 + 10); i += 45)
		{
			// See function declaration below
			add(getRandomTile(), 750, i);
		}
		// Fill the second tile rack
		for (int i = 50; i < 50 + (8 * 35 + 10); i += 45)
		{
			// See function declaration below
			add(getRandomTile(), 860, i);
		}
		// The GLabel that holds the instructions at the beginning of the
		// game.
		instructions = new GLabel(
				"Click to Start the Game.Drag the tiles to where you want them.Click on the white circle to get more tiles.");
		instructions.setFont("Open Sans-25");
		instructions.setColor(Color.RED);
		add(instructions, getWidth() / 2 - instructions.getWidth() / 2, getHeight() / 2
				- instructions.getAscent() / 2);
		// Add the mouse listeners.
		addMouseListeners();
	}

	/**
	 * This function returns a random tile from the non-null tiles remaining
	 * in the game
	 * 
	 * @return A random tile from the "bag" of unused tiles.
	 */
	private Tile getRandomTile()
	{
		// The tile to use.
		if (used != 100)
		{
			int temp = rgen.nextInt(100 - used);
			// Update the New Tile Creator "Tiles Left" Message
			String str = dumpText.getLabel().substring(0,
					dumpText.getLabel().indexOf("T") - 1);
			int n = Integer.parseInt(str);
			dumpText.setLabel((n - 1) + " Tiles Left.");
			// Store the chosen tile in a new variable
			Tile t = tiles[temp];
			// Swap the chosen tile with the last unused one
			tiles[temp] = tiles[tiles.length - used - 1];
			used++;
			return t;
		}
		else
			return null;
	}

	public void mouseClicked(MouseEvent evt)
	{
		// Do nothing if the user hasn't clicked on the instruction screen
		// yet
		if (gameActive)
		{
			// If the user has clicked on the "New Tile Creator"...
			GObject element = getElementAt(evt.getX(), evt.getY());
			if (element != null)
			{
				if (element instanceof GOval || (element.getX() == 1000)
						&& (element.getY() == 350) && (element instanceof GLabel))
				{
					// generate a new tile
					Tile t = getRandomTile();
					try
					{
						add(t, evt.getX(), evt.getY());
					}
					catch (NullPointerException e)
					{
						//do nothing cuz this means that bleh blah bleh
					}
					t.sendToFront();
				}
			}
		}
		else
		{
			// start the game because this means that the user just
			// clicked on the instruction screen
			gameActive = true;
			this.remove(instructions);
		}
	}

	public void mousePressed(MouseEvent e)
	{
		// get the element at the site of click. null if none present
		lastX = e.getX();
		lastY = e.getY();
		gobj = getElementAt(lastX, lastY);
	}

	/** Called on mouse drag to reposition the object */
	public void mouseDragged(MouseEvent e)
	{
		// Do nothing if game inactive
		if (gameActive)
		{
			// If the object being dragged is actually an object and is a
			// tile
			// Tile Size is 35 by 35 px
			if (gobj != null && gobj.getHeight() == 35)
			{
				// Drag the tile
				gobj.move(e.getX() - lastX, e.getY() - lastY);
				lastX = e.getX();
				lastY = e.getY();
			}
		}
	}
}
