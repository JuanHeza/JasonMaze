/*
 * Model view that renders the different objects of the vacuum cleaner world
 * Author: Romeo Sanchez
 */

import jason.environment.grid.GridWorldView;

import java.awt.Color;
import java.awt.Graphics;

public class BuildingView extends GridWorldView {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	BuildingModel bmodel;

	public BuildingView(BuildingModel model) {
		super(model, "Vaccuum Cleaning Robot", 700);
		bmodel = model;
		setVisible(true);
		repaint();
	}

	/** draw application objects given the values of such objects: DIRT*/
	@Override
	public void draw(Graphics g, int x, int y, int object) {
		switch (object) {
		case BuildingModel.DIRT:
			drawDirt(g, x, y, Color.black, object);
			break;
		}
		//repaint();
	}

	/** Draws the vacuum cleaner */
	@Override
	public void drawAgent(Graphics g, int x, int y, Color c, int id) {
		c = Color.yellow;
		super.drawAgent(g, x, y, c, -1);
		if (id == 0) {
			g.setColor(Color.blue);
		} else {
			g.setColor(Color.red);
		}
		//g.setColor(Color.yellow);
		super.drawString(g, x, y, defaultFont, "XDK");
	}

	public void drawDirt(Graphics g, int x, int y, Color c, int id) {
		c = Color.black;
		g.setColor(c);
	}

}
