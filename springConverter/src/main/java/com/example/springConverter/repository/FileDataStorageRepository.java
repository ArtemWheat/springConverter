package com.example.springConverter.repository;

import com.example.springConverter.entity.FileData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileDataStorageRepository extends JpaRepository<FileData, String> {
    Optional<FileData> findByName(String fileName);
}

