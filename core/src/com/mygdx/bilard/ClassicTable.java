package com.mygdx.bilard;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class ClassicTable extends Table {
	private static final long serialVersionUID = 1L;
	public boolean isNowFirstPlayer = true;
	public int firstPlayerScore = 0;
	public int secondPlayerScore = 0;
	public int firstPlayerHited = 0;
	public int secondPlayerHited = 0;
	public int firstPlayerSumHited = 0;
	public int secondPlayerSumHited = 0;
	public int firstPlayerMaxHited = 0;
	public int secondPlayerMaxHited = 0;
	public boolean isGameOver = false;
	protected int tourCounter = 0;

	public ClassicTable(int x, int y, int width, int height, int border, float rubbing, float lossVehicle,
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

	private void checkHoles(ShapeRenderer shapeRenderer) {
		if (tourCounter != whiteBill.useCounter) { // next tour
			isNowFirstPlayer = !isNowFirstPlayer;
			tourCounter = whiteBill.useCounter;
			
			if(firstPlayerHited > firstPlayerMaxHited)
				firstPlayerMaxHited = firstPlayerHited;
			if(secondPlayerHited > secondPlayerMaxHited)
				secondPlayerMaxHited = secondPlayerHited;
			
			firstPlayerSumHited+=firstPlayerHited;
			firstPlayerHited = 0;
			secondPlayerSumHited += secondPlayerHited;
			secondPlayerHited = 0;
		}

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

					// count scores and check rules
					if (bills.size() == 1) // last one bill
						isGameOver = true;
					if (bills.get(j).id+1 == 8) { // 8 was holed
						System.out.println(bills.get(j).id);
						isGameOver = true;
						if (isNowFirstPlayer) {
							firstPlayerScore = 0;
						} else {
							secondPlayerScore = 0;
						}
					}
					if(isGameOver)
						whiteBill.disable();
					if (isNowFirstPlayer) { // other was holed
						firstPlayerHited++;
						if (bills.get(j).isHalf)
							firstPlayerScore++;
						else {
							secondPlayerScore++;
							isNowFirstPlayer = false;
						}
					} else {
						secondPlayerHited++;
						if (bills.get(j).isHalf)
							secondPlayerScore++;
						else {
							firstPlayerScore++;
							isNowFirstPlayer = true;
						}
					}
					bills.remove(j);
				}
			}
			holes.get(i).draw(shapeRenderer);
		}
	}
}
