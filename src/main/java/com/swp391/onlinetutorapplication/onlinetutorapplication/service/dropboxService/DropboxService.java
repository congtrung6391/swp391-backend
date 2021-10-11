package com.swp391.onlinetutorapplication.onlinetutorapplication.service.dropboxService;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.files.WriteMode;
import com.dropbox.core.v2.sharing.CreateSharedLinkWithSettingsErrorException;
import com.dropbox.core.v2.sharing.GetSharedLinkFileErrorException;
import com.dropbox.core.v2.sharing.ListSharedLinksResult;
import com.dropbox.core.v2.sharing.SharedLinkMetadata;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swp391.onlinetutorapplication.onlinetutorapplication.model.courses.CourseMaterial;
import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.courseResponse.MaterialCreationResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.repository.course.CourseMaterialRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class DropboxService {
    @Autowired
    private DropboxClient dropboxClient;

    @Autowired
    private CourseMaterialRepository courseMaterialRepository;

    public void createFolder(String courseId, String materialId) throws DbxException {
        dropboxClient.dropboxClient().files().createFolderV2("/Course/"+courseId+"/"+materialId);
    }


    public Object uploadFile(MultipartFile file, String courseId, String materialId) throws IOException, DbxException {
        try {

            // Đọc file bằng Byte Aray Input Stream
            ByteArrayInputStream inputStream = new ByteArrayInputStream(file.getBytes());

            //Dùng FileMetadata của dropbox Api để upload thông ra link :
            // SWP/Cousre/:CourseID/Material/MaterialId
            FileMetadata uploadMetaData = dropboxClient.dropboxClient().files().
                    uploadBuilder("/Course/" + courseId + "/" + materialId + "/" + file.getOriginalFilename()).uploadAndFinish(file.getInputStream());

            //Sau khi upload thì tạo luôn link shareing bằng ShareLinkMetadata
            SharedLinkMetadata sharedLinkMetadata = dropboxClient.dropboxClient().sharing().
                    createSharedLinkWithSettings("/Course/" + courseId + "/" + materialId + "/" + file.getOriginalFilename());
            log.info("upload meta data =====> {}", uploadMetaData.toString());
            inputStream.close();

            // Lấy thông tin của material, gán vào material response.
            CourseMaterial material = courseMaterialRepository.getById(Long.parseLong(materialId));

            material.setLinkShare(sharedLinkMetadata.getUrl()); //Set link sharing cho model Course Material
            courseMaterialRepository.save(material); //Lưu vào database

            //Gán thông tin vào material response
            //Response trả về dạng : title, description, tên file, link file
            MaterialCreationResponse response =
                    new MaterialCreationResponse(material.getTitle(), material.getDescription(), material.getFileAttach(), sharedLinkMetadata.getUrl(), true);

            return response;
        } catch (CreateSharedLinkWithSettingsErrorException ex) {
            log.error("Error at CreateSharedLinkWithSettingsErrorException : " + ex.getMessage());
        } catch (DbxException ex) {
            log.error("Error at upload file to dropbox at dropbox service : " + ex.getMessage());
        }
        return null;
    }

    public Object uploadOverwrittenFile(MultipartFile file, String courseId, String materialId) throws IOException, DbxException {
        try {

            //Kiểm tra nếu tutor update file trùng tên thì ghi đè lên file cũ, vẫn giữ link sharing
            //Kiểm tra nếu tutor update file mà khác tên thì xóa luôn folder chưa materialId, tạo lại folder Id cũ những bên trong rỗng
            Boolean fileNameExisted = duplicateFileName(courseId, materialId, file.getOriginalFilename());
            ByteArrayInputStream inputStream = new ByteArrayInputStream(file.getBytes());

            FileMetadata uploadMetaData;
            SharedLinkMetadata sharedLinkMetadata = null;

            if (!fileNameExisted) { // Fie Attach không trùng tên file cũ
                //Xóa file cũ
                deleteMaterialFolderId("/Course/" + courseId + "/" + materialId );
                //upload file mới
                uploadMetaData = dropboxClient.dropboxClient().files().
                        uploadBuilder("/Course/" + courseId + "/" + materialId + "/" + file.getOriginalFilename())
                        .uploadAndFinish(file.getInputStream());
                // Tạo link sharing mới
                sharedLinkMetadata = dropboxClient.dropboxClient().sharing().
                        createSharedLinkWithSettings("/Course/" + courseId + "/" + materialId + "/" + file.getOriginalFilename());
            } else {

                //Ghi đè
                uploadMetaData = dropboxClient.dropboxClient().files().
                        uploadBuilder("/Course/" + courseId + "/" + materialId + "/" + file.getOriginalFilename())
                        .withMode(WriteMode.OVERWRITE).uploadAndFinish(file.getInputStream());
                String linkSharing = getShareLink(courseId, materialId, file.getOriginalFilename());

                //Giữ link sharing cũ
                sharedLinkMetadata = dropboxClient.dropboxClient().sharing().getSharedLinkMetadata(linkSharing);
            }

            // Lấy thông tin của material, gán vào material response.
            CourseMaterial material = courseMaterialRepository.getById(Long.parseLong(materialId));

            material.setLinkShare(sharedLinkMetadata.getUrl()); //Set link sharing cho model Course Material
            courseMaterialRepository.save(material); //Lưu vào database

            log.info("upload meta data =====> {}", uploadMetaData.toString());
            inputStream.close();

            //Gán thông tin vào material response
            //Response trả về dạng : title, description, tên file, link file
            MaterialCreationResponse response =
                    new MaterialCreationResponse(material.getId(),material.getTitle(), material.getDescription(), material.getFileAttach(), sharedLinkMetadata.getUrl(), true);

            return response;
        } catch (CreateSharedLinkWithSettingsErrorException ex) {
            log.error("Error at CreateSharedLinkWithSettingsErrorException : " + ex.getMessage());
        } catch (DbxException ex) {
            log.error("Error at upload file to dropbox at dropbox service : " + ex.getMessage());
        }
        return null;
    }


    public List<Map<String, Object>> getFileList(String courseId, String materialId) throws IOException, DbxException {
        ListFolderResult entries = dropboxClient.dropboxClient().files().listFolder("/Course/" + courseId + "/" + materialId + "/");
        List<Map<String, Object>> result = new ArrayList<>();

        while (true) {
            for (Metadata metadata : entries.getEntries()) {
                if (metadata instanceof FileMetadata) {
                    log.info("{} is file " + metadata.getName());
                }
                String metadataString = metadata.toString();
                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> map = new HashMap<>();
                map = mapper.readValue(metadataString, new TypeReference<Map<String, Object>>() {
                });
                result.add(map);
            }
            if (!entries.getHasMore()) {
                break;
            }
            entries = dropboxClient.dropboxClient().files().listFolderContinue(entries.getCursor());
        }
        return result;
    }

    //Lấy link sharing của file trong dropbox
    public String getShareLink(String courseId, String materialId, String fileName) {
        try {
            ListSharedLinksResult sharedLinkMetadata = dropboxClient.dropboxClient().sharing().listSharedLinksBuilder()
                    .withPath("/Course/" + courseId + "/" + materialId + "/" + fileName).withDirectOnly(true).start();
            for (SharedLinkMetadata linkMetadata : sharedLinkMetadata.getLinks()) {
                return linkMetadata.getUrl();
            }
        } catch (GetSharedLinkFileErrorException ex) {
            log.error("Error at GetSharedLinkFileErrorException : " + ex.getMessage());
        } catch (DbxException ex) {
            log.error("Error at get share link in dropbox service : " + ex.getMessage());
        }
        return null;
    }


    //Kiểm tra nếu trùng tên thì ghi đè
    public Boolean duplicateFileName(String courseId, String materialId, String fileName) throws DbxException {
        ListFolderResult entries = dropboxClient.dropboxClient().files().listFolder("/Course/" + courseId + "/" + materialId + "/");
        for (Metadata metadata : entries.getEntries()) {
            if (metadata.getName().equals(fileName))
                return true;
        }
        return false;
    }

    // xóa folder
    public void deleteMaterialFolderId(String path) throws DbxException {
        dropboxClient.dropboxClient().files().delete(path);
    }

}
