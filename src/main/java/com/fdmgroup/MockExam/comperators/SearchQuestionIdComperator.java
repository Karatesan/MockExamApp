package com.fdmgroup.MockExam.comperators;

import java.util.Comparator;

import com.fdmgroup.MockExam.dto.QuestionFoundInSearchDTO;

public class SearchQuestionIdComperator implements Comparator<QuestionFoundInSearchDTO>{

	@Override
	public int compare(QuestionFoundInSearchDTO o1, QuestionFoundInSearchDTO o2) {
		  return Integer.compare(o2.getId(), o1.getId());
	}

}
