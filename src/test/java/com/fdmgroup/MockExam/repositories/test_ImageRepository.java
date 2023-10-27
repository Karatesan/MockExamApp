package com.fdmgroup.MockExam.repositories;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.fdmgroup.MockExam.model.Image;

@DataJpaTest
class test_ImageRepository {
	
	@Autowired
	private ImageRepository imageRepository;
	
	@Autowired
	private TestEntityManager entityManager;

	
	@Test
	public void test_findByName_withExistingImageName() {
		Image image = new Image();
		image.setName("ExistingImage.jpg");
		entityManager.persist(image);
		entityManager.flush();
		
		Image foundImage = imageRepository.findByName("ExistingImage.jpg");
		assertNotNull(foundImage);
		assertEquals("ExistingImage.jpg", foundImage.getName());
	}
	
	@Test
	public void test_findByName_withNonexistingImageName() {
		Image foundImage = imageRepository.findByName("Nonexisting.jpg");
		assertNull(foundImage);
	}

}