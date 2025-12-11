package org.example.qoq_test.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.qoq_test.enumeration.Tag;

@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
public class SaveNoteDTO {

    @NotBlank
    private String title;
    @NotBlank
    private String text;
    private Tag tag;




}
