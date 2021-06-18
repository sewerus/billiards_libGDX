package com.mygdx.bilard;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class Bilard extends ApplicationAdapter {
	private float timer = 0F;
	private int operation = 0;
	// 0 - menu
	// 1 - training
	// 2 - whichUser
	// 3 - magnetic
	// 4 - cheese
	// 5 - statistics
	// 6 - credits
	// 7 - exit
	// 8 - new user
	// 9 - saved user
	// 10 - classic
	private Texture logo;
	private Texture menuBackground;
	private Texture menuTraining;
	private Texture menuClassic;
	private Texture menuMagnetic;
	private Texture menuCheese;
	private Texture menuStatistics;
	private Texture menuCredits;
	private Texture menuExit;
	private Texture backToMenu;
	private Texture newPlayer;
	private Texture savedPlayer;
	private Texture menuHoverTraining;
	private Texture menuHoverClassic;
	private Texture menuHoverMagnetic;
	private Texture menuHoverCheese;
	private Texture menuHoverStatistics;
	private Texture menuHoverCredits;
	private Texture menuHoverExit;
	private Texture hoverBackToMenu;
	private Texture hoverNewPlayer;
	private Texture hoverSavedPlayer;
	private Texture creditsInfo;
	private Texture playAgain;
	private Texture hoverPlayAgain;
	private Texture firstPlayerButton;
	private Texture secondPlayerButton;
	private SpriteBatch batch;
	private BitmapFont font;
	private ShapeRenderer shapeRenderer;
	private Sound hitSound;
	private Table trainingTable;
	private ClassicTable classicTable;
	private MagneticTable magneticTable;
	private CheeseTable cheeseTable;
	private User firstPlayer;
	private User secondPlayer;
	private boolean isFirstPlayerLoaded = false;
	private int whichGame = 0;
	// 0 - classic
	// 1 - magnetic
	// 2 - cheese
	private ArrayList<User> usersList;
	private ArrayList<Music> musicList;
	private Random generator;
	private Integer playing;
	private boolean areUsersUpdated;

	public void create() {
		logo = new Texture("logo.png");
		menuBackground = new Texture("menu.jpg");
		menuTraining = new Texture("Menu/training.png");
		menuClassic = new Texture("Menu/classic.png");
		menuMagnetic = new Texture("Menu/magnetic.png");
		menuCheese = new Texture("Menu/cheese.png");
		menuStatistics = new Texture("Menu/statistics.png");
		menuCredits = new Texture("Menu/credits.png");
		menuExit = new Texture("Menu/exit.png");
		backToMenu = new Texture("Menu/backtomenu.png");
		newPlayer = new Texture("Menu/newplayer.png");
		savedPlayer = new Texture("Menu/savedplayer.png");
		menuHoverTraining = new Texture("MenuHover/training.png");
		menuHoverClassic = new Texture("MenuHover/classic.png");
		menuHoverMagnetic = new Texture("MenuHover/magnetic.png");
		menuHoverCheese = new Texture("MenuHover/cheese.png");
		menuHoverStatistics = new Texture("MenuHover/statistics.png");
		menuHoverCredits = new Texture("MenuHover/credits.png");
		menuHoverExit = new Texture("MenuHover/exit.png");
		hoverBackToMenu = new Texture("MenuHover/backtomenu.png");
		hoverNewPlayer = new Texture("MenuHover/newplayer.png");
		hoverSavedPlayer = new Texture("MenuHover/savedplayer.png");
		creditsInfo = new Texture("credits_info.png");
		playAgain = new Texture("Menu/playagain.png");
		hoverPlayAgain = new Texture("MenuHover/playagain.png");
		firstPlayerButton = new Texture("firstPlayer.png");
		secondPlayerButton = new Texture("secondPlayer.png");
		font = new BitmapFont();
		hitSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/bills1.wav"));
		shapeRenderer = new ShapeRenderer();
		batch = new SpriteBatch();
		trainingTable = new Table(50, 50, 1100, 400, 40, 0.003F, 0.7F, hitSound);
		classicTable = new ClassicTable(50, 50, 1100, 400, 40, 0.003F, 0.7F, hitSound);
		magneticTable = new MagneticTable(50, 50, 1100, 400, 40, 0.003F, 0.7F, hitSound);
		cheeseTable = new CheeseTable(50, 50, 1100, 400, 40, 0.003F, 0.7F, hitSound);
		musicList = new ArrayList<Music>();
		musicList.add(Gdx.audio.newMusic(Gdx.files.internal("Music/amelia.mp3")));
		musicList.add(Gdx.audio.newMusic(Gdx.files.internal("Music/experience.mp3")));
		musicList.add(Gdx.audio.newMusic(Gdx.files.internal("Music/gosolo.mp3")));
		musicList.add(Gdx.audio.newMusic(Gdx.files.internal("Music/interstellar.mp3")));
		musicList.add(Gdx.audio.newMusic(Gdx.files.internal("Music/madworld.mp3")));
		musicList.add(Gdx.audio.newMusic(Gdx.files.internal("Music/unomattina.mp3")));
		musicList.add(Gdx.audio.newMusic(Gdx.files.internal("Music/whereismy.mp3")));
		generator = new Random();
		playing = new Integer(generator.nextInt(7));
		areUsersUpdated = false;

		usersList = new ArrayList<User>();
		try {
			loadUsersFromDatabase();
		} catch (IOException e) {
			System.out.println("Nie uda³o siê za³adowaæ bazy danych!");
			e.printStackTrace();
		}
	}

	public void update() {
		timer += Gdx.graphics.getDeltaTime();
	}

	public void render() {
		update();
		Gdx.gl.glClearColor((float) 205 / 255, (float) 133 / 255, (float) 63 / 255, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if (timer < 3) {
			showLogo();
		} else {
			if (operation == 0) {
				isFirstPlayerLoaded = false;
				firstPlayer = null;
				secondPlayer = null;
				areUsersUpdated = false;
				whichGame = 0;
				showMenu();
			}
			if (operation == 1)
				training();
			if (operation == 2)
				whichUser();
			if (operation == 3)
				magnetic();
			if (operation == 4)
				cheese();
			if (operation == 5)
				statistics();
			if (operation == 6)
				credits();
			if (operation == 7)
				Gdx.app.exit();
			if (operation == 8)
				newPlayer();
			if (operation == 9)
				savedPlayer();
			if (operation == 10)
				classic();
		}
	}

	public void dispose() {
		menuBackground.dispose();
		menuTraining.dispose();
		menuClassic.dispose();
		menuMagnetic.dispose();
		menuCheese.dispose();
		menuStatistics.dispose();
		menuCredits.dispose();
		menuExit.dispose();
		backToMenu.dispose();
		newPlayer.dispose();
		savedPlayer.dispose();
		menuHoverTraining.dispose();
		menuHoverClassic.dispose();
		menuHoverMagnetic.dispose();
		menuHoverCheese.dispose();
		menuHoverStatistics.dispose();
		menuHoverCredits.dispose();
		menuHoverExit.dispose();
		hoverBackToMenu.dispose();
		hoverNewPlayer.dispose();
		hoverSavedPlayer.dispose();
		creditsInfo.dispose();
		playAgain.dispose();
		firstPlayerButton.dispose();
		secondPlayerButton.dispose();
		hoverPlayAgain.dispose();
		hitSound.dispose();
		shapeRenderer.dispose();
		batch.dispose();
		font.dispose();
		logo.dispose();
	}

	public void loadUsersFromDatabase() throws IOException {
		FileReader fileReader = new FileReader("database.txt");
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		String line = bufferedReader.readLine();
		while (line != null) {
			int startLogin = 0;
			int endLogin = 0;

			while (line.charAt(endLogin) != ';') {
				endLogin++;
			}

			// get id
			int id = Integer.parseInt(line.substring(0, endLogin));

			endLogin++;
			startLogin = endLogin;
			while (line.charAt(endLogin) != ';') {
				endLogin++;
			}

			// get login
			String login = line.substring(startLogin, endLogin);

			endLogin++;
			startLogin = endLogin;
			while (line.charAt(endLogin) != ';') {
				endLogin++;
			}

			// get level
			int level = Integer.parseInt(line.substring(startLogin, endLogin));

			endLogin++;
			startLogin = endLogin;
			while (line.charAt(endLogin) != ';') {
				endLogin++;
			}

			// get holedBills
			int holedBills = Integer.parseInt(line.substring(startLogin, endLogin));

			endLogin++;
			startLogin = endLogin;
			while (line.charAt(endLogin) != ';') {
				endLogin++;
			}

			// get maxHoledBills
			int maxHoledBills = Integer.parseInt(line.substring(startLogin, endLogin));

			endLogin++;
			startLogin = endLogin;
			while (line.charAt(endLogin) != ';') {
				endLogin++;
			}

			// get winCount
			int winCount = Integer.parseInt(line.substring(startLogin, endLogin));

			endLogin++;
			startLogin = endLogin;
			while (line.charAt(endLogin) != ';') {
				endLogin++;
			}

			// get loseCount
			int loseCount = Integer.parseInt(line.substring(startLogin, endLogin));

			usersList.add(new User(id, login, level, holedBills, maxHoledBills, winCount, loseCount));

			line = bufferedReader.readLine();
		}

		bufferedReader.close();
	}

	public void showLogo() {
		batch.begin();
		batch.draw(logo, Gdx.graphics.getWidth() / 2 - 800 / 2,
				Gdx.graphics.getHeight() / 2 - logo.getHeight() * 800 / logo.getWidth() / 2, 800,
				logo.getHeight() * 800 / logo.getWidth());
		batch.end();
	}

	public void showMenu() {
		musicList.get(playing).stop();
		playing = generator.nextInt(7);
		musicList.get(playing).play();
		batch.begin();
		batch.draw(menuBackground, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		// training
		if (Gdx.input.getX() > Gdx.graphics.getWidth() / 2 - menuTraining.getWidth() / 2
				&& Gdx.input.getX() < Gdx.graphics.getWidth() / 2 + menuTraining.getWidth() / 2
				&& Gdx.graphics.getHeight() - Gdx.input.getY() > Gdx.graphics.getHeight() / 8 * 7
				&& Gdx.graphics.getHeight() - Gdx.input.getY() < Gdx.graphics.getHeight() / 8 * 7
						+ menuTraining.getHeight()) {
			batch.draw(menuHoverTraining, Gdx.graphics.getWidth() / 2 - menuHoverTraining.getWidth() / 2,
					Gdx.graphics.getHeight() / 8 * 7);
			if (Gdx.input.justTouched())
				operation = 1;
		} else {
			batch.draw(menuTraining, Gdx.graphics.getWidth() / 2 - menuTraining.getWidth() / 2,
					Gdx.graphics.getHeight() / 8 * 7);
		}

		// classic
		if (Gdx.input.getX() > Gdx.graphics.getWidth() / 2 - menuClassic.getWidth() / 2
				&& Gdx.input.getX() < Gdx.graphics.getWidth() / 2 + menuClassic.getWidth() / 2
				&& Gdx.graphics.getHeight() - Gdx.input.getY() > Gdx.graphics.getHeight() / 8 * 6
				&& Gdx.graphics.getHeight() - Gdx.input.getY() < Gdx.graphics.getHeight() / 8 * 6
						+ menuClassic.getHeight()) {
			batch.draw(menuHoverClassic, Gdx.graphics.getWidth() / 2 - menuHoverClassic.getWidth() / 2,
					Gdx.graphics.getHeight() / 8 * 6);
			if (Gdx.input.justTouched()) {
				whichGame = 0;
				operation = 2;
			}
		} else {
			batch.draw(menuClassic, Gdx.graphics.getWidth() / 2 - menuClassic.getWidth() / 2,
					Gdx.graphics.getHeight() / 8 * 6);
		}

		// magnetic
		if (Gdx.input.getX() > Gdx.graphics.getWidth() / 2 - menuMagnetic.getWidth() / 2
				&& Gdx.input.getX() < Gdx.graphics.getWidth() / 2 + menuMagnetic.getWidth() / 2
				&& Gdx.graphics.getHeight() - Gdx.input.getY() > Gdx.graphics.getHeight() / 8 * 5
				&& Gdx.graphics.getHeight() - Gdx.input.getY() < Gdx.graphics.getHeight() / 8 * 5
						+ menuMagnetic.getHeight()) {
			batch.draw(menuHoverMagnetic, Gdx.graphics.getWidth() / 2 - menuHoverMagnetic.getWidth() / 2,
					Gdx.graphics.getHeight() / 8 * 5);
			if (Gdx.input.justTouched()) {
				whichGame = 1;
				operation = 2;
			}
		} else {
			batch.draw(menuMagnetic, Gdx.graphics.getWidth() / 2 - menuMagnetic.getWidth() / 2,
					Gdx.graphics.getHeight() / 8 * 5);
		}

		// cheese
		if (Gdx.input.getX() > Gdx.graphics.getWidth() / 2 - menuCheese.getWidth() / 2
				&& Gdx.input.getX() < Gdx.graphics.getWidth() / 2 + menuCheese.getWidth() / 2
				&& Gdx.graphics.getHeight() - Gdx.input.getY() > Gdx.graphics.getHeight() / 8 * 4
				&& Gdx.graphics.getHeight() - Gdx.input.getY() < Gdx.graphics.getHeight() / 8 * 4
						+ menuCheese.getHeight()) {
			batch.draw(menuHoverCheese, Gdx.graphics.getWidth() / 2 - menuHoverCheese.getWidth() / 2,
					Gdx.graphics.getHeight() / 8 * 4);
			if (Gdx.input.justTouched()) {
				whichGame = 2;
				operation = 2;
			}
		} else {
			batch.draw(menuCheese, Gdx.graphics.getWidth() / 2 - menuCheese.getWidth() / 2,
					Gdx.graphics.getHeight() / 8 * 4);
		}

		// statistics
		if (Gdx.input.getX() > Gdx.graphics.getWidth() / 2 - menuStatistics.getWidth() / 2
				&& Gdx.input.getX() < Gdx.graphics.getWidth() / 2 + menuStatistics.getWidth() / 2
				&& Gdx.graphics.getHeight() - Gdx.input.getY() > Gdx.graphics.getHeight() / 8 * 3
				&& Gdx.graphics.getHeight() - Gdx.input.getY() < Gdx.graphics.getHeight() / 8 * 3
						+ menuStatistics.getHeight()) {
			batch.draw(menuHoverStatistics, Gdx.graphics.getWidth() / 2 - menuHoverStatistics.getWidth() / 2,
					Gdx.graphics.getHeight() / 8 * 3);
			if (Gdx.input.justTouched())
				operation = 5;
		} else {
			batch.draw(menuStatistics, Gdx.graphics.getWidth() / 2 - menuStatistics.getWidth() / 2,
					Gdx.graphics.getHeight() / 8 * 3);
		}

		// credits
		if (Gdx.input.getX() > Gdx.graphics.getWidth() / 2 - menuCredits.getWidth() / 2
				&& Gdx.input.getX() < Gdx.graphics.getWidth() / 2 + menuCredits.getWidth() / 2
				&& Gdx.graphics.getHeight() - Gdx.input.getY() > Gdx.graphics.getHeight() / 8 * 2
				&& Gdx.graphics.getHeight() - Gdx.input.getY() < Gdx.graphics.getHeight() / 8 * 2
						+ menuCredits.getHeight()) {
			batch.draw(menuHoverCredits, Gdx.graphics.getWidth() / 2 - menuHoverCredits.getWidth() / 2,
					Gdx.graphics.getHeight() / 8 * 2);
			if (Gdx.input.justTouched())
				operation = 6;
		} else {
			batch.draw(menuCredits, Gdx.graphics.getWidth() / 2 - menuCredits.getWidth() / 2,
					Gdx.graphics.getHeight() / 8 * 2);
		}

		// exit
		if (Gdx.input.getX() > Gdx.graphics.getWidth() / 2 - menuExit.getWidth() / 2
				&& Gdx.input.getX() < Gdx.graphics.getWidth() / 2 + menuExit.getWidth() / 2
				&& Gdx.graphics.getHeight() - Gdx.input.getY() > Gdx.graphics.getHeight() / 8 * 1
				&& Gdx.graphics.getHeight() - Gdx.input.getY() < Gdx.graphics.getHeight() / 8 * 1
						+ menuExit.getHeight()) {
			batch.draw(menuHoverExit, Gdx.graphics.getWidth() / 2 - menuHoverExit.getWidth() / 2,
					Gdx.graphics.getHeight() / 8 * 1);
			if (Gdx.input.justTouched())
				operation = 7;
		} else {
			batch.draw(menuExit, Gdx.graphics.getWidth() / 2 - menuExit.getWidth() / 2,
					Gdx.graphics.getHeight() / 8 * 1);
		}
		batch.end();
	}

	public void whichUser() {
		batch.begin();
		batch.draw(menuBackground, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		if (!isFirstPlayerLoaded) {
			// firstPlayer button
			batch.draw(firstPlayerButton, Gdx.graphics.getWidth() / 2 - firstPlayerButton.getWidth() / 2,
					Gdx.graphics.getHeight() / 8 * 6);
		} else {
			// secondPlayer button
			batch.draw(secondPlayerButton, Gdx.graphics.getWidth() / 2 - secondPlayerButton.getWidth() / 2,
					Gdx.graphics.getHeight() / 8 * 6);
		}

		// newPlayer button
		if (Gdx.input.getX() > Gdx.graphics.getWidth() / 2 - newPlayer.getWidth() / 2
				&& Gdx.input.getX() < Gdx.graphics.getWidth() / 2 + newPlayer.getWidth() / 2
				&& Gdx.graphics.getHeight() - Gdx.input.getY() > Gdx.graphics.getHeight() / 8 * 3
				&& Gdx.graphics.getHeight() - Gdx.input.getY() < Gdx.graphics.getHeight() / 8 * 3
						+ newPlayer.getHeight()) {
			batch.draw(hoverNewPlayer, Gdx.graphics.getWidth() / 2 - hoverNewPlayer.getWidth() / 2,
					Gdx.graphics.getHeight() / 8 * 3);
			if (Gdx.input.justTouched())
				operation = 8;
		} else {
			batch.draw(newPlayer, Gdx.graphics.getWidth() / 2 - newPlayer.getWidth() / 2,
					Gdx.graphics.getHeight() / 8 * 3);
		}

		// savedPlayer button
		if (Gdx.input.getX() > Gdx.graphics.getWidth() / 2 - savedPlayer.getWidth() / 2
				&& Gdx.input.getX() < Gdx.graphics.getWidth() / 2 + savedPlayer.getWidth() / 2
				&& Gdx.graphics.getHeight() - Gdx.input.getY() > Gdx.graphics.getHeight() / 8 * 2
				&& Gdx.graphics.getHeight() - Gdx.input.getY() < Gdx.graphics.getHeight() / 8 * 2
						+ savedPlayer.getHeight()) {
			batch.draw(hoverSavedPlayer, Gdx.graphics.getWidth() / 2 - hoverSavedPlayer.getWidth() / 2,
					Gdx.graphics.getHeight() / 8 * 2);
			if (Gdx.input.justTouched())
				operation = 9;
		} else {
			batch.draw(savedPlayer, Gdx.graphics.getWidth() / 2 - savedPlayer.getWidth() / 2,
					Gdx.graphics.getHeight() / 8 * 2);
		}

		// backToMenu button
		if (Gdx.input.getX() > Gdx.graphics.getWidth() / 2 - backToMenu.getWidth() / 2
				&& Gdx.input.getX() < Gdx.graphics.getWidth() / 2 + backToMenu.getWidth() / 2
				&& Gdx.graphics.getHeight() - Gdx.input.getY() > Gdx.graphics.getHeight() / 8 * 1
				&& Gdx.graphics.getHeight() - Gdx.input.getY() < Gdx.graphics.getHeight() / 8 * 1
						+ backToMenu.getHeight()) {
			batch.draw(hoverBackToMenu, Gdx.graphics.getWidth() / 2 - hoverBackToMenu.getWidth() / 2,
					Gdx.graphics.getHeight() / 8 * 1);
			if (Gdx.input.justTouched())
				operation = 0;
		} else {
			batch.draw(backToMenu, Gdx.graphics.getWidth() / 2 - backToMenu.getWidth() / 2,
					Gdx.graphics.getHeight() / 8 * 1);
		}
		batch.end();
	}

	public void newPlayer() {
		batch.begin();
		batch.draw(menuBackground, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.draw(newPlayer, Gdx.graphics.getWidth() / 2 - newPlayer.getWidth() / 2, Gdx.graphics.getHeight() / 8 * 5);
		if (!isFirstPlayerLoaded) {
			firstPlayer = new User();
			firstPlayer.saveNewUser();
			usersList.add(firstPlayer);
			isFirstPlayerLoaded = true;
			operation = 2;
		} else {
			secondPlayer = new User();
			secondPlayer.saveNewUser();
			usersList.add(secondPlayer);
			// play the game now
			if (whichGame == 0)
				operation = 10; // classic
			else if (whichGame == 1)
				operation = 3; // magnetic
			else
				operation = 4; // cheese
		}

		// backToMenu button
		if (Gdx.input.getX() > Gdx.graphics.getWidth() / 2 - backToMenu.getWidth() / 2
				&& Gdx.input.getX() < Gdx.graphics.getWidth() / 2 + backToMenu.getWidth() / 2
				&& Gdx.graphics.getHeight() - Gdx.input.getY() > Gdx.graphics.getHeight() / 8 * 3
				&& Gdx.graphics.getHeight() - Gdx.input.getY() < Gdx.graphics.getHeight() / 8 * 3
						+ backToMenu.getHeight()) {
			batch.draw(hoverBackToMenu, Gdx.graphics.getWidth() / 2 - hoverBackToMenu.getWidth() / 2,
					Gdx.graphics.getHeight() / 8 * 3);
			if (Gdx.input.justTouched())
				operation = 0;
		} else {
			batch.draw(backToMenu, Gdx.graphics.getWidth() / 2 - backToMenu.getWidth() / 2,
					Gdx.graphics.getHeight() / 8 * 3);
		}
		batch.end();
	}

	public void savedPlayer() {
		batch.begin();
		batch.draw(menuBackground, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.draw(savedPlayer, Gdx.graphics.getWidth() / 2 - savedPlayer.getWidth() / 2,
				Gdx.graphics.getHeight() / 8 * 5);
		batch.end();

		// users list
		if (!isFirstPlayerLoaded) {
			shapeRenderer.begin(ShapeType.Filled);
			shapeRenderer.rect(Gdx.graphics.getWidth() / 2 - 300, 250, 600, 400, Color.WHITE, Color.WHITE, Color.WHITE,
					Color.WHITE);
			shapeRenderer.end();

			batch.begin();
			// statistics
			font.setColor(Color.BLACK);
			font.draw(batch, "Login:", Gdx.graphics.getWidth() / 2 - 149, Gdx.graphics.getHeight() - 50);
			if (!isFirstPlayerLoaded) {
				for (int i = 0; i < usersList.size(); i++) {
					if (Gdx.input.getX() > Gdx.graphics.getWidth() / 2 - 300
							&& Gdx.input.getX() < Gdx.graphics.getWidth() / 2 + 300
							&& Gdx.graphics.getHeight() - Gdx.input.getY() > Gdx.graphics.getHeight() - 50
									- (i + 2) * font.getLineHeight()
							&& Gdx.graphics.getHeight() - Gdx.input.getY() < Gdx.graphics.getHeight() - 50
									- (i + 1) * font.getLineHeight()) {
						font.setColor(Color.RED);
						if (Gdx.input.isTouched()) {
							firstPlayer = usersList.get(i);
							isFirstPlayerLoaded = true;
							operation = 2;
						}
					}
					font.draw(batch, usersList.get(i).name, Gdx.graphics.getWidth() / 2 - 149,
							Gdx.graphics.getHeight() - 50 - (i + 1) * font.getLineHeight());
					font.setColor(Color.BLACK);
				}
			}
			batch.end();
		} else {
			shapeRenderer.begin(ShapeType.Filled);
			shapeRenderer.rect(Gdx.graphics.getWidth() / 2 - 300, 250, 600, 400, Color.WHITE, Color.WHITE, Color.WHITE,
					Color.WHITE);
			shapeRenderer.end();

			batch.begin();
			font.setColor(Color.BLACK);
			font.draw(batch, "Login:", Gdx.graphics.getWidth() / 2 - 149, Gdx.graphics.getHeight() - 50);

			for (int i = 0; i < usersList.size(); i++) {
				if (Gdx.input.getX() > Gdx.graphics.getWidth() / 2 - 300
						&& Gdx.input.getX() < Gdx.graphics.getWidth() / 2 + 300
						&& Gdx.graphics.getHeight() - Gdx.input.getY() > Gdx.graphics.getHeight() - 50
								- (i + 2) * font.getLineHeight()
						&& Gdx.graphics.getHeight() - Gdx.input.getY() < Gdx.graphics.getHeight() - 50
								- (i + 1) * font.getLineHeight()) {
					font.setColor(Color.RED);
					if (Gdx.input.isTouched()) {
						secondPlayer = usersList.get(i);
						// play the game now
						if (whichGame == 0)
							operation = 10; // classic
						else if (whichGame == 1)
							operation = 3; // magnetic
						else
							operation = 4; // cheese
					}
				}
				font.draw(batch, usersList.get(i).name, Gdx.graphics.getWidth() / 2 - 149,
						Gdx.graphics.getHeight() - 50 - (i + 1) * font.getLineHeight());
				font.setColor(Color.BLACK);
			}

			batch.end();
		}

		batch.begin();

		// backToMenu button
		if (Gdx.input.getX() > Gdx.graphics.getWidth() / 2 - backToMenu.getWidth() / 2
				&& Gdx.input.getX() < Gdx.graphics.getWidth() / 2 + backToMenu.getWidth() / 2
				&& Gdx.graphics.getHeight() - Gdx.input.getY() > Gdx.graphics.getHeight() / 8 * 3
				&& Gdx.graphics.getHeight() - Gdx.input.getY() < Gdx.graphics.getHeight() / 8 * 3
						+ backToMenu.getHeight()) {
			batch.draw(hoverBackToMenu, Gdx.graphics.getWidth() / 2 - hoverBackToMenu.getWidth() / 2,
					Gdx.graphics.getHeight() / 8 * 3);
			if (Gdx.input.justTouched())
				operation = 0;
		} else {
			batch.draw(backToMenu, Gdx.graphics.getWidth() / 2 - backToMenu.getWidth() / 2,
					Gdx.graphics.getHeight() / 8 * 3);
		}
		batch.end();
	}

	public void statistics() {
		batch.begin();
		batch.draw(menuBackground, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.end();

		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.rect(Gdx.graphics.getWidth() / 2 - 310, 240, 670, 420, Color.BROWN, Color.BROWN, Color.BROWN,
				Color.BROWN);
		shapeRenderer.rect(Gdx.graphics.getWidth() / 2 - 300, 250, 650, 400, Color.WHITE, Color.WHITE, Color.WHITE,
				Color.WHITE);
		shapeRenderer.end();

		batch.begin();
		// statistics
		font.setColor(Color.BLACK);
		font.draw(batch,
				"Login        |  Level  |  Count of punded bills  |  Count of punded bills continuously  |  tWins  |  Loss ",
				Gdx.graphics.getWidth() / 2 - 298, Gdx.graphics.getHeight() - 50);
		for (int i = 0; i < usersList.size(); i++) {
			font.draw(batch,
					usersList.get(i).name + "   |  " + usersList.get(i).level + "  |                  "
							+ usersList.get(i).holedBills + "                  |                            "
							+ usersList.get(i).maxHoledBills + "                            |      "
							+ usersList.get(i).winCount + "      |      " + usersList.get(i).loseCount,
					Gdx.graphics.getWidth() / 2 - 298, Gdx.graphics.getHeight() - 50 - (i + 1) * font.getLineHeight());
		}

		// backToMenu button
		if (Gdx.input.getX() > Gdx.graphics.getWidth() / 2 - backToMenu.getWidth() / 2
				&& Gdx.input.getX() < Gdx.graphics.getWidth() / 2 + backToMenu.getWidth() / 2
				&& Gdx.graphics.getHeight() - Gdx.input.getY() > Gdx.graphics.getHeight() / 8 * 1
				&& Gdx.graphics.getHeight() - Gdx.input.getY() < Gdx.graphics.getHeight() / 8 * 1
						+ backToMenu.getHeight()) {
			batch.draw(hoverBackToMenu, Gdx.graphics.getWidth() / 2 - hoverBackToMenu.getWidth() / 2,
					Gdx.graphics.getHeight() / 8 * 1);
			if (Gdx.input.justTouched())
				operation = 0;
		} else {
			batch.draw(backToMenu, Gdx.graphics.getWidth() / 2 - backToMenu.getWidth() / 2,
					Gdx.graphics.getHeight() / 8 * 1);
		}
		batch.end();
	}

	public void credits() {
		batch.begin();

		// credits info
		batch.draw(creditsInfo, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		// backToMenu button
		if (Gdx.input.getX() > Gdx.graphics.getWidth() / 2 - backToMenu.getWidth() / 2
				&& Gdx.input.getX() < Gdx.graphics.getWidth() / 2 + backToMenu.getWidth() / 2
				&& Gdx.graphics.getHeight() - Gdx.input.getY() > Gdx.graphics.getHeight() / 8 * 1
				&& Gdx.graphics.getHeight() - Gdx.input.getY() < Gdx.graphics.getHeight() / 8 * 1
						+ backToMenu.getHeight()) {
			batch.draw(hoverBackToMenu, Gdx.graphics.getWidth() / 2 - hoverBackToMenu.getWidth() / 2,
					Gdx.graphics.getHeight() / 8 * 1);
			if (Gdx.input.justTouched())
				operation = 0;
		} else {
			batch.draw(backToMenu, Gdx.graphics.getWidth() / 2 - backToMenu.getWidth() / 2,
					Gdx.graphics.getHeight() / 8 * 1);
		}
		batch.end();
	}

	public void training() {
		trainingTable.draw(shapeRenderer);
		trainingTable.drawNumbers(batch, font, shapeRenderer);

		// backToMenu button
		batch.begin();

		if (Gdx.input.getX() > Gdx.graphics.getWidth() - backToMenu.getWidth() - 10
				&& Gdx.input.getX() < Gdx.graphics.getWidth() - 10 && Gdx.graphics.getHeight()
						- Gdx.input.getY() > Gdx.graphics.getHeight() - hoverBackToMenu.getHeight() - 10
				&& Gdx.graphics.getHeight() - Gdx.input.getY() < Gdx.graphics.getHeight() - 10) {
			batch.draw(hoverBackToMenu, Gdx.graphics.getWidth() - hoverBackToMenu.getWidth() - 10,
					Gdx.graphics.getHeight() - hoverBackToMenu.getHeight() - 10);
			if (Gdx.input.justTouched())
				operation = 0;
		} else {
			batch.draw(backToMenu, Gdx.graphics.getWidth() - backToMenu.getWidth() - 10,
					Gdx.graphics.getHeight() - backToMenu.getHeight() - 10);
		}
		batch.end();
	}

	public void classic() {
		classicTable.draw(shapeRenderer);
		classicTable.drawNumbers(batch, font, shapeRenderer);

		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(Color.WHITE);
		shapeRenderer.rect(650, 510, 280, 190);
		shapeRenderer.setColor(Color.BROWN);
		shapeRenderer.rect(655, 515, 270, 180);
		shapeRenderer.end();

		batch.begin();
		font.setColor(Color.WHITE);
		font.draw(batch, firstPlayer.name + "(half) vs " + secondPlayer.name + "(filled)", 670, 650);
		font.draw(batch, classicTable.firstPlayerScore + " : " + classicTable.secondPlayerScore, 670, 630);
		if (classicTable.isNowFirstPlayer)
			font.draw(batch, "Now is " + firstPlayer.name + "'s turn", 670, 610);
		else
			font.draw(batch, "Now is " + secondPlayer.name + "'s turn", 670, 610);
		batch.end();

		if (classicTable.isGameOver) {
			shapeRenderer.begin(ShapeType.Filled);
			shapeRenderer.setColor(Color.WHITE);
			shapeRenderer.rect(Gdx.graphics.getWidth() / 2 - 300, Gdx.graphics.getHeight() / 2 - 200, 600, 400);
			shapeRenderer.setColor(Color.BROWN);
			shapeRenderer.rect(Gdx.graphics.getWidth() / 2 - 290, Gdx.graphics.getHeight() / 2 - 190, 580, 380);
			shapeRenderer.end();

			batch.begin();
			if (classicTable.firstPlayerScore >= classicTable.secondPlayerScore)
				font.draw(batch, firstPlayer.name + " won!", Gdx.graphics.getWidth() / 2 - 260,
						Gdx.graphics.getHeight() / 2 + 170);
			else
				font.draw(batch, secondPlayer.name + " won!", Gdx.graphics.getWidth() / 2 - 260,
						Gdx.graphics.getHeight() / 2 + 170);

			if (!areUsersUpdated) {
				firstPlayer.updateUser(usersList, cheeseTable.firstPlayerSumHited, cheeseTable.firstPlayerMaxHited,
						(cheeseTable.firstPlayerScore >= cheeseTable.secondPlayerScore));
				secondPlayer.updateUser(usersList, cheeseTable.secondPlayerSumHited, cheeseTable.secondPlayerMaxHited,
						(cheeseTable.secondPlayerScore >= cheeseTable.firstPlayerScore));
				areUsersUpdated = true;
			}

			font.draw(batch, firstPlayer.name + "'s statistics:", Gdx.graphics.getWidth() / 2 - 260,
					Gdx.graphics.getHeight() / 2 + 130);
			font.draw(batch, classicTable.firstPlayerHited + " countiniously holed bills",
					Gdx.graphics.getWidth() / 2 - 260, Gdx.graphics.getHeight() / 2 + 110);
			font.draw(batch, classicTable.firstPlayerMaxHited + " holed bills", Gdx.graphics.getWidth() / 2 - 260,
					Gdx.graphics.getHeight() / 2 + 90);

			font.draw(batch, secondPlayer.name + "'s statistics:", Gdx.graphics.getWidth() / 2 - 260,
					Gdx.graphics.getHeight() / 2 + 50);
			font.draw(batch, classicTable.secondPlayerMaxHited + " countiniously holed bills",
					Gdx.graphics.getWidth() / 2 - 260, Gdx.graphics.getHeight() / 2 + 30);
			font.draw(batch, classicTable.secondPlayerHited + " holed bills", Gdx.graphics.getWidth() / 2 - 260,
					Gdx.graphics.getHeight() / 2 + 10);
			// playAgain button
			if (Gdx.input.getX() > Gdx.graphics.getWidth() / 2 - playAgain.getWidth() / 2
					&& Gdx.input.getX() < Gdx.graphics.getWidth() / 2 + playAgain.getWidth() / 2
					&& Gdx.graphics.getHeight() - Gdx.input.getY() > Gdx.graphics.getHeight() / 8 * 3
					&& Gdx.graphics.getHeight() - Gdx.input.getY() < Gdx.graphics.getHeight() / 8 * 3
							+ backToMenu.getHeight()) {
				batch.draw(hoverPlayAgain, Gdx.graphics.getWidth() / 2 - hoverPlayAgain.getWidth() / 2,
						Gdx.graphics.getHeight() / 8 * 3);
				if (Gdx.input.justTouched()) {
					classicTable = new ClassicTable(50, 50, 1100, 400, 40, 0.003F, 0.7F, hitSound);
				}
			} else {
				batch.draw(playAgain, Gdx.graphics.getWidth() / 2 - playAgain.getWidth() / 2,
						Gdx.graphics.getHeight() / 8 * 3);
			}
			// backToMenu button
			if (Gdx.input.getX() > Gdx.graphics.getWidth() / 2 - backToMenu.getWidth() / 2
					&& Gdx.input.getX() < Gdx.graphics.getWidth() / 2 + backToMenu.getWidth() / 2
					&& Gdx.graphics.getHeight() - Gdx.input.getY() > Gdx.graphics.getHeight() / 8 * 2
					&& Gdx.graphics.getHeight() - Gdx.input.getY() < Gdx.graphics.getHeight() / 8 * 2
							+ backToMenu.getHeight()) {
				batch.draw(hoverBackToMenu, Gdx.graphics.getWidth() / 2 - hoverBackToMenu.getWidth() / 2,
						Gdx.graphics.getHeight() / 8 * 2);
				if (Gdx.input.justTouched())
					operation = 0;
			} else {
				batch.draw(backToMenu, Gdx.graphics.getWidth() / 2 - backToMenu.getWidth() / 2,
						Gdx.graphics.getHeight() / 8 * 2);
			}
			batch.end();
		}

		// backToMenu button
		batch.begin();
		if (Gdx.input.getX() > Gdx.graphics.getWidth() - backToMenu.getWidth() - 10
				&& Gdx.input.getX() < Gdx.graphics.getWidth() - 10 && Gdx.graphics.getHeight()
						- Gdx.input.getY() > Gdx.graphics.getHeight() - hoverBackToMenu.getHeight() - 10
				&& Gdx.graphics.getHeight() - Gdx.input.getY() < Gdx.graphics.getHeight() - 10) {
			batch.draw(hoverBackToMenu, Gdx.graphics.getWidth() - hoverBackToMenu.getWidth() - 10,
					Gdx.graphics.getHeight() - hoverBackToMenu.getHeight() - 10);
			if (Gdx.input.justTouched())
				operation = 0;
		} else {
			batch.draw(backToMenu, Gdx.graphics.getWidth() - backToMenu.getWidth() - 10,
					Gdx.graphics.getHeight() - backToMenu.getHeight() - 10);
		}
		batch.end();
	}

	public void magnetic() {
		magneticTable.draw(shapeRenderer);
		magneticTable.drawNumbers(batch, font, shapeRenderer);

		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(Color.WHITE);
		shapeRenderer.rect(650, 510, 280, 190);
		shapeRenderer.setColor(Color.BROWN);
		shapeRenderer.rect(655, 515, 270, 180);
		shapeRenderer.end();

		batch.begin();
		font.setColor(Color.WHITE);
		font.draw(batch, firstPlayer.name + "(half) vs " + secondPlayer.name + "(filled)", 670, 650);
		font.draw(batch, magneticTable.firstPlayerScore + " : " + magneticTable.secondPlayerScore, 670, 630);
		if (magneticTable.isNowFirstPlayer)
			font.draw(batch, "Now is " + firstPlayer.name + "'s turn", 670, 610);
		else
			font.draw(batch, "Now is " + secondPlayer.name + "'s turn", 670, 610);
		batch.end();

		if (magneticTable.isGameOver) {
			shapeRenderer.begin(ShapeType.Filled);
			shapeRenderer.setColor(Color.WHITE);
			shapeRenderer.rect(Gdx.graphics.getWidth() / 2 - 300, Gdx.graphics.getHeight() / 2 - 200, 600, 400);
			shapeRenderer.setColor(Color.BROWN);
			shapeRenderer.rect(Gdx.graphics.getWidth() / 2 - 290, Gdx.graphics.getHeight() / 2 - 190, 580, 380);
			shapeRenderer.end();

			batch.begin();
			if (magneticTable.firstPlayerScore >= magneticTable.secondPlayerScore)
				font.draw(batch, firstPlayer.name + " won!", Gdx.graphics.getWidth() / 2 - 260,
						Gdx.graphics.getHeight() / 2 + 170);
			else
				font.draw(batch, secondPlayer.name + " won!", Gdx.graphics.getWidth() / 2 - 260,
						Gdx.graphics.getHeight() / 2 + 170);

			if (!areUsersUpdated) {
				firstPlayer.updateUser(usersList, cheeseTable.firstPlayerSumHited, cheeseTable.firstPlayerMaxHited,
						(cheeseTable.firstPlayerScore >= cheeseTable.secondPlayerScore));
				secondPlayer.updateUser(usersList, cheeseTable.secondPlayerSumHited, cheeseTable.secondPlayerMaxHited,
						(cheeseTable.secondPlayerScore >= cheeseTable.firstPlayerScore));
				areUsersUpdated = true;
			}

			font.draw(batch, firstPlayer.name + "'s statistics:", Gdx.graphics.getWidth() / 2 - 260,
					Gdx.graphics.getHeight() / 2 + 130);
			font.draw(batch, magneticTable.firstPlayerSumHited + " countiniously holed bills",
					Gdx.graphics.getWidth() / 2 - 260, Gdx.graphics.getHeight() / 2 + 110);
			font.draw(batch, magneticTable.firstPlayerMaxHited + " holed bills", Gdx.graphics.getWidth() / 2 - 260,
					Gdx.graphics.getHeight() / 2 + 90);

			font.draw(batch, secondPlayer.name + "'s statistics:", Gdx.graphics.getWidth() / 2 - 260,
					Gdx.graphics.getHeight() / 2 + 50);
			font.draw(batch, magneticTable.secondPlayerMaxHited + " countiniously holed bills",
					Gdx.graphics.getWidth() / 2 - 260, Gdx.graphics.getHeight() / 2 + 30);
			font.draw(batch, magneticTable.secondPlayerSumHited + " holed bills", Gdx.graphics.getWidth() / 2 - 260,
					Gdx.graphics.getHeight() / 2 + 10);
			// playAgain button
			if (Gdx.input.getX() > Gdx.graphics.getWidth() / 2 - playAgain.getWidth() / 2
					&& Gdx.input.getX() < Gdx.graphics.getWidth() / 2 + playAgain.getWidth() / 2
					&& Gdx.graphics.getHeight() - Gdx.input.getY() > Gdx.graphics.getHeight() / 8 * 3
					&& Gdx.graphics.getHeight() - Gdx.input.getY() < Gdx.graphics.getHeight() / 8 * 3
							+ backToMenu.getHeight()) {
				batch.draw(hoverPlayAgain, Gdx.graphics.getWidth() / 2 - hoverPlayAgain.getWidth() / 2,
						Gdx.graphics.getHeight() / 8 * 3);
				if (Gdx.input.justTouched()) {
					magneticTable = new MagneticTable(50, 50, 1100, 400, 40, 0.003F, 0.7F, hitSound);
				}
			} else {
				batch.draw(playAgain, Gdx.graphics.getWidth() / 2 - playAgain.getWidth() / 2,
						Gdx.graphics.getHeight() / 8 * 3);
			}
			// backToMenu button
			if (Gdx.input.getX() > Gdx.graphics.getWidth() / 2 - backToMenu.getWidth() / 2
					&& Gdx.input.getX() < Gdx.graphics.getWidth() / 2 + backToMenu.getWidth() / 2
					&& Gdx.graphics.getHeight() - Gdx.input.getY() > Gdx.graphics.getHeight() / 8 * 2
					&& Gdx.graphics.getHeight() - Gdx.input.getY() < Gdx.graphics.getHeight() / 8 * 2
							+ backToMenu.getHeight()) {
				batch.draw(hoverBackToMenu, Gdx.graphics.getWidth() / 2 - hoverBackToMenu.getWidth() / 2,
						Gdx.graphics.getHeight() / 8 * 2);
				if (Gdx.input.justTouched())
					operation = 0;
			} else {
				batch.draw(backToMenu, Gdx.graphics.getWidth() / 2 - backToMenu.getWidth() / 2,
						Gdx.graphics.getHeight() / 8 * 2);
			}
			batch.end();
		}

		// backToMenu button
		batch.begin();
		if (Gdx.input.getX() > Gdx.graphics.getWidth() - backToMenu.getWidth() - 10
				&& Gdx.input.getX() < Gdx.graphics.getWidth() - 10 && Gdx.graphics.getHeight()
						- Gdx.input.getY() > Gdx.graphics.getHeight() - hoverBackToMenu.getHeight() - 10
				&& Gdx.graphics.getHeight() - Gdx.input.getY() < Gdx.graphics.getHeight() - 10) {
			batch.draw(hoverBackToMenu, Gdx.graphics.getWidth() - hoverBackToMenu.getWidth() - 10,
					Gdx.graphics.getHeight() - hoverBackToMenu.getHeight() - 10);
			if (Gdx.input.justTouched())
				operation = 0;
		} else {
			batch.draw(backToMenu, Gdx.graphics.getWidth() - backToMenu.getWidth() - 10,
					Gdx.graphics.getHeight() - backToMenu.getHeight() - 10);
		}
		batch.end();
	}

	public void cheese() {
		cheeseTable.draw(shapeRenderer);
		cheeseTable.drawNumbers(batch, font, shapeRenderer);

		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(Color.WHITE);
		shapeRenderer.rect(650, 510, 280, 190);
		shapeRenderer.setColor(Color.BROWN);
		shapeRenderer.rect(655, 515, 270, 180);
		shapeRenderer.end();

		batch.begin();
		font.setColor(Color.WHITE);
		font.draw(batch, firstPlayer.name + "(half) vs " + secondPlayer.name + "(filled)", 670, 650);
		font.draw(batch, cheeseTable.firstPlayerScore + " : " + cheeseTable.secondPlayerScore, 670, 630);
		if (cheeseTable.isNowFirstPlayer)
			font.draw(batch, "Now is " + firstPlayer.name + "'s turn", 670, 610);
		else
			font.draw(batch, "Now is " + secondPlayer.name + "'s turn", 670, 610);
		batch.end();

		if (cheeseTable.isGameOver) {
			shapeRenderer.begin(ShapeType.Filled);
			shapeRenderer.setColor(Color.WHITE);
			shapeRenderer.rect(Gdx.graphics.getWidth() / 2 - 300, Gdx.graphics.getHeight() / 2 - 200, 600, 400);
			shapeRenderer.setColor(Color.BROWN);
			shapeRenderer.rect(Gdx.graphics.getWidth() / 2 - 290, Gdx.graphics.getHeight() / 2 - 190, 580, 380);
			shapeRenderer.end();

			batch.begin();

			if (!areUsersUpdated) {
				firstPlayer.updateUser(usersList, cheeseTable.firstPlayerSumHited, cheeseTable.firstPlayerMaxHited,
						(cheeseTable.firstPlayerScore >= cheeseTable.secondPlayerScore));
				secondPlayer.updateUser(usersList, cheeseTable.secondPlayerSumHited, cheeseTable.secondPlayerMaxHited,
						(cheeseTable.secondPlayerScore >= cheeseTable.firstPlayerScore));
				areUsersUpdated = true;
			}
			
			if (cheeseTable.firstPlayerScore >= cheeseTable.secondPlayerScore)
				font.draw(batch, firstPlayer.name + " won!", Gdx.graphics.getWidth() / 2 - 260,
						Gdx.graphics.getHeight() / 2 + 170);
			else
				font.draw(batch, secondPlayer.name + " won!", Gdx.graphics.getWidth() / 2 - 260,
						Gdx.graphics.getHeight() / 2 + 170);

			font.draw(batch, firstPlayer.name + "'s statistics:", Gdx.graphics.getWidth() / 2 - 260,
					Gdx.graphics.getHeight() / 2 + 130);
			font.draw(batch, cheeseTable.firstPlayerSumHited + " countiniously holed bills",
					Gdx.graphics.getWidth() / 2 - 260, Gdx.graphics.getHeight() / 2 + 110);
			font.draw(batch, cheeseTable.firstPlayerMaxHited + " holed bills", Gdx.graphics.getWidth() / 2 - 260,
					Gdx.graphics.getHeight() / 2 + 90);

			font.draw(batch, secondPlayer.name + "'s statistics:", Gdx.graphics.getWidth() / 2 - 260,
					Gdx.graphics.getHeight() / 2 + 50);
			font.draw(batch, cheeseTable.secondPlayerMaxHited + " countiniously holed bills",
					Gdx.graphics.getWidth() / 2 - 260, Gdx.graphics.getHeight() / 2 + 30);
			font.draw(batch, cheeseTable.secondPlayerSumHited + " holed bills", Gdx.graphics.getWidth() / 2 - 260,
					Gdx.graphics.getHeight() / 2 + 10);
			// playAgain button
			if (Gdx.input.getX() > Gdx.graphics.getWidth() / 2 - playAgain.getWidth() / 2
					&& Gdx.input.getX() < Gdx.graphics.getWidth() / 2 + playAgain.getWidth() / 2
					&& Gdx.graphics.getHeight() - Gdx.input.getY() > Gdx.graphics.getHeight() / 8 * 3
					&& Gdx.graphics.getHeight() - Gdx.input.getY() < Gdx.graphics.getHeight() / 8 * 3
							+ backToMenu.getHeight()) {
				batch.draw(hoverPlayAgain, Gdx.graphics.getWidth() / 2 - hoverPlayAgain.getWidth() / 2,
						Gdx.graphics.getHeight() / 8 * 3);
				if (Gdx.input.justTouched()) {
					cheeseTable = new CheeseTable(50, 50, 1100, 400, 40, 0.003F, 0.7F, hitSound);
				}
			} else {
				batch.draw(playAgain, Gdx.graphics.getWidth() / 2 - playAgain.getWidth() / 2,
						Gdx.graphics.getHeight() / 8 * 3);
			}
			// backToMenu button
			if (Gdx.input.getX() > Gdx.graphics.getWidth() / 2 - backToMenu.getWidth() / 2
					&& Gdx.input.getX() < Gdx.graphics.getWidth() / 2 + backToMenu.getWidth() / 2
					&& Gdx.graphics.getHeight() - Gdx.input.getY() > Gdx.graphics.getHeight() / 8 * 2
					&& Gdx.graphics.getHeight() - Gdx.input.getY() < Gdx.graphics.getHeight() / 8 * 2
							+ backToMenu.getHeight()) {
				batch.draw(hoverBackToMenu, Gdx.graphics.getWidth() / 2 - hoverBackToMenu.getWidth() / 2,
						Gdx.graphics.getHeight() / 8 * 2);
				if (Gdx.input.justTouched())
					operation = 0;
			} else {
				batch.draw(backToMenu, Gdx.graphics.getWidth() / 2 - backToMenu.getWidth() / 2,
						Gdx.graphics.getHeight() / 8 * 2);
			}
			batch.end();
		}

		// backToMenu button
		batch.begin();
		if (Gdx.input.getX() > Gdx.graphics.getWidth() - backToMenu.getWidth() - 10
				&& Gdx.input.getX() < Gdx.graphics.getWidth() - 10 && Gdx.graphics.getHeight()
						- Gdx.input.getY() > Gdx.graphics.getHeight() - hoverBackToMenu.getHeight() - 10
				&& Gdx.graphics.getHeight() - Gdx.input.getY() < Gdx.graphics.getHeight() - 10) {
			batch.draw(hoverBackToMenu, Gdx.graphics.getWidth() - hoverBackToMenu.getWidth() - 10,
					Gdx.graphics.getHeight() - hoverBackToMenu.getHeight() - 10);
			if (Gdx.input.justTouched())
				operation = 0;
		} else {
			batch.draw(backToMenu, Gdx.graphics.getWidth() - backToMenu.getWidth() - 10,
					Gdx.graphics.getHeight() - backToMenu.getHeight() - 10);
		}
		batch.end();
	}
}
