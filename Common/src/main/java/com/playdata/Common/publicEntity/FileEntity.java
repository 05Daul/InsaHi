package com.playdata.Common.publicEntity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "files")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileId;  // 파일 고유 ID (PK)

    private String category;  // 파일 카테고리

    private Long userId;  // 파일을 업로드한 사용자 ID

    private String originalFileName;  // 원본 파일명

    private String storeFilename;  // 저장된 파일명

    private String filePath;  // 파일 저장 경로

    @ManyToOne
    @JoinColumn(name = "company_code", referencedColumnName = "companyCode", nullable = false)
    private CompanyEntity companyEntity;  // 회사와의 연관 관계

    @Column(name = "upload_datetime", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime uploadDateTime;  // 업로드 일시

    @Column(name = "update_datetime")
    @UpdateTimestamp
    private LocalDateTime updateDateTime;  // 마지막 수정 일시
}
