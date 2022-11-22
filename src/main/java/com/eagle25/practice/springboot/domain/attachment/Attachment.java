package com.eagle25.practice.springboot.domain.attachment;

import com.eagle25.practice.springboot.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Attachment extends BaseTimeEntity {
    @Id
    private String id;

    @Column(length =  100, nullable = false)
    private String fileName;

    @Column(length =  500, nullable = false)
    private String fileUrl;

    @Builder
    public Attachment(String id, String fileName, String fileUrl) {
        this.id = id;
        this.fileName = fileName;
        this.fileUrl = fileUrl;
    }
}
