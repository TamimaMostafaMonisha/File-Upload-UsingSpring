package com.tclk.File_Upload.controller;

import com.tclk.File_Upload.dto.FileInfo;
import com.tclk.File_Upload.message.ResponseMessage;
import com.tclk.File_Upload.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class controller {

    private final StorageService service;

    @PostMapping("/fileSystem")
    public ResponseEntity<?> uploadImageToFileSystem(@RequestParam("image")MultipartFile file)
            throws IOException{
        String message = "";
        try {
            String uploadImage = service.uploadImageToFileSystem(file);
            message = "Uploaded the file succecssfully: " + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        }catch(Exception e){
            message = "Could not upload the file" + file.getOriginalFilename() + ". Error: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));

        }
    }


    @GetMapping("/fileSystem/{fileName}")
    public ResponseEntity<?> downloadImageFromFileSystem(@PathVariable String fileName)
    throws IOException{
        byte[] imageData = service.downloadImageFromFileSystem(fileName);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(imageData);
    }

    @GetMapping("/fileSystem")
    public ResponseEntity<?> getAllImage() throws IOException{
        List<FileInfo> fileInfoList = service.getAllImage();
        return  ResponseEntity.status(HttpStatus.OK).body(fileInfoList);
    }

    @DeleteMapping("/name/{name}")
    public ResponseEntity<?> deleteFile(@PathVariable String name) {
        try {
            service.deleteByName(name);
            return ResponseEntity.ok(new ResponseMessage("File deleted successfully!"));
        } catch (Exception e) {
            //logger.error("Failed to delete file with id {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Could not delete file: " + e.getMessage());
        }
    }


}
