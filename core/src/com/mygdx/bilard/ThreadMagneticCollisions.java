package com.mygdx.bilard;

import java.util.ArrayList;

import com.badlogic.gdx.audio.Sound;

public class ThreadMagneticCollisions extends Thread {
	private ArrayList<Bill> bills = new ArrayList<Bill>();
	public ThreadMagneticCollisions(ArrayList<Bill> bills, int start, int stop, WhiteBill whiteBill, float lossVehicle,
			float rubbing, float x, float y, float width, float height, Sound hitSound) {
		this.bills = bills;
	}

	@Override
	public void run() {
		// magnetic source
		for (int i = 0; i < bills.size(); i++) {
			for (int j = 0; j < i; j++) {
					bills.get(i).v_x -= 0.001
							/ Math.sqrt((bills.get(i).x - bills.get(j).x) * (bills.get(i).x - bills.get(j).x)
									+ (bills.get(i).y - bills.get(j).y) * (bills.get(i).y - bills.get(j).y))
							* (bills.get(i).x - bills.get(j).x);

					bills.get(i).v_y -= 0.001
							/ Math.sqrt((bills.get(i).x - bills.get(j).x) * (bills.get(i).x - bills.get(j).x)
									+ (bills.get(i).y - bills.get(j).y) * (bills.get(i).y - bills.get(j).y))
							* (bills.get(i).y - bills.get(j).y);
			}
		}
	}
}
