package org.example.qoq_test.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.qoq_test.dto.NoteResponseDTO;
import org.example.qoq_test.dto.SaveNoteDTO;
import org.example.qoq_test.dto.TitleAndDateFindDTO;
import org.example.qoq_test.entity.Note;
import org.example.qoq_test.enumeration.Tag;
import org.example.qoq_test.mapper.NoteDTOMapper;
import org.example.qoq_test.repository.NoteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NoteService {

    private final NoteRepository noteRepository;
    private final NoteDTOMapper mapper;

    public void deleteNote(String id) {
        log.info("Deleting note with id: {}", id);
        noteRepository.deleteById(id);
    }

    public NoteResponseDTO updateNote(Note note) {
        log.info("Updating note with id: {}", note.getId());
        return mapper.mapNoteToDTO(noteRepository.save(note));
    }

    public NoteResponseDTO saveNote(SaveNoteDTO noteDTO) {
        log.info("Saving new note with title: '{}'", noteDTO.getTitle());
        Note note = Note.builder()
                .tag(noteDTO.getTag())
                .createdDate(LocalDateTime.now())
                .text(noteDTO.getText())
                .title(noteDTO.getTitle())
                .build();
        return mapper.mapNoteToDTO(noteRepository.save(note));
    }

    public String getNoteText(String id) {
        log.info("Fetching text for note id: {}", id);
        return noteRepository.getTextById(id).getText();
    }

    public Page<NoteResponseDTO> getNotesByTag(Tag tag, Pageable pageable) {
        log.info("Fetching notes by tag: {}", tag);
        return noteRepository.findAllByTag(tag, pageable).map(mapper::mapNoteToDTO);
    }

    public Page<TitleAndDateFindDTO> getAllDateAndTitle(Pageable pageable) {
        log.info("Fetching all notes (title and date). Page: {}, Size: {}", pageable.getPageNumber(), pageable.getPageSize());
        Page<Note> notePage = noteRepository.getAllDateAndTitle(pageable);
        return notePage.map(note -> TitleAndDateFindDTO.builder()
                .id(note.getId())
                .title(note.getTitle())
                .createdDate(note.getCreatedDate())
                .build()
        );
    }

    public Map<String, Integer> analyzeText(String text) {
        List<String> wordList = Arrays.stream(text.toLowerCase().split("\\W+")).toList();

        return wordList.stream()
                .filter(word -> !word.isEmpty())
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.summingInt(element -> 1)
                ))
                .entrySet()
                .stream()
                .sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldVal, newVal) -> oldVal,
                        LinkedHashMap::new
                ));
    }
}