package com.eagle25.practice.springboot.service.attachments;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import com.eagle25.practice.springboot.domain.attachment.Attachment;
import com.eagle25.practice.springboot.domain.attachment.AttachmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AttachmentsService {

    private final AttachmentRepository _attachmentRepository;

    private AmazonS3 _s3;

    @Value("${cloud.aws.credentials.accessKey}")
    private String _accessKey;

    @Value("${cloud.aws.credentials.secretKey}")
    private String _secretKey;

    @Value("${cloud.aws.s3.bucket}")
    private String _bucketName;

    @Value("${cloud.aws.region.static}")
    private String _region;

    @PostConstruct
    public void setS3Client() {
        AWSCredentials credentials = new BasicAWSCredentials(this._accessKey, this._secretKey);

        _s3 = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(_region)
                .build();
    }

    @Transactional
    public List<Long> uploadAll(Long ownerPostId, List<MultipartFile> files) throws IOException {
        var results = new ArrayList<Long>();

        for(var file : files) {
            results.add(upload(ownerPostId, file));
        }

        return results;
    }

    @Transactional
    public Long upload(Long ownerPostId, MultipartFile file) throws IOException{
        var uniqueFileName =  UUID.randomUUID() + "_" + file.getOriginalFilename();

        _s3.putObject(new PutObjectRequest(_bucketName, uniqueFileName, file.getInputStream(), null)
                .withCannedAcl(CannedAccessControlList.PublicRead));

        return _attachmentRepository.save(Attachment
                        .builder()
                        .uniqueFileName(uniqueFileName)
                        .userFileName(file.getOriginalFilename())
                        .ownerPostId(ownerPostId)
                        .build())
                .getId();
    }

    public ResponseEntity<byte[]> getObject(Long id) throws IOException {
        var attachment = _attachmentRepository
                .getOne(id);

        // UUID가 붙은 파일 이름
        var uniqueFileName = attachment
                .getUniqueFileName();

        // 사용자가 업로드한 파일의 원래 이름
        var userFileName = URLEncoder
                .encode(attachment.getUserFileName(), "UTF-8")
                .replaceAll("\\+", "%20");

        var s3Object = _s3
                .getObject(new GetObjectRequest(_bucketName, uniqueFileName));

        var objectInputStream = s3Object
                .getObjectContent();

        var bytes = IOUtils
                .toByteArray(objectInputStream);

        var httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        httpHeaders.setContentLength(bytes.length);
        httpHeaders.setContentDispositionFormData("attachment", userFileName);

        return new ResponseEntity<>(bytes, httpHeaders, HttpStatus.OK);
    }

    @Transactional
    public Long deleteObject(Long id) {
        var attachment = _attachmentRepository
                .getOne(id);

        _s3.deleteObject(new DeleteObjectRequest(_bucketName, attachment.getUniqueFileName()));

        _attachmentRepository.delete(attachment);

        return id;
    }

    @Transactional
    public int deleteObjectsByPostId(Long postId) {
        var targetAttachments = _attachmentRepository
                .findByOwnerPostId(postId);

        return deleteObjects(targetAttachments);
    }

    @Transactional
    public int deleteObjectsById(Iterable<Long> attachmentIds) {
        var targetAttachments = _attachmentRepository
                .findAllById(attachmentIds);

        return deleteObjects(targetAttachments);
    }

    @Transactional
    public int deleteObjects(Iterable<Attachment> attachments) {
        var count = 0;

        for(var file: attachments) {
            deleteObject(file.getId());
            count += 1;
        }

        _attachmentRepository
                .deleteAll(attachments);

        return count;
    }
}
