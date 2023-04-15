package com.bitarrded.youtube.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        // Save the file to a directory on the server
        File directory = new File("D:\\uploads");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        File uploadedFile = new File(directory.getAbsolutePath() + File.separator + UUID.randomUUID());
        file.transferTo(uploadedFile);
        String id = uploadedFile.getName();
        return id;
    }
}
