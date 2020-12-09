package com.example.oauth2sociallogin.service;

import com.example.oauth2sociallogin.domain.File;
import com.example.oauth2sociallogin.repository.FileRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class FileService {

    private final FileRepository fileRepository;

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public Long uploadFile(MultipartFile file) throws IOException {
        File fileToUpload = new File();
        fileToUpload.setFileName(file.getOriginalFilename());
        fileToUpload.setContentType(file.getContentType());
        fileToUpload.setContent(file.getBytes());

       return fileRepository.save(fileToUpload)
               .getId();
    }

    public File getFile(Long id){

       return fileRepository.findById(id).orElse(null);
    }
}
