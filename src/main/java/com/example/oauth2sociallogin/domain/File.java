package com.example.oauth2sociallogin.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
@Data
public class File {

    @Id
    @GeneratedValue
    private Long id;

    private String contentType;
    private String fileName;
    @Lob
    private byte[] content;
}
