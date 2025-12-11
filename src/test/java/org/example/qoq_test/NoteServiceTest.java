package org.example.qoq_test;

import org.example.qoq_test.dto.NoteResponseDTO;
import org.example.qoq_test.dto.SaveNoteDTO;
import org.example.qoq_test.dto.TitleAndDateFindDTO;
import org.example.qoq_test.entity.Note;
import org.example.qoq_test.enumeration.Tag;
import org.example.qoq_test.mapper.NoteDTOMapper;
import org.example.qoq_test.repository.NoteRepository;
import org.example.qoq_test.service.NoteService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NoteServiceTest {

    @InjectMocks
    private NoteService noteService;

    @Mock
    private NoteRepository noteRepository;

    @Mock
    private NoteDTOMapper noteDTOMapper;

    @Test
    void saveNoteTest() {
        // GIVEN
        SaveNoteDTO dto = new SaveNoteDTO("Мій Заголовок", "Мій Текст", Tag.BUSINESS);
        Note note = Note.builder()
                .title("Мій Заголовок")
                .text("Мій Текст")
                .tag(Tag.BUSINESS)
                .createdDate(LocalDateTime.now())
                .build();
        NoteResponseDTO expectedDTO = NoteResponseDTO.builder()
                .title("Мій Заголовок")
                .text("Мій Текст")
                .tag(Tag.BUSINESS)
                .build();

        when(noteRepository.save(any(Note.class))).thenReturn(note);
        when(noteDTOMapper.mapNoteToDTO(any(Note.class))).thenReturn(expectedDTO);

        // WHEN
        NoteResponseDTO result = noteService.saveNote(dto);

        // THEN
        assertEquals(expectedDTO.getTitle(), result.getTitle());
        assertEquals(expectedDTO.getText(), result.getText());
        verify(noteRepository).save(any(Note.class));
        verify(noteDTOMapper).mapNoteToDTO(any(Note.class));
    }

    @Test
    void deleteNoteTest() {
        // GIVEN
        String id = "test-id-123";

        // WHEN
        noteService.deleteNote(id);

        // THEN
        verify(noteRepository).deleteById(id);
    }

    @Test
    void updateNoteTest() {
        // GIVEN
        Note note = new Note();
        note.setId("1");
        note.setTitle("Updated Title");

        NoteResponseDTO expectedDTO = NoteResponseDTO.builder()
                .id("1")
                .title("Updated Title")
                .build();

        when(noteRepository.save(note)).thenReturn(note);
        when(noteDTOMapper.mapNoteToDTO(note)).thenReturn(expectedDTO);

        // WHEN
        NoteResponseDTO result = noteService.updateNote(note);

        // THEN
        assertEquals(expectedDTO, result);
        verify(noteRepository).save(note);
    }

    @Test
    void getNoteTextTest() {
        // GIVEN
        String id = "1";
        String expectedText = "Content of the note";
        Note note = Note.builder().text(expectedText).build();

        when(noteRepository.getTextById(id)).thenReturn(note);

        // WHEN
        String result = noteService.getNoteText(id);

        // THEN
        assertEquals(expectedText, result);
    }

    @Test
    void getNotesByTagTest() {
        // GIVEN
        Tag tag = Tag.BUSINESS;
        Pageable pageable = PageRequest.of(0, 10);
        Note note = new Note();
        Page<Note> notePage = new PageImpl<>(List.of(note));

        NoteResponseDTO responseDTO = new NoteResponseDTO();

        when(noteRepository.findAllByTag(tag, pageable)).thenReturn(notePage);
        when(noteDTOMapper.mapNoteToDTO(note)).thenReturn(responseDTO);

        // WHEN
        Page<NoteResponseDTO> result = noteService.getNotesByTag(tag, pageable);

        // THEN
        assertEquals(1, result.getTotalElements());
        assertEquals(responseDTO, result.getContent().getFirst());
        verify(noteRepository).findAllByTag(tag, pageable);
    }

    @Test
    void getAllDateAndTitleTest() {
        // GIVEN
        Note note = Note.builder().id("100").title("Тест").createdDate(LocalDateTime.now()).text("Секрет").build();
        Page<Note> notePage = new PageImpl<>(List.of(note));
        Pageable pageable = PageRequest.of(0, 10);

        when(noteRepository.getAllDateAndTitle(pageable)).thenReturn(notePage);

        // WHEN
        Page<TitleAndDateFindDTO> result = noteService.getAllDateAndTitle(pageable);

        // THEN
        assertEquals(1, result.getTotalElements());
        TitleAndDateFindDTO dto = result.getContent().getFirst();

        assertEquals("Тест", dto.getTitle());
        assertEquals("100", dto.getId());
    }

    @Test
    void analyzeTextTest() {
        // GIVEN
        String text = "Java Spring Java";

        // WHEN
        Map<String, Integer> result = noteService.analyzeText(text);

        // THEN
        assertEquals(2, result.get("java"));
        assertEquals(1, result.get("spring"));

        String firstKey = result.keySet().iterator().next();
        assertEquals("java", firstKey);
    }
}