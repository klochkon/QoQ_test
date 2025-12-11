package org.example.qoq_test.mapper;

import org.example.qoq_test.dto.NoteResponseDTO;
import org.example.qoq_test.entity.Note;
import org.springframework.stereotype.Component;

@Component
public class NoteDTOMapper {
    public NoteResponseDTO mapNoteToDTO(Note note) {
        return NoteResponseDTO.builder()
                .title(note.getTitle())
                .text(note.getText())
                .id(note.getId())
                .tag(note.getTag())
                .createdDate(note.getCreatedDate())
                .build();
    }
}
