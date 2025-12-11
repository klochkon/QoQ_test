package org.example.qoq_test.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.qoq_test.enumeration.Tag;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
public class NoteResponseDTO {

    private String id;

    private String title;
    private String text;
    private Tag tag;
    private LocalDateTime createdDate;
}
