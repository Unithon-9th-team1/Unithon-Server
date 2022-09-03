package com.example.springbootboilerplate.upload;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.springbootboilerplate.base.GeneralException;
import com.example.springbootboilerplate.base.constant.Code;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;
import javax.imageio.ImageIO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3FileUploadService {
    // 버킷 이름 동적 할당
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

    public String uploadFile(MultipartFile multipartFile) throws IOException {
        return uploadFile(multipartFile, Optional.empty(), false);
    }

    public String uploadFile(MultipartFile multipartFile, Optional<String> path, boolean saveThumbnail) throws IOException {
        String fileUrl;

        String fileName = createFileName(multipartFile.getOriginalFilename(), path);
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(multipartFile.getSize());
        objectMetadata.setContentType(multipartFile.getContentType());

        try (InputStream inputStream = multipartFile.getInputStream()){
            putS3(fileName, inputStream, objectMetadata);

            if (saveThumbnail) {
                saveThumbnail(multipartFile, fileName, 100);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        fileUrl = amazonS3.getUrl(bucket, fileName).toString();
        System.out.println("fileName: " + fileName);
        System.out.println("fileUrl: " + fileUrl);

        return fileUrl;
    }

    // 파일명 난수화
    private String createFileName(String fileName, Optional<String> path) {
        String uuid = UUID.randomUUID().toString().concat("." + getFileExtension(fileName));

        if (path.isPresent()) {
            return path.get() + "/" + uuid;
        }

        return uuid;
    }

    private String getFileExtension(String fileName) {
        try {
            int pos = fileName.lastIndexOf(".");
            return fileName.substring(pos+1);
        } catch (StringIndexOutOfBoundsException e) {
            e.printStackTrace();
            throw e;
        }
    }

    private void putS3(String fileName, InputStream inputStream, ObjectMetadata objectMetadata) {
        amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
            .withCannedAcl(CannedAccessControlList.PublicRead));
    }

    private void putS3(String fileName, File file) {
        amazonS3.putObject(new PutObjectRequest(bucket, fileName, file)
            .withCannedAcl(CannedAccessControlList.PublicRead));
    }

    private void saveThumbnail(MultipartFile multipartFile, String originFileName, Integer size) {
        try {
            String format = "png";
            File thumbnailImg = resizeImageFile(multipartFile, "png", size);
            putS3(originFileName + ".t" + size + "." + format, thumbnailImg);
            thumbnailImg.delete();
        } catch (Exception e) {
            throw new GeneralException(Code.INTERNAL_ERROR, e);
        }
    }

    private File resizeImageFile(MultipartFile multipartFile, String format, int size) {
        try {
            String tempSavePathName = System.getProperty("user.dir") + "/temp." + format;
            BufferedImage inputImage = ImageIO.read(multipartFile.getInputStream());
            int originWidth = inputImage.getWidth();
            int originHeight = inputImage.getHeight();
            int newWidth, newHeight;
            if (originWidth < originHeight) {
                newWidth = size;
                newHeight = (originHeight * newWidth) / originWidth;
            } else {
                newHeight = size;
                newWidth = (originWidth * newHeight) / originHeight;
            }

            Image resizeImage = inputImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
            BufferedImage newImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
            Graphics graphics = newImage.getGraphics();
            graphics.drawImage(resizeImage, 0, 0, null);
            graphics.dispose();

            File newFile = new File(tempSavePathName);
            ImageIO.write(newImage, format, newFile);
            return newFile;
        } catch (Exception e) {
            throw new GeneralException(Code.INTERNAL_ERROR, e);
        }
    }

}
