package com.mygdx.bilard;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class WhiteBill extends Bill {
	private static final long serialVersionUID = 1L;
	private int arc = 0;
	private float F_x = 0F, F_y = 0F; // vectors from stick to bill
	private float force = 0F;
	private boolean isReady = false;
	protected int useCounter = 0;
	private boolean canMove = true;

	public WhiteBill(int id, int x, int y, Color color, boolean isHalf) {
		super(id, x, y, color, isHalf);
		this.id = id;
		this.x = x;
		this.y = y;
		this.color = color;
		this.isHalf = false;
	}

	private boolean isHovered() {
		if (this.contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())) {
			return true;
		} else
			return false;
	}

	private void showCircle(ShapeRenderer shapeRenderer, int arc) {
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.arc(x, y, radius + 30, 90, arc);
		shapeRenderer.end();
	}

	private void showStick(ShapeRenderer shapeRenderer) {
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.rectLine(x - force * F_x, y - force * F_y, Gdx.input.getX() - force * F_x,
				Gdx.graphics.getHeight() - Gdx.input.getY() - force * F_y, 15, Color.BROWN, Color.ORANGE);
		shapeRenderer.end();
	}

	public void countVectors() {
		F_x = x - Gdx.input.getX();
		F_y = y - (Gdx.graphics.getHeight() - Gdx.input.getY());
	}

	private void stickHitBill() {
		float temp_force = force;
		while (force * force * (F_x * F_x + F_y * F_y) > radius * radius)
			force -= 0.01;
		
		v_x += temp_force * F_x / 15;
		v_y += temp_force * F_y / 15;
		arc = 0;
	}
	
	public void disable() {
		canMove = false;
	}

	public void events(ShapeRenderer shapeRenderer) {
		if (isHovered()) {
			showCircle(shapeRenderer, arc);
			if (arc <= 360 - 4)
				arc += 4;
		} else {
			if (arc < 360)
				arc = 0;
		}

		if (arc >= 360) {
			countVectors();
			showStick(shapeRenderer);
			if (Gdx.input.isTouched() && canMove) {
				isReady = true;
				if (force < 1)
					force += 0.01;
			} else {
				if (isReady) {
					useCounter++;
					stickHitBill();
					isReady = false;
				}
			}
		}
	}

}
