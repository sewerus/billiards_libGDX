package com.mygdx.bilard;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import java.util.ArrayList;
import java.util.Arrays;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Table extends Rectangle {
	private static final long serialVersionUID = 1L;
	protected int border; // border width
	protected float rubbing;
	protected float lossVehicle;
	protected Sound hitSound;
	protected ArrayList<Bill> bills = new ArrayList<Bill>();
	protected ArrayList<Hole> holes = new ArrayList<Hole>();
	private ArrayList<Color> billColors = new ArrayList<Color>(Arrays.asList(Color.YELLOW, Color.BLUE, Color.RED,
			Color.VIOLET, Color.ORANGE, Color.GREEN, Color.BROWN, Color.BLACK, Color.YELLOW, Color.BLUE, Color.RED,
			Color.VIOLET, Color.ORANGE, Color.GREEN, Color.BROWN));
	private ArrayList<Boolean> billHalf = new ArrayList<Boolean>(Arrays.asList(false, true, true, false, false, true,
			true, false, true, false, false, false, true, false, true));
	protected WhiteBill whiteBill;

	public Table(int x, int y, int width, int height, int border, float rubbing, float lossVehicle, Sound hitSound) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.border = border;
		this.rubbing = rubbing;
		this.lossVehicle = lossVehicle;
		this.hitSound = hitSound;
		prepareBills();
		prepareHoles();
	}

	protected void prepareBills() {
		int radius = 20;
		int counter = 0;
		whiteBill = new WhiteBill(0, (int) (x + width / 2), (int) (y + height / 2), Color.WHITE, false);
		for (int i = 0; i < 5; i++)
			for (int j = 0; j <= i; j++) {
				bills.add(new Bill(counter, (int) (x + width * 3 / 4 + i * (radius + 2) * Math.sqrt(3)),
						(int) (y + height / 2 - radius / 2 + 2 * (radius + 2) * (j - (float) i / 2)),
						billColors.get(counter), billHalf.get(counter)));
				counter++;
			}
	}

	protected void prepareHoles() {
		int radius = 30;
		holes.add(new Hole(0, x, y, radius, x + whiteBill.radius * 5 * 0, y + border + height + 20));
		holes.add(new Hole(1, x + width / 2, y, radius, x + whiteBill.radius * 5 * 1, y + border + height + 20));
		holes.add(new Hole(2, x + width, y, radius, x + whiteBill.radius * 5 * 2, y + border + height + 20));
		holes.add(new Hole(3, x, y + height, radius, x + whiteBill.radius * 5 * 3, y + border + height + 20));
		holes.add(
				new Hole(4, x + width / 2, y + height, radius, x + whiteBill.radius * 5 * 4, y + border + height + 20));
		holes.add(new Hole(5, x + width, y + height, radius, x + whiteBill.radius * 5 * 5, y + border + height + 20));
	}

	protected void runBills(ShapeRenderer shapeRenderer) {
		whiteBill.move(rubbing);
		whiteBill.draw(shapeRenderer);
		for (int i = 0; i < bills.size(); i++) {
			bills.get(i).move(rubbing);
			bills.get(i).draw(shapeRenderer);
		}
		whiteBill.events(shapeRenderer);
	}

	private void checkHoles(ShapeRenderer shapeRenderer) {
		for (int i = 0; i < holes.size(); i++) {
			// white bill
			if (holes.get(i).isCaughtBill(whiteBill)) {
				whiteBill.x = x + width / 2;
				whiteBill.y = y + height / 2;
				whiteBill.v_x = 0;
				whiteBill.v_y = 0;
			}
			// other bills
			for (int j = 0; j < bills.size(); j++) {
				if (holes.get(i).isCaughtBill(bills.get(j))) {
					holes.get(i).caughtBill(bills.get(j));
					bills.remove(bills.lastIndexOf(bills));
				}
			}
			holes.get(i).draw(shapeRenderer);
		}
	}

	protected void collisions() {
		// count bills collisions with 4 threads
		ThreadCollisions[] thread = new ThreadCollisions[2];
		int len = bills.size();
		for (int i = 0; i < 2; i++) {
			thread[i] = new ThreadCollisions(bills, (int) (i * len) / 2, (int) ((i + 1) * len / 2), whiteBill,
					lossVehicle, rubbing, x, y, width, height, hitSound);
			thread[i].start();
		}
		for (int i = 0; i < 2; i++) {
			try {
				thread[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		// collisions whiteBill with table border
		if (whiteBill.x - whiteBill.radius <= x || whiteBill.x + whiteBill.radius >= x + width) {
			hitSound.play();
			whiteBill.billHitTable('X', x, x + width);
			whiteBill.lossVehicleX(lossVehicle);
		}
		if (whiteBill.y - whiteBill.radius <= y || whiteBill.y + whiteBill.radius >= y + height) {
			hitSound.play();
			whiteBill.billHitTable('Y', y, y + height);
			whiteBill.lossVehicleY(lossVehicle);
		}
	}

	public void draw(ShapeRenderer shapeRenderer) {
		shapeRenderer.begin(ShapeType.Filled);
		// border
		shapeRenderer.rect(x - border, y - border, width + 2 * border, height + 2 * border, Color.BROWN, Color.BROWN,
				Color.BROWN, Color.BROWN);
		// table
		shapeRenderer.rect(x, y, width, height, Color.GREEN, Color.FOREST, Color.FOREST, Color.FOREST);
		shapeRenderer.end();
		// holes
		checkHoles(shapeRenderer);
		// bills
		runBills(shapeRenderer);
		// collisions
		collisions();
	}

	public void drawNumbers(SpriteBatch batch, BitmapFont font, ShapeRenderer shapeRenderer) {
		for (int i = 0; i < bills.size(); i++) {
			bills.get(i).drawNumber(batch, font, shapeRenderer);
		}
		for (int i = 0; i < holes.size(); i++) {
			holes.get(i).drawNumber(batch, font, shapeRenderer);
		}
	}
}
