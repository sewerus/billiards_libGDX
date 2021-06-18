package com.mygdx.bilard;

import com.badlogic.gdx.audio.Sound;

public class MagneticTable extends ClassicTable {
	private static final long serialVersionUID = 1L;

	public MagneticTable(int x, int y, int width, int height, int border, float rubbing, float lossVehicle,
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
	protected void collisions() {
		//count bills collisions with 1 threads
		ThreadMagneticCollisions[] thread = new ThreadMagneticCollisions[1];
		//int len = bills.size();
        //for (int i = 0; i < 2; i++) {
        	thread[0] = new ThreadMagneticCollisions(bills, 0, bills.size(), whiteBill, lossVehicle, rubbing, x, y, width, height, hitSound);
        	thread[0].start();
//        	thread[1] = new ThreadMagneticCollisions(bills, 4, 8, whiteBill, lossVehicle, rubbing, x, y, width, height, hitSound);
//        	thread[1].start();
//        	thread[2] = new ThreadMagneticCollisions(bills, 8, 12, whiteBill, lossVehicle, rubbing, x, y, width, height, hitSound);
//        	thread[2].start();
//        	thread[3] = new ThreadMagneticCollisions(bills, 12, 15, whiteBill, lossVehicle, rubbing, x, y, width, height, hitSound);
//        	thread[3].start();
        //}
        for (int i = 0; i < 1; i++) {
        	try {
				thread[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        }
		// magnetic source
//		for (int i = 0; i < bills.size(); i++) {
//			for (int j = 0; j < i; j++) {
//				//if (bills.get(i).v_x > 0.004)
//					bills.get(i).v_x += 0.05
//							/ ((bills.get(i).x - bills.get(j).x) * (bills.get(i).x - bills.get(j).x)
//									+ (bills.get(i).y - bills.get(j).y) * (bills.get(i).y - bills.get(j).y))
//							* (bills.get(i).x - bills.get(j).x);
//
//				//if (bills.get(i).v_y > 0.04)
//					bills.get(i).v_y += 0.05
//							/ ((bills.get(i).x - bills.get(j).x) * (bills.get(i).x - bills.get(j).x)
//									+ (bills.get(i).y - bills.get(j).y) * (bills.get(i).y - bills.get(j).y))
//							* (bills.get(i).y - bills.get(j).y);
//			}
//
//			//if (bills.get(i).v_x > 0.04)
//				bills.get(i).v_x += 0.005
//						/ Math.sqrt((bills.get(i).x - whiteBill.x) * (bills.get(i).x - whiteBill.x)
//								+ (bills.get(i).y - whiteBill.y) * (bills.get(i).y - whiteBill.y))
//						* (bills.get(i).x - whiteBill.x);
//
//			//if (bills.get(i).v_y > 0.04)
//				bills.get(i).v_y += 0.005
//						/ Math.sqrt((bills.get(i).x - whiteBill.x) * (bills.get(i).x - whiteBill.x)
//								+ (bills.get(i).y - whiteBill.y) * (bills.get(i).y - whiteBill.y))
//						* (bills.get(i).y - whiteBill.y);
//		}

		for (int i = 0; i < bills.size(); i++) {
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
			for (int j = 0; j < i; j++) {
				if (bills.get(i).overlaps(bills.get(j))) {
					hitSound.play();
					bills.get(i).billHitBill(bills.get(j));
					bills.get(i).lossVehicle(lossVehicle);
					bills.get(j).lossVehicle(lossVehicle);
					bills.get(i).move(rubbing);
					bills.get(j).move(rubbing);
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
}
