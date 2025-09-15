package com.CodeWithTrevy.SpringBootImageUploader.controller;

import com.CodeWithTrevy.SpringBootImageUploader.dto.UploadResponse;
import com.CodeWithTrevy.SpringBootImageUploader.service.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.constraints.NotNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/photos")
public class PhotoUploadController {

    private final CloudinaryService cloudinaryService;
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5 MB
    private static final List<String> ALLOWED_FILE_TYPES = Arrays.asList("image/jpeg", "image/png", "image/gif");

    @Autowired
    public PhotoUploadController(CloudinaryService cloudinaryService) {
        this.cloudinaryService = cloudinaryService;
    }

    @PostMapping("/upload")
    public ResponseEntity<UploadResponse> uploadPhoto(
            @RequestParam("file") @NotNull MultipartFile file) { // @NotNull from jakarta.validation


        if (file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please select a file to upload.");
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File size exceeds the limit of 5MB.");
        }
        if (!ALLOWED_FILE_TYPES.contains(file.getContentType())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only JPEG, PNG, and GIF image types are allowed.");
        }

        try {
            Map uploadResult = cloudinaryService.uploadImage(file);
            String publicId = (String) uploadResult.get("public_id");
            String imageUrl = (String) uploadResult.get("secure_url");

            String thumbnailUrl = cloudinaryService.generateThumbnailUrl(publicId, 150, 150);


            String watermarkedUrl = cloudinaryService.applyWatermark(publicId);
//            String sepiaFilteredUrl = cloudinaryService.applySepiaFilter(publicId);


            UploadResponse response = new UploadResponse(
                    "Photo uploaded successfully!",
                    imageUrl,
                    thumbnailUrl,
                    publicId,
                    watermarkedUrl
            );
//                    sepiaFilteredUrl


            return ResponseEntity.ok(response);

        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to upload image: " + e.getMessage(), e);
        } catch (Exception e) { // Catch other potential Cloudinary errors
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred during upload: " + e.getMessage(), e);
        }
    }
}