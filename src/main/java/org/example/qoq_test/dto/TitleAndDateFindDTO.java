package org.example.qoq_test.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
public class TitleAndDateFindDTO {
    private String id;
    private String title;
    private LocalDateTime createdDate;
}
