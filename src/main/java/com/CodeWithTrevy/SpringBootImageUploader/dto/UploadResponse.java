package com.CodeWithTrevy.SpringBootImageUploader.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadResponse {
    private String message;
    private String imageUrl;
    private String thumbnailUrl;
    private String publicId;
    private String watermarkedUrl;
//    private String sepiaFilteredUrl;
}