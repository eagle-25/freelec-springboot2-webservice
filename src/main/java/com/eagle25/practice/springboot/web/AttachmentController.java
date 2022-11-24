package com.eagle25.practice.springboot.web;

import com.eagle25.practice.springboot.service.attachments.AttachmentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class AttachmentController {

    private final AttachmentsService attachmentsService;

    @GetMapping("/attachment/{id}")
    public ResponseEntity<byte[]> downloadObject(@PathVariable Long id) throws IOException {
        return attachmentsService
                .getObject(id);
    }

    @DeleteMapping("/attachment/{id}")
    public Long deleteObject(@PathVariable Long id) throws IOException {
        return attachmentsService
                .deleteObject(id);
    }
}
