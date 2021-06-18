package com.mygdx.bilard;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Timer;

public class User {
	private int id;
	public String name;
	public int level;
	public int holedBills;
	public int maxHoledBills;
	public int winCount;
	public int loseCount;

	public User() {
		StringListener textListener = new StringListener();
		Timer timer = new Timer();

		Gdx.input.getTextInput(textListener, "Podaj imie", "", "");
		while (textListener.text == null) {
			timer.delay(5);
		}
		String name = textListener.text;
		textListener.text = null;

		this.id = howManyAreUsers() + 1;
		this.name = name;
		this.level = 0;
		this.holedBills = 0;
		this.maxHoledBills = 0;
		this.winCount = 0;
		this.loseCount = 0;
	}

	public User(int id, String name, int level, int holedBills, int maxHoledBills, int winCount, int loseCount) {
		this.id = id;
		this.name = name;
		this.level = level;
		this.holedBills = holedBills;
		this.maxHoledBills = maxHoledBills;
		this.winCount = winCount;
		this.loseCount = loseCount;
	}

	public int howManyAreUsers() {
		// id count
		int count = 0;
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader("database.txt"));
			while (reader.readLine() != null) {
				count++;
			}
		} catch (Exception e) {
			System.out.println("Can't read file database.txt");
		}
		return count;
	}

	public void saveNewUser() {
		String line;

		if (howManyAreUsers() > 0)
			line = System.getProperty("line.separator") + id + ";" + name + ";" + level + ";" + holedBills + ";"
					+ maxHoledBills + ";" + winCount + ";" + loseCount + ";";
		else
			line = id + ";" + name + ";" + level + ";" + holedBills + ";" + maxHoledBills + ";" + winCount + ";"
					+ loseCount + ";";
		try {
			Files.write(Paths.get("database.txt"), line.getBytes(), StandardOpenOption.APPEND);
		} catch (IOException e) {
			System.out.println("Not saved in database!");
		}
	}

	public void updateUser(ArrayList<User> usersList, int sum, int max, boolean isWinner) {
		for (int i = 0; i < usersList.size(); i++) {
			if (usersList.get(i).id == id) {
				usersList.get(i).holedBills += sum;
				if (usersList.get(i).maxHoledBills < max)
					usersList.get(i).maxHoledBills = max;
				if (isWinner)
					usersList.get(i).winCount++;
				else
					usersList.get(i).loseCount++;
				usersList.get(i).level = (usersList.get(i).winCount - usersList.get(i).loseCount
						+ usersList.get(i).maxHoledBills + usersList.get(i).holedBills) / 10 + 1;
				break;
			}
		}

		// update in file
		String data = "";
		for (int i = 0; i < usersList.size(); i++) {
			data += usersList.get(i).id + ";" + usersList.get(i).name + ";" + usersList.get(i).level + ";"
					+ usersList.get(i).holedBills + ";" + usersList.get(i).maxHoledBills + ";"
					+ usersList.get(i).winCount + ";" + usersList.get(i).loseCount + ";"
					+ System.getProperty("line.separator");
			System.out.println(data);
		}
		try {
			Files.write(Paths.get("database.txt"), data.substring(0, data.length() - 1).getBytes(),
					StandardOpenOption.WRITE);
		} catch (IOException e) {
			System.out.println("Not saved in database!");
		}
	}
}
