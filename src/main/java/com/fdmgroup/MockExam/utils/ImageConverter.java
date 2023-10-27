package com.fdmgroup.MockExam.utils;

import java.io.IOException;
import java.util.Base64;

import org.springframework.web.multipart.MultipartFile;

import com.fdmgroup.MockExam.dto.ImageResponseDTO;
import com.fdmgroup.MockExam.model.Image;
import com.fdmgroup.MockExam.model.UserImage;



public class ImageConverter {
	
	 public static String convertToBase64String(byte[] image) {
		 
	        return Base64.getEncoder().encodeToString(image);		 	 
}
	
    public static  Image convertMultiPartFileToImage(MultipartFile file) throws IOException {
        byte[] imageData = file.getBytes();
        String imageName = file.getOriginalFilename();

        Image image = new Image();
        image.setName(imageName);
        image.setData(imageData);
        image.setSize(file.getSize());
        return image;
    }
    
	 public static ImageResponseDTO convertImageToResponseImage(Image image) {
		 if(image ==null) return null;
		 ImageResponseDTO responseImage = new ImageResponseDTO(image.getId(), 
				 image.getName(),  
				 image.getSize(), 
				 convertToBase64String(image.getData()));
		 return responseImage;
}

	public static UserImage convertMultiPartFileToUserImage(MultipartFile file) throws IOException {
		  byte[] imageData = file.getBytes();
	        String imageName = file.getOriginalFilename();

	        UserImage image = new UserImage();
	        image.setName(imageName);
	        image.setData(imageData);
	        image.setSize(file.getSize());
	        return image;
	}
}
