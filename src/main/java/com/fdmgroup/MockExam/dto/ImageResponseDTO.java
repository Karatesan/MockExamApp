package com.fdmgroup.MockExam.dto;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
//Class for sending images to frontend, image is base64 encoded string
public class ImageResponseDTO {
    @Id
    private Integer id;
    private String name;
    private long size;
    private String base64image;
	public ImageResponseDTO(String name, long size, String base64image) {
		super();
		this.name = name;
		this.size = size;
		this.base64image = base64image;
	}
}
