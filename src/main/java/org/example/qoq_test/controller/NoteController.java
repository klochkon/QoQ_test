package org.example.qoq_test.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.qoq_test.dto.NoteResponseDTO;
import org.example.qoq_test.dto.SaveNoteDTO;
import org.example.qoq_test.dto.TitleAndDateFindDTO;
import org.example.qoq_test.entity.Note;
import org.example.qoq_test.enumeration.Tag;
import org.example.qoq_test.service.NoteService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/note")
@RequiredArgsConstructor
@Slf4j
public class NoteController {

    private final NoteService noteService;

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable String id) {
        log.info("Request to delete note with id: {}", id);
        noteService.deleteNote(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("filter")
    public ResponseEntity<Page<NoteResponseDTO>> getNotesByTag(
            @RequestParam Tag tag,
            @PageableDefault(sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("Request to filter notes by tag: {}", tag);
        return ResponseEntity.ok(noteService.getNotesByTag(tag, pageable));
    }

    @PutMapping
    public ResponseEntity<NoteResponseDTO> updateNote(@RequestBody Note note) {
        log.info("Request to update note with id: {}", note.getId());
        NoteResponseDTO updatedNote = noteService.updateNote(note);
        return ResponseEntity.ok(updatedNote);
    }

    @PostMapping
    public ResponseEntity<NoteResponseDTO> saveNote(@Valid @RequestBody SaveNoteDTO noteDTO) {
        log.info("Request to save note with title: '{}'", noteDTO.getTitle());
        return ResponseEntity.status(HttpStatus.CREATED).body(noteService.saveNote(noteDTO));
    }

    @GetMapping
    public ResponseEntity<Page<TitleAndDateFindDTO>> getAllTitleAndDate(
            @PageableDefault(sort = "createdDate",
                    direction = Sort.Direction.DESC
            ) Pageable pageable) {
        log.info("Request to get all notes (titles and dates)");
        return ResponseEntity.ok(noteService.getAllDateAndTitle(pageable));
    }

    @PostMapping("analyze")
    public ResponseEntity<Map<String, Integer>> analyzeText(@RequestBody String text) {
        log.info("Request to analyze text");
        return ResponseEntity.ok(noteService.analyzeText(text));
    }

    @GetMapping("{id}/text")
    public ResponseEntity<String> getNoteText(@PathVariable String id) {
        log.info("Request to get text for note id: {}", id);
        return ResponseEntity.ok(noteService.getNoteText(id));
    }
}