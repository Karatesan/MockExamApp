package com.fdmgroup.MockExam.services;

import java.util.List;

import com.fdmgroup.MockExam.model.Image;

/**
 * Interface to define the contract for the ImageService.
 * Provides methods for managing images.
 */
public interface ImageService {

    /**
     * Retrieves an image by its ID.
     * @param id The ID of the image to retrieve.
     * @return The image with the specified ID, or null if not found.
     */
	Image getImageById(Integer id);

    /**
     * Retrieves a list of all images.
     * @return A list containing all images in the repository.
     */
	List<Image> findAllImages();

    /**
     * Finds an image by its name.
     * @param name The name of the image to search for.
     * @return The image with the specified name, if found.
     */
	Image findByName(String name);

    /**
     * Saves an image.
     * @param image The image to be saved.
     * @return The saved image.
     */
	Image save(Image image);

}