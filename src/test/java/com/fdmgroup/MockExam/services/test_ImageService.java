package com.fdmgroup.MockExam.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fdmgroup.MockExam.model.Image;
import com.fdmgroup.MockExam.repositories.ImageRepository;

@ExtendWith(MockitoExtension.class)
public class test_ImageService {
	@InjectMocks
	private ImageServiceImpl imageService;

	@Mock
	private ImageRepository imageRepository;

	private Image image;
	Image actual;

	@BeforeEach
	public void setUp() {
		image = new Image();
		actual = new Image();
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_getImageById_pushId_checkRepositoryFinding() {
		doReturn(Optional.of(image)).when(imageRepository).findById(1);

		actual = imageService.getImageById(1);

		assertEquals(image, actual);
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_findAllImages_checkRepositoryWorks() {
		List<Image> list = Arrays.asList(image);
		doReturn(list).when(imageRepository).findAll();

		List<Image> actual = imageService.findAllImages();

		assertEquals(list, actual);
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_findByName() {
		doReturn(image).when(imageRepository).findByName(Mockito.anyString());
		actual = imageService.findByName("Find Image");
		assertEquals(image, actual);

	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_save() {
		doReturn(image).when(imageRepository).save(image);
		actual = imageService.save(image);
		assertEquals(image, actual);

	}
}
