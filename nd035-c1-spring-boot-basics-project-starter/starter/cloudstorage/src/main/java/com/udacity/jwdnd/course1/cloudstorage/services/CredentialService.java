package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mappers.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CredentialService {

    private UserService userService;

    private CredentialMapper credentialMapper;

    private EncryptionService encryptionService;

    public CredentialService(UserService userService, CredentialMapper credentialMapper, EncryptionService encryptionService) {
        this.userService = userService;
        this.credentialMapper = credentialMapper;
        this.encryptionService = encryptionService;
    }

    public List<Credential> getAllCredentialForUser(String userName) {

        Integer userId = userService.getUserId ( userName );

        List<Credential> listCredential = credentialMapper.getAllCredentialByUserId(userId);

        for ( Credential credential: listCredential) {
            credential.setPassword (decryptedPassword(credential));
        }

        return listCredential;
    }

    public void updateCredential(Credential credential) {
        credential.setKey(encryptionService.generateSalt());
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), credential.getKey());
        credential.setPassword(encryptedPassword);
        System.out.println(credential.getPassword());
        credentialMapper.updateCredential(credential);
    }

    public boolean insertCredential(String username, Credential credential){
        Integer userId = userService.getUserId(username);
        credential.setUserId(userId);
        credential.setKey(encryptionService.generateSalt());
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), credential.getKey());
        credential.setPassword(encryptedPassword);
        return credentialMapper.insertCredential(credential) > 0;
    }

    public boolean isExitCredential(Integer credentialId){
        return credentialMapper.getCredentialById(credentialId) != null;
    }

    public boolean deleteCredential(Integer credentialId) {
        int row = credentialMapper.deleteCredential(credentialId);

        if ( row > 0) {
            return true;
        }
        return false;
    }

    public String decryptedPassword(Credential credential) {
        String decryptedPassword = encryptionService.decryptValue(credential.getPassword(), credential.getKey());
        return decryptedPassword;
    }

}
