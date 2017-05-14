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
    private static final String CLOUD_NAME = "cloud_name";
    private static final String API_KEY = "api_key";
    private static final String API_SECRET = "api_secret";
    private static final String NAME = "vitaly";
    private static final String KEY = "154489512775426";
    private static final String SECRET = "Xg-yOs-Zqw_qe9QI0oYmIKDtsiU";

    private static Logger logger = LogManager.getLogger(CloudinaryImageService.class.getName());

    private Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
            CLOUD_NAME, NAME,
            API_KEY, KEY,
            API_SECRET, SECRET));

    /**
     * @inheritDoc
     */
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
