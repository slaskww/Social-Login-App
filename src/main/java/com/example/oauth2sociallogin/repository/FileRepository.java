package com.example.oauth2sociallogin.repository;

import com.example.oauth2sociallogin.domain.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {
}
