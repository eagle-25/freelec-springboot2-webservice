package com.eagle25.practice.springboot.domain.attachment.owner;

import com.eagle25.practice.springboot.domain.posts.Posts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AttachmentOwnerRepository extends JpaRepository<AttachmentOwner, Long> {
    List<AttachmentOwner> findByOwnerPostId(Long ownerPostId);
}
