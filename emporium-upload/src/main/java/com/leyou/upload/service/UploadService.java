package com.leyou.upload.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author Si6x
 */
public interface UploadService {

    String uploadImage(MultipartFile file);
}
