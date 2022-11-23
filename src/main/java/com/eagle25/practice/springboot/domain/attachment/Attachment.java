package com.eagle25.practice.springboot.domain.attachment;

import com.eagle25.practice.springboot.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Primary;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Attachment extends BaseTimeEntity {
    @Id
    private String id;

    @Column(length =  100, nullable = false)
    private String uniqueFileName;

    @Column(length =  100, nullable = false)
    private String userFileName;

    @Builder
    public Attachment(String id, String uniqueFileName, String userFileName) {
        this.id = id;
        this.uniqueFileName = uniqueFileName;;
        this.userFileName = userFileName;;
    }
}
