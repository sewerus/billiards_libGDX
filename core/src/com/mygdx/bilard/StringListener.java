package com.mygdx.bilard;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.TextInputListener;

public class StringListener extends ApplicationAdapter implements TextInputListener {
	String text;

	 @Override
	public void create() {
	 }
	
	 public void render() {
		 Gdx.input.getTextInput(this, "Wczytaj tekst", "", "");
	 }

	@Override
	public void input(String text) {
		this.text = text;
	}

	@Override
	public void canceled() {
		text = "Empty";
	}
}
