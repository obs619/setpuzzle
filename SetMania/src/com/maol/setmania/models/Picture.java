package com.maol.setmania.models;

public class Picture {

	private String background;
	private String animal;
	private String animalColor;

	public Picture() {
	}

	public String getBackground() {
		return background;
	}

	public void setBackground(String background) {
		this.background = background;
	}

	public String getAnimal() {
		return animal;
	}

	public void setAnimal(String animal) {
		this.animal = animal;
	}

	public String getAnimalColor() {
		return animalColor;
	}

	public void setAnimalColor(String animalColor) {
		this.animalColor = animalColor;
	}
	
	@Override
	public String toString() {
		return getAnimalColor() + " " + getAnimal() + " inside " + getBackground();
	}
	
}
