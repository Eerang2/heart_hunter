package heart_link.application.files.impl;

import heart_link.application.files.FileUpload;
import heart_link.application.member.enums.ImageType;
import heart_link.application.member.repository.entity.ProfileImageEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class LocalFileUploader implements FileUpload<ProfileImageEntity> {

    private final String UPLOAD_DIR;
    private static final String DB_URL_PREFIX = "https://fake-bucket.s3.amazonaws.com/";

    public LocalFileUploader(@Value("${profile.image.url}")String UPLOAD_DIR) {
        this.UPLOAD_DIR = UPLOAD_DIR;
    }

    public List<ProfileImageEntity> upload(List<MultipartFile> files) {
        List<ProfileImageEntity> uploadedUrls = new ArrayList<>();

        File saveDir = new File(UPLOAD_DIR);
        if (!saveDir.exists() && !saveDir.mkdirs()) {
            throw new RuntimeException("디렉토리 생성 실패: " + UPLOAD_DIR);
        }

        for (MultipartFile file : files) {
            if (file.isEmpty()) continue;

            String uniqueFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            File destination = new File(saveDir, uniqueFileName);

            try {
                file.transferTo(destination);
            } catch (IOException e) {
                throw new RuntimeException("파일 저장 중 오류 발생", e);
            }

            String savedUrl = DB_URL_PREFIX + uniqueFileName;
            ImageType imageType = ImageType.fromMimeType(file.getContentType());
            uploadedUrls.add(ProfileImageEntity.of(savedUrl, imageType));
        }

        return uploadedUrls;
    }
}
