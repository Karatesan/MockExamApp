package com.fdmgroup.MockExam.services;

import java.util.List;

import com.fdmgroup.MockExam.dto.IdFoundInSearchDTO;

public interface SearchingSortService {
	<T extends IdFoundInSearchDTO> List<T> sortList(List<T> list);

	<T extends IdFoundInSearchDTO> List<T> findSearchedItemsOnPage(List<T> list, int page);
}
