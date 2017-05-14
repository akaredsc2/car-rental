package org.vitaly.service.abstraction;

import java.io.File;

/**
 * Image service
 */
public interface ImageService {

    /**
     * Upload image to cloud and return url
     *
     * @param image image
     * @return created url
     */
    String upload(File image);
}
