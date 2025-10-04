package com.tclk.File_Upload.repository;

import com.tclk.File_Upload.entity.ImageData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageDataRepository extends JpaRepository<ImageData, Long> {
}
