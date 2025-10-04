package com.tclk.File_Upload.service;

import com.tclk.File_Upload.dto.FileInfo;
import com.tclk.File_Upload.entity.FileData;
import com.tclk.File_Upload.repository.FileDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service

public class StorageService {

    @Autowired
    private FileDataRepository fileDataRepository;

    private final String FOLDER_PATH = "D:\\Image\\";

//    public byte[] downloadImage(String fileName){
//
//    }
    public String uploadImageToFileSystem(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String extension = "";

        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        String uniqueFileName = UUID.randomUUID().toString() + extension;
        String filePath = FOLDER_PATH + uniqueFileName;

//    String filePath  = FOLDER_PATH + file.getOriginalFilename();

        FileData fileData = FileData.builder()
            .name(uniqueFileName)
            .type(file.getContentType())
            .filePath(filePath)
            .build();

        FileData saved = fileDataRepository.save(fileData);

        // Save the actual file in the folder
        file.transferTo(new File(filePath));

        return "File uploaded successfully: " + saved.getFilePath();
    }


    public byte[] downloadImageFromFileSystem(String fileName) throws IOException{

        FileData fileData = fileDataRepository.findAllSortedByNameUsingNative(fileName);

        if(fileData == null){
            throw new RuntimeException("File not found: " + fileName);
        }
        return Files.readAllBytes(new File(fileData.getFilePath()).toPath());

    }

    public List<FileInfo> getAllImage(){
        List<FileInfo> fileInfos = new ArrayList<>();
        List<FileData> fileDataList = fileDataRepository.findAll();

        fileDataList.forEach(fileData -> {
            try {
                byte[] image = Files.readAllBytes(new File(fileData.getFilePath()).toPath());
                fileInfos.add(new FileInfo(fileData.getName(), fileData.getFilePath(), image));
            } catch (IOException e){
                throw new RuntimeException("Error reading file: " + fileData.getName(), e);
            }
        });
        return fileInfos;
    }

}
