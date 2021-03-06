package com.matchagames.setmania.service;

import com.matchagames.setmania.models.Answer;
import com.matchagames.setmania.models.Picture;

import java.util.List;
import java.util.ArrayList;

public class SetServiceImpl {
	
	private List<Answer> correctAnswers;
	
	private boolean isCompletelyDifferent(String one, String two,
			String three) {
		if (!one.equals(two) && !one.equals(three) && !two.equals(three))
			return true;
		return false;
	}

	private boolean isSame(String one, String two, String three) {
		if (one.equals(two) && two.equals(three))
			return true;
		return false;

	}

	private boolean isAnAnswer(Picture one, Picture two, Picture three) {

		boolean b = false, a = false, ac = false;
		if (isSame(one.getBackground(), two.getBackground(),
				three.getBackground())
				|| isCompletelyDifferent(one.getBackground(),
						two.getBackground(), three.getBackground()))
			b = true;
		if (isSame(one.getAnimal(), two.getAnimal(), three.getAnimal())
				|| isCompletelyDifferent(one.getAnimal(), two.getAnimal(),
						three.getAnimal()))
			a = true;
		if (isSame(one.getAnimalColor(), two.getAnimalColor(),
				three.getAnimalColor())
				|| isCompletelyDifferent(one.getAnimalColor(),
						two.getAnimalColor(), three.getAnimalColor()))
			ac = true;

		return b && a && ac;
	}

	private boolean isDuplicate(List<Answer> answers, Answer newAnswer) {
		for (Answer answer : answers) {
			if (answer.isSame(newAnswer))
				return true;
		}
		return false;
	}

	public void initAllPossibleSets(Picture[] pictures) {

		List<Answer> answers = new ArrayList<Answer>();
		for (int i = 0; i < pictures.length; i++) {
			for (int j = 0; j < pictures.length; j++) {
				for (int k = 0; k < pictures.length; k++) {
					if (!(i == j || i == k || j == k)) {
						Picture one = pictures[i];
						Picture two = pictures[j];
						Picture three = pictures[k];
						if (isAnAnswer(one, two, three)) {
							Answer newAnswer = new Answer(i, j, k);
							if (!isDuplicate(answers, newAnswer))
								answers.add(newAnswer);
						}
					}
				}
			}
		}
		correctAnswers = answers;
	}

	public boolean isCorrectAnswer(Answer answer) {
		for (Answer a : correctAnswers)
			if (a.isSame(answer)){
				correctAnswers.remove(a);
				return true;
			}
		return false;
	}
	
	public List<Answer> getCorrectAnswers() {
		return correctAnswers;
	}
	
}
