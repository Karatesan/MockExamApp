package com.fdmgroup.MockExam.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fdmgroup.MockExam.comperators.SearchIdComperator;
import com.fdmgroup.MockExam.dto.IdFoundInSearchDTO;
@Service
public class SearchingSortServiceImpls implements SearchingSortService {

	@Override
	public <T extends IdFoundInSearchDTO> List<T> sortList(List<T> list) {
		SearchIdComperator comparator = new SearchIdComperator();
		Collections.sort(list, comparator);
		return list;
	}

	// ----------------------------------------------------------------------------------

	@Override
	public <T extends IdFoundInSearchDTO> List<T> findSearchedItemsOnPage(List<T> list, int page) {
		List<T> returningList = new ArrayList<>();

		for (int i = page * 5 - 5; i < page * 5; i++) {
			try {
				returningList.add(list.get(i));
			} catch (Exception e) {
			}
		}
		return returningList;
	}

}
