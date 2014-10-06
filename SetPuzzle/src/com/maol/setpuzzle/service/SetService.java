package com.maol.setpuzzle.service;

import com.maol.setpuzzle.models.Answer;
import com.maol.setpuzzle.models.Picture;

import java.util.List;

public interface SetService {

	public boolean isCorrectAnswer(Answer answer);
	
	public  List<Answer> getAllPossibleSets(Picture[] pictures);
}
