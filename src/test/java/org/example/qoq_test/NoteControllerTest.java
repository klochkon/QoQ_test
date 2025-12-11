package org.example.qoq_test;


import org.example.qoq_test.controller.NoteController;
import org.example.qoq_test.dto.NoteResponseDTO;
import org.example.qoq_test.dto.SaveNoteDTO;
import org.example.qoq_test.dto.TitleAndDateFindDTO;
import org.example.qoq_test.entity.Note;
import org.example.qoq_test.enumeration.Tag;
import org.example.qoq_test.service.NoteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;


import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NoteController.class)
public class NoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private NoteService noteService;

    @Test
    void getAllTitleAndDateTest() throws Exception {
        // GIVEN
        TitleAndDateFindDTO dto = TitleAndDateFindDTO.builder().id("1").title("Test").build();
        Page<TitleAndDateFindDTO> page = new PageImpl<>(List.of(dto));

        when(noteService.getAllDateAndTitle(any(Pageable.class))).thenReturn(page);

        // WHEN & THEN
        mockMvc.perform(get("/api/v1/note")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content[0].title").value("Test"))
                .andExpect(jsonPath("$.content[0].id").value("1"))
                .andExpect(jsonPath("$.content[0].title").value("Test"));
    }

    @Test
    void saveNoteTest() throws Exception {
        // GIVEN
        SaveNoteDTO inputDto = new SaveNoteDTO("New Note", "Content", Tag.BUSINESS);

        NoteResponseDTO responseDTO = NoteResponseDTO.builder().id("123").title("New Note").build();

        when(noteService.saveNote(any(SaveNoteDTO.class))).thenReturn(responseDTO);

        // WHEN & THEN
        mockMvc.perform(post("/api/v1/note")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("123"))
                .andExpect(jsonPath("$.title").value("New Note"));
    }


    @Test
    void deleteNoteTest() throws Exception {
        // WHEN & THEN
        mockMvc.perform(delete("/api/v1/note/{id}", "123"))
                .andExpect(status().isNoContent());

        verify(noteService).deleteNote("123");
    }


    @Test
    void updateNoteTest() throws Exception {
        // GIVEN
        Note noteInput = Note.builder().id("1").title("Updated").build();
        NoteResponseDTO responseDTO = NoteResponseDTO.builder().id("1").title("Updated").build();

        when(noteService.updateNote(any(Note.class))).thenReturn(responseDTO);

        // WHEN & THEN
        mockMvc.perform(put("/api/v1/note")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(noteInput)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated"));
    }


    @Test
    void getNotesByTagTest() throws Exception {
        // GIVEN
        NoteResponseDTO noteDTO = NoteResponseDTO.builder()
                .id("55")
                .title("title")
                .text("Review Q3 financials")
                .tag(Tag.BUSINESS)
                .build();

        Page<NoteResponseDTO> page = new PageImpl<>(List.of(noteDTO));

        when(noteService.getNotesByTag(eq(Tag.BUSINESS), any(Pageable.class)))
                .thenReturn(page);

        // WHEN & THEN
        mockMvc.perform(get("/api/v1/note/filter")
                        .param("tag", "BUSINESS")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content[0].title").value("title"))
                .andExpect(jsonPath("$.content[0].tag").value("BUSINESS"));
    }


    @Test
    void analyzeTextTest() throws Exception {
        // GIVEN
        Map<String, Integer> mockResult = Map.of("hello", 5);
        when(noteService.analyzeText(any(String.class))).thenReturn(mockResult);

        // WHEN & THEN
        mockMvc.perform(post("/api/v1/note/analyze")
                        .content("hello world text")
                        .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hello").value(5));
    }


    @Test
    void getNoteTextTest() throws Exception {
        // GIVEN
        String expectedText = "Some content";
        when(noteService.getNoteText("123")).thenReturn(expectedText);

        // WHEN & THEN
        mockMvc.perform(get("/api/v1/note/123/text")) //
                .andExpect(status().isOk())
                .andExpect(content().string(expectedText));
    }
}

