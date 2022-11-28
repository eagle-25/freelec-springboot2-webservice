package com.eagle25.practice.springboot.web;

import com.eagle25.practice.springboot.service.attachments.AttachmentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class AttachmentController {

    private final AttachmentsService attachmentsService;


    @GetMapping("/api/v1/attachment/{id}")
    public ResponseEntity<byte[]> getObject(@PathVariable Long id) throws IOException {
        return attachmentsService
                .getObject(id);
    }

    @DeleteMapping("/api/v1/attachment/{id}")
    public Long deleteObject(@PathVariable Long id) throws IOException {
        return attachmentsService
                .deleteObject(id);
    }
}
