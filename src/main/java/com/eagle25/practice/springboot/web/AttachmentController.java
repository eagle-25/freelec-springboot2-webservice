package com.eagle25.practice.springboot.web;

import com.eagle25.practice.springboot.service.attachments.AttachmentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;

@RequiredArgsConstructor
@Controller
public class AttachmentController {

    private final AttachmentsService attachmentsService;

    @GetMapping("/attachments/{id}")
    public ResponseEntity<byte[]> downloadObject(@PathVariable String id) throws IOException {
        return attachmentsService
                .getObject(id);
    }
}