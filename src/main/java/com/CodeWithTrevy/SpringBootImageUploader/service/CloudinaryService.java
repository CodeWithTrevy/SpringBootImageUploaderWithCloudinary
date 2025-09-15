package com.CodeWithTrevy.SpringBootImageUploader.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    @Autowired
    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public Map uploadImage(MultipartFile file) throws IOException {

        return cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap(
                        "folder", "spring_uploads"
                ));
    }

    public String generateThumbnailUrl(String publicId, int width, int height) {

        return cloudinary.url()
                .transformation(new Transformation()
                        .width(width)
                        .height(height)
                        .crop("fill")
                        .gravity("auto"))
                        .generate(publicId);
    }

    public String applyWatermark(String publicId) {

        return cloudinary.url()
                .transformation(new Transformation()
                        .width(400).crop("limit")
                        .overlay("text:Arial_30:MyBrand")
                        .gravity("south_east")
                        .x(10).y(10)
                        .opacity(70)
                        .color("blue")
                )
                .generate(publicId);
    }


//    public String applySepiaFilter(String publicId) {
//        return cloudinary.url()
//                .transformation(new Transformation().effect("sepia"))
//                .generate(publicId);
//    }
}