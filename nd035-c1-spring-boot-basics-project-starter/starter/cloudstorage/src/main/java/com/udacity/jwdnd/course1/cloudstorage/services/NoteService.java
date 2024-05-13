package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mappers.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    private UserService userService;
    private NoteMapper noteMapper;

    public NoteService(UserService userService, NoteMapper noteMapper) {
        this.userService = userService;
        this.noteMapper = noteMapper;
    }

    public List<Note> getAllNoteForUser(String username){
        Integer userId = userService.getUserId(username);
        return noteMapper.getAllNoteByUserId(userId);
    }

//    public void addNewOrUpdateNote(String username, Note note) {
//        Integer userId = userService.getUserId(username);
//        note.setUserId(userId);
//        try {
//            if (isExitNote(note.getNoteId())){
//                noteMapper.updateNote(note);
//                model.addAttribute("success", true);
//            } else {
//                int newNote = noteMapper.insertNote(note);
//                if (newNote > 0) {
//                    model.addAttribute("success", true);
//                } else {
//                    model.addAttribute("resultError", "Sorry! something went wrong while you add new note.");
//                }
//            }
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//    }

    public void updateNote(Note note){
        noteMapper.updateNote(note);
    }

    public boolean insertNote(String username, Note note){
        Integer userId = userService.getUserId(username);
        note.setUserId(userId);
        return noteMapper.insertNote(note) > 0;
    }

    public boolean isExitNote(Integer noteId){
        return noteMapper.getNoteById(noteId) != null;
    }

    public boolean deleteNote(Integer noteId){
        int row = noteMapper.deleteNote(noteId);
        if(row > 0) {
            return true;
        }
        return false;
    }

}
