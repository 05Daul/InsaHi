package com.playdata.common.publicEntity;

import jakarta.persistence.*;
import lombok.Data;


@Entity
//@MappedSuperclass
@Table(name = "files")
@Data
//@NoArgsConstructor
//@AllArgsConstructor
public class FileEntity extends DateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileId;  // 파일 고유 ID (PK)

    private String category;  // 파일 카테고리

    private Long userId;  // 파일을 업로드한 사용자 ID

    private String originalFileName;  // 원본 파일명

    private String storeFilename;  // 저장된 파일명

    private String filePath;  // 파일 저장 경로

    private String companyCode;

}
