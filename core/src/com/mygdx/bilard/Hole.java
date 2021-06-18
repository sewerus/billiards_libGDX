package com.mygdx.bilard;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Circle;

public class Hole extends Circle {
	private static final long serialVersionUID = 1L;
	private int id;
	float a, b; // container point
	private ArrayList<Bill> caughtBills = new ArrayList<Bill>();

	public Hole(int id, float x, float y, int radius, float a, float b) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.radius = radius;
		this.a = a;
		this.b = b;
	}

	public void showContent(ShapeRenderer shapeRenderer) {
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.rectLine(a, b + radius * 5, a, b, 3, Color.DARK_GRAY, Color.DARK_GRAY);
		shapeRenderer.rectLine(a, b, a + 2 * radius, b, 3, Color.DARK_GRAY, Color.DARK_GRAY);
		shapeRenderer.rectLine(a + 2 * radius, b, a + 2 * radius, b + radius * 5, 3, Color.DARK_GRAY, Color.DARK_GRAY);
		shapeRenderer.end();
		for (int i = 0; i < caughtBills.size(); i++) {
			if (caughtBills.get(i).y - caughtBills.get(i).radius > b + 3 + (caughtBills.size()-1)*caughtBills.get(i).radius*2) {
				caughtBills.get(i).v_y -= 0.1;
			} else {
				caughtBills.get(i).v_y = 0;
			}
			caughtBills.get(i).move(0);
			caughtBills.get(i).draw(shapeRenderer);
		}
	}

	public boolean isCaughtBill(Bill bill) {
		if ((x - bill.x) * (x - bill.x) + (y - bill.y) * (y - bill.y) <= (radius + bill.radius / 2)
				* (radius + bill.radius / 2))
			return true;
		else
			return false;
	}

	public void caughtBill(Bill bill) {
		caughtBills.add(bill);
		caughtBills.get(caughtBills.lastIndexOf(bill)).x = a + radius;
		caughtBills.get(caughtBills.lastIndexOf(bill)).y = b + radius * 5;
		caughtBills.get(caughtBills.lastIndexOf(bill)).v_x = 0F;
		caughtBills.get(caughtBills.lastIndexOf(bill)).v_y = -5;
	}

	public void draw(ShapeRenderer shapeRenderer) {
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(Color.DARK_GRAY);
		shapeRenderer.circle(x, y, radius);
		shapeRenderer.end();
		showContent(shapeRenderer);
	}

	public void drawNumber(SpriteBatch batch, BitmapFont font, ShapeRenderer shapeRenderer) {
		batch.begin();
		font.draw(batch, Integer.toString(id + 1), x, y);
		font.draw(batch, Integer.toString(id + 1), a + radius - 3, b + radius * 6);
		batch.end();
		for (int i = 0; i < caughtBills.size(); i++) {
			caughtBills.get(i).drawNumber(batch, font, shapeRenderer);
		}
	}
}
