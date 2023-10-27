package com.fdmgroup.MockExam.model;

import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents an Image entity in the database.
 */
@Data
@NoArgsConstructor
@Entity
public class Image {
    
	@Id
    @GeneratedValue
    private Integer id;
    private String name;
    private long size;
    @Column(length = 1048576)
    private byte[] data;
    
    /**
     * Constructs an Image object with the specified name, size, and data.
     * @param name The name of the image.
     * @param size The size of the image in bytes.
     * @param data The binary data representing the image.
     */
	public Image(String name, long size, byte[] data) {
		super();
		this.name = name;
		this.size = size;
		this.data = data;
	}
}
