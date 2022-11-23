package com.eagle25.practice.springboot.domain.attachment.owner;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class AttachmentOwner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attachmentId;

    private Long ownerPostId;

    @Builder
    public AttachmentOwner(Long attachmentId, Long ownerPostId)
    {
        this.attachmentId = attachmentId;
        this.ownerPostId = ownerPostId;
    }
}
