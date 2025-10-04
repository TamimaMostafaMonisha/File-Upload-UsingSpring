package com.tclk.File_Upload.repository;

import com.tclk.File_Upload.entity.FileData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FileDataRepository extends JpaRepository<FileData, Long> {
//    Optional<FileData> findByName(String )

    @Query(value = "SELECT * FROM FILE_DATA where name = :name limit 1", nativeQuery = true)
    public FileData findAllSortedByNameUsingNative(@Param(value = "name") String name );
}
