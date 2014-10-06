package com.maol.setpuzzle.models;

public class Answer {

	private int[] answer;

	public Answer(int a, int b, int c) {
		answer = new int[3];
		answer[0] = a;
		answer[1] = b;
		answer[2] = c;
	}

	public int[] getAnswer() {
		return answer;
	}

	public boolean isSame(Answer answer) {
		return isSame(answer.getAnswer()[0], answer.getAnswer()[1],
				answer.getAnswer()[2]);
	}

	public boolean isSame(int x, int y, int z) {
		boolean flag = true;
		if (notInList(x) || notInList(y) || notInList(z))
			flag = false;
		return flag;
	}

	private boolean notInList(int x) {
		boolean flag = true;
		for (int i : answer) {
			if (i == x)
				flag = false;
		}
		return flag;
	}
}
