package heart_link.application.files;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileUpload<T> {
    List<T> upload(List<MultipartFile> file);
}
