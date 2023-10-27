package com.fdmgroup.MockExam.comperators;

import java.util.Comparator;

import com.fdmgroup.MockExam.dto.IdFoundInSearchDTO;
import com.fdmgroup.MockExam.dto.UserFoundInSearchDTO;

public class SearchIdComperator implements Comparator<IdFoundInSearchDTO>{

	@Override
	public int compare(IdFoundInSearchDTO o1, IdFoundInSearchDTO o2) {
		  return Integer.compare(o2.getId(), o1.getId());
	}

}
