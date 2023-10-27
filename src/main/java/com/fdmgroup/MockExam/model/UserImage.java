package com.fdmgroup.MockExam.model;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents an Image entity in the database.
 */
@Data
@NoArgsConstructor
@Entity
public class UserImage extends Image{
    
//	@Id
//    @GeneratedValue
//    private Integer id;
//    private String name;
//    private long size;
//    @Lob
//    @Column(columnDefinition = "BINARY")
//    private byte[] data;
//    
//    /**
//     * Constructs an Image object with the specified name, size, and data.
//     * @param name The name of the image.
//     * @param size The size of the image in bytes.
//     * @param data The binary data representing the image.
//     */
//	public UserImage(String name, long size, byte[] data) {
//		super();
//		this.name = name;
//		this.size = size;
//		this.data = data;
//	}
}
