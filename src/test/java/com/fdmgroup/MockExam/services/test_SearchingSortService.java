package com.fdmgroup.MockExam.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fdmgroup.MockExam.dto.IdFoundInSearchDTO;
import com.fdmgroup.MockExam.dto.QuestionFoundInSearchDTO;

@ExtendWith(MockitoExtension.class)

public class test_SearchingSortService {
	@InjectMocks
	private SearchingSortServiceImpls searchingSortService;

	@Test
	public void test_findSearchedItemsOnPage() {
		List<QuestionFoundInSearchDTO> list = new ArrayList<>();

		for (int i = 1; i <= 13; i++) {
			QuestionFoundInSearchDTO newItem = new QuestionFoundInSearchDTO();
			newItem.setQuestionContent("This is content of question: " + i);
			list.add(newItem);
		}

		List<QuestionFoundInSearchDTO> result = searchingSortService.findSearchedItemsOnPage(list, 3);

		assertEquals(3, result.size());
		assertEquals("This is content of question: 11", result.get(0).getQuestionContent());
		assertEquals("This is content of question: 12", result.get(1).getQuestionContent());
		assertEquals("This is content of question: 13", result.get(2).getQuestionContent());

	}

	// ---------------------------------------------------------------------------------------------

	@Test
    public void testSortList() {
        List<IdFoundInSearchDTO> list = new ArrayList<>();
        list.add(new IdFoundInSearchDTO(1));
        list.add(new IdFoundInSearchDTO(2));
        list.add(new IdFoundInSearchDTO(3));

        List<IdFoundInSearchDTO> sortedList = searchingSortService.sortList(list);

        assertEquals(3, sortedList.get(0).getId());
        assertEquals(2, sortedList.get(1).getId());
        assertEquals(1, sortedList.get(2).getId());

 
    }
}