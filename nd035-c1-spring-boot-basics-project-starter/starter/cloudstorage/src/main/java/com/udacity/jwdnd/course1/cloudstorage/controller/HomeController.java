package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class HomeController {

    @Autowired
    private FileService fileService;

    @Autowired
    private NoteService noteService;

    @Autowired
    private CredentialService credentialService;

    @GetMapping("/home")
    public String getHomePage(Authentication authentication, Model model){
        String userName = authentication.getName();

        model.addAttribute("listFile", fileService.getFiles(userName));
        model.addAttribute("listNote", noteService.getAllNoteForUser(userName));
        model.addAttribute("ListCredential", credentialService.getAllCredentialForUser(userName));

        return "home";
    }

    @PostMapping("/home/upload")
    public String handleFileUpload(@RequestParam("fileUpload") MultipartFile multipartFile, Authentication authentication, Model model) throws IOException {
        //check file is null
        if(multipartFile.isEmpty()){
            model.addAttribute("error", "File upload is empty.");
            return "result";
        }

        //check file duplicated
        if(fileService.isFileDuplicated(multipartFile.getOriginalFilename(), authentication.getName())){
            model.addAttribute("error", "File is exit.");
            return "result";
        }

        if(fileService.insertFile(multipartFile,authentication.getName())){
            model.addAttribute("success", "File uploaded.");
            return "result";
        }

        model.addAttribute("error", "Sorry! something went wrong while you uploading the file.");
        return "result";
    }

    @GetMapping("/home/view-file")
    public ResponseEntity<byte[]> viewFile(@RequestParam("fileId") Integer fileId){
        File file = fileService.getFileById(fileId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+file.getFileName()+"\"")
                .body(file.getFileData());
    }

    @GetMapping("/home/delete-file")
    public String deleteFile(@RequestParam("fileId") Integer fileId, Model model){
        if(fileService.deleteFile(fileId)){
            model.addAttribute("success", "File deleted.");
            return "result";
        }
        model.addAttribute("error", "Sorry! something went wrong while you deleting the file.");
        return "result";
    }

    @PostMapping("/home/add-update-note")
    public String addOrUpdateNote(Note note, Authentication authentication, Model model){
        if(note.getNoteId() != null) {
            if(noteService.isExitNote(note.getNoteId())) {
                noteService.updateNote(note);
                model.addAttribute("success", "Note updated.");
                return "result";
            } else {
                model.addAttribute("error", "Sorry! Note is not exits");
                return "result";
            }
        }
        if(noteService.insertNote(authentication.getName(), note)) {
            model.addAttribute("success", "Note added");
            return "result";
        };
        model.addAttribute("error", "Sorry! something went wrong while add the note.");
        return "result";
    }

    @GetMapping("/home/delete-note")
    public String deleteNote(@RequestParam("noteId") Integer noteId, Model model){
        if(noteService.deleteNote(noteId)) {
            model.addAttribute("success", "Note deleted.");
            return "result";
        }
        model.addAttribute("error", "Sorry! something went wrong while you deleting the note.");
        return "result";
    }

    @PostMapping("/home/add-update-credential")
    public String addOrUpdateCredential(Credential credential, Authentication authentication, Model model){
        if(credential.getCredentialId() != null) {
            if(credentialService.isExitCredential(credential.getCredentialId())) {
                credentialService.updateCredential(credential);
                model.addAttribute("success", "Credential updated.");
                return "result";
            } else {
                model.addAttribute("error", "Sorry! Credential is not exits");
                return "result";
            }
        }
        if(credentialService.insertCredential(authentication.getName(), credential)) {
            model.addAttribute("success", "Credential added");
            return "result";
        };
        model.addAttribute("error", "Sorry! something went wrong while add the Credential.");
        return "result";
    }

    @GetMapping("/home/delete-credential")
    public String deleteCredential(@RequestParam("credentialId") Integer credentialId, Model model){
        if(credentialService.deleteCredential(credentialId)) {
            model.addAttribute("success", "Credential deleted.");
            return "result";
        }
        model.addAttribute("error", "Sorry! something went wrong while you deleting the credential.");
        return "result";
    }

}
