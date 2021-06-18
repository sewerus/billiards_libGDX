package com.mygdx.bilard;

import java.util.ArrayList;

import com.badlogic.gdx.audio.Sound;

public class ThreadCollisions extends Thread {
	private int start, stop;
	private ArrayList<Bill> bills = new ArrayList<Bill>();
	private WhiteBill whiteBill;
	private float lossVehicle;
	private float rubbing;
	private float x;
	private float y;
	private float width;
	private float height;
	private Sound hitSound;

	public ThreadCollisions(ArrayList<Bill> bills, int start, int stop, WhiteBill whiteBill, float lossVehicle, float rubbing, float x, float y, float width, float height, Sound hitSound) {
		this.start = start;
		this.stop = stop;
		this.bills = bills;
		this.whiteBill = whiteBill;
		this.lossVehicle = lossVehicle;
		this.rubbing = rubbing;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.hitSound = hitSound;
	}

	@Override
	public void run() {
		for (int i = start; i < stop; i++) {
			// white bill
			if (bills.get(i).overlaps(whiteBill)) {
				hitSound.play();
				whiteBill.billHitBill(bills.get(i));
				bills.get(i).lossVehicle(lossVehicle);
				whiteBill.lossVehicle(lossVehicle);
				bills.get(i).move(rubbing);
				whiteBill.move(rubbing);
			}

			// collisions with bills
			for (int j = 0; j < bills.size(); j++) {
				if (i != j) {
					if (bills.get(i).overlaps(bills.get(j))) {
						hitSound.play();
						bills.get(i).billHitBill(bills.get(j));
						bills.get(i).lossVehicle(lossVehicle);
						bills.get(j).lossVehicle(lossVehicle);
						bills.get(i).move(rubbing);
						bills.get(j).move(rubbing);
					}
				}
			}
			// collisions with table border
			if (bills.get(i).x - bills.get(i).radius <= x || bills.get(i).x + bills.get(i).radius >= x + width) {
				hitSound.play();
				bills.get(i).billHitTable('X', x, x + width);
				bills.get(i).lossVehicleX(lossVehicle);
			}
			if (bills.get(i).y - bills.get(i).radius <= y || bills.get(i).y + bills.get(i).radius >= y + height) {
				hitSound.play();
				bills.get(i).billHitTable('Y', y, y + height);
				bills.get(i).lossVehicleY(lossVehicle);
			}
		}
	}
}
