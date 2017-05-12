package org.vitaly.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vitaly.service.abstraction.ImageService;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

/**
 * Created by vitaly on 2017-05-12.
 */
public class CloudinaryImageService implements ImageService {
    private static Logger logger = LogManager.getLogger(CloudinaryImageService.class.getName());

    private Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", "vitaly",
            "api_key", "154489512775426",
            "api_secret", "Xg-yOs-Zqw_qe9QI0oYmIKDtsiU"));

    @Override
    public String upload(File image) {
        try {
            return (String) cloudinary.uploader()
                    .upload(image, Collections.emptyMap())
                    .get("url");
        } catch (IOException e) {
            logger.error("Failed to upload image", e);
        }

        return null;
    }
}
