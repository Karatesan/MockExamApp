package com.fdmgroup.MockExam.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fdmgroup.MockExam.model.Image;
import com.fdmgroup.MockExam.repositories.ImageRepository;

/**
 * The class implements the ImageService interface.
 * Provides methods to find, get and save images.
 */
@Service
public class ImageServiceImpl implements ImageService {

	private final ImageRepository imageRepository;

    /**
     * Constructs a new ImageServiceImpl instance.
     * @param imageRepository The repository for managing images.
     */
	@Autowired
	public ImageServiceImpl(ImageRepository imageRepository) {
		this.imageRepository = imageRepository;
	}

	@Override
	public Image getImageById(Integer id) {
		return imageRepository.findById(id).orElse(null);
	}

	@Override
	public List<Image> findAllImages() {
		return imageRepository.findAll();
	}
	
	@Override
	public Image findByName(String name) {
		return imageRepository.findByName(name);
	}

	@Override
	public Image save(Image image) {		
		return imageRepository.save(image);
	}

}
