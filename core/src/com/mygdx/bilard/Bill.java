package com.mygdx.bilard;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Circle;

public class Bill extends Circle {
	private static final long serialVersionUID = 1L;
	public int id;
	public float v_x; // velocity
	public float v_y;
	private float m; // mass
	public Color color;
	public boolean isHalf;

	public Bill(int id, int x, int y, Color color, boolean isHalf) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.color = color;
		this.isHalf = isHalf;
		v_x = 0.0001F;
		v_y = 0.0001F;
		radius = 20;
		m = 10;
	}

	private void repareTranslation(char direction, float min, float max) {
		if (direction == 'X') {
			if (x - radius < min) {
				x += min - x + radius;
				y -= v_y * (min - x + radius) / v_x;
			}
			if (x + radius > max) {
				x -= x + radius - max;
				y -= v_y * (x + radius - max) / v_x;
			}
		}
		if (direction == 'Y') {
			if (y - radius < min) {
				y += min - y + radius;
				x -= v_x * (min - y + radius) / v_y;
			}
			if (y + radius > max) {
				y -= y + radius - max;
				x -= v_x * (y + radius - max) / v_x;
			}
		}
	}

	public void billHitTable(char direction, float min, float max) {
		if (direction == 'X') {
			v_x = -v_x;
			repareTranslation('X', min, max);
		}
		if (direction == 'Y') {
			v_y = -v_y;
			repareTranslation('Y', min, max);
		}
	}

	private void repareTranslationOverBill(Bill hited) {
		float overDistance = radius + hited.radius
				- (float) Math.sqrt((hited.x - x) * (hited.x - x) + (hited.y - y) * (hited.y - y));
		if (x < hited.x)
			repareTranslation('X', 0, x + radius - overDistance);
		else
			repareTranslation('X', x - radius + overDistance, Gdx.graphics.getWidth());
		if (y < hited.y)
			repareTranslation('Y', 0, y + radius - overDistance);
		else
			repareTranslation('Y', y - radius + overDistance, Gdx.graphics.getHeight());
	}

	public void billHitBill(Bill hited) {
		repareTranslationOverBill(hited);
		float M_1 = m / (m + hited.m);
		float M_2 = hited.m / (m + hited.m);
		float XY = (float) Math.sqrt((hited.x - x) * (hited.x - x) + (hited.y - y) * (hited.y - y));
		float v_1n = (v_x * (hited.x - x) + v_y * (hited.y - y)) / XY;
		float v_2n = (hited.v_x * (hited.x - x) + hited.v_y * (hited.y - y)) / XY;
		float v_1t = (-v_x * (hited.y - y) + v_y * (hited.x - x)) / XY;
		float v_2t = (-hited.v_x * (hited.y - y) + hited.v_y * (hited.x - x)) / XY;
		float v_1N = (M_1 - M_2) * v_1n + 2 * M_2 * v_2n;
		float v_2N = -(M_1 - M_2) * v_2n + 2 * M_2 * v_1n;
		v_x = (v_1N * (hited.x - x) - v_1t * (hited.y - y)) / (float) XY;
		v_y = (v_1N * (hited.y - y) - v_1t * (hited.x - x)) / (float) XY;
		hited.v_x = (v_2N * (hited.x - x) - v_2t * (hited.y - y)) / (float) XY;
		hited.v_y = (v_2N * (hited.y - y) - v_2t * (hited.x - x)) / (float) XY;
	}

	public void lossVehicleX(float loss) {
		v_x *= loss;
	}

	public void lossVehicleY(float loss) {
		v_y *= loss;
	}

	public void lossVehicle(float loss) {
		v_x *= loss;
		v_y *= loss;
	}

	public void draw(ShapeRenderer shapeRenderer) {
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(color);
		shapeRenderer.circle(x, y, radius);
		shapeRenderer.end();
	}

	public void drawNumber(SpriteBatch batch, BitmapFont font, ShapeRenderer shapeRenderer) {
		float height = 8;
		if (isHalf) {
			shapeRenderer.begin(ShapeType.Filled);
			for (; height > 0; height--)
				shapeRenderer.rect(x - (float) Math.sqrt((float) (radius * radius - height * height)), y - height,
						(float) Math.sqrt((float) (radius * radius - height * height)) * 2, height * 2, Color.WHITE,
						Color.WHITE, Color.WHITE, Color.WHITE);

			shapeRenderer.end();
		}
		else {
			shapeRenderer.begin(ShapeType.Filled);
			shapeRenderer.setColor(Color.WHITE);
			shapeRenderer.circle(x, y, 11);
			shapeRenderer.end();
		}
			
		batch.begin();
		font.setColor(Color.BLACK);
		if (id + 1 < 10)
			font.draw(batch, Integer.toString(id + 1), x - (float) (font.getSpaceWidth() * 1.5),
					y + font.getXHeight() / 2 + 2);
		else
			font.draw(batch, Integer.toString(id + 1), x - (float) (font.getSpaceWidth() * 1.5) - 5,
					y + font.getXHeight() / 2 + 2);
		batch.end();
	}

	public void move(float rubbing) {
		x += v_x;
		y += v_y;

		// rubbing
		if (v_x * v_x > rubbing * rubbing) {
			if (v_x > rubbing)
				v_x -= rubbing;
			else if (v_x < -rubbing)
				v_x += rubbing;
			else
				v_x = 0F;
		}
		if (v_y * v_y > rubbing * rubbing) {
			if (v_y > rubbing)
				v_y -= rubbing;
			else if (v_y < -rubbing)
				v_y += rubbing;
			else
				v_y = 0F;
		}
	}

	// overlaps
	public boolean isHit(Bill bill) {
		if ((x - bill.x) * (x - bill.x) + (y - bill.y) * (y - bill.y) == (radius + bill.radius)
				* (radius + bill.radius))
			return true;
		else
			return false;
	}
}
