package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mappers.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FileService {

    private FileMapper fileMapper;

    private UserService userService;

    public FileService(FileMapper fileMapper, UserService userService) {
        this.fileMapper = fileMapper;
        this.userService = userService;
    }

    public Boolean insertFile(MultipartFile fileUpload, String userName) throws IOException {
        Integer userId = userService.getUserId(userName);

        File file = new File(null,
                        fileUpload.getOriginalFilename(),
                        fileUpload.getContentType(),
                        Long.toString(fileUpload.getSize()),
                        userId,
                        fileUpload.getBytes());

        int row = fileMapper.insertFile(file);

        if(row > 0) {
            return true;
        }
        return false;
    }

    public List<File> getFiles(String userName){
        Integer userId = userService.getUserId(userName);
        return fileMapper.getAllFiles(userId);
    }

    public File getFileById(Integer fileId){
        return fileMapper.getFileById(fileId);
    }

    public boolean deleteFile(Integer fileId){
        int row = fileMapper.deleteFile(fileId);

        if(row > 0) {
            return true;
        }
        return false;
    }

    public boolean isFileDuplicated(String fileName, String userName) {
        Integer userId = userService.getUserId(userName);
        return fileMapper.getFile(fileName, userId) != null;
    }

}
