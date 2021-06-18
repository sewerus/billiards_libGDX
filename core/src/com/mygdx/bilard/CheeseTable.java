package com.mygdx.bilard;

import java.util.Random;

import com.badlogic.gdx.audio.Sound;

public class CheeseTable extends ClassicTable {
	private static final long serialVersionUID = 1L;

	public CheeseTable(int x, int y, int width, int height, int border, float rubbing, float lossVehicle,
			Sound hitSound) {
		super(x, y, width, height, border, rubbing, lossVehicle, hitSound);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.border = border;
		this.rubbing = rubbing;
		this.lossVehicle = lossVehicle;
		this.hitSound = hitSound;
		prepareExtraHoles();
	}

	protected void prepareExtraHoles() {
		int radius = 40;
		Random generator = new Random();
		for(int i = 6; i < 10; i++)
			holes.add(new Hole(i, x + width/10*generator.nextInt(11), y + height/10*generator.nextInt(11), radius, x + whiteBill.radius * 5 * i, y + border + height + 20));
	}

}
