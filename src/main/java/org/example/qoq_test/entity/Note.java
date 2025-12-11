package org.example.qoq_test.entity;

import lombok.*;
import org.example.qoq_test.enumeration.Tag;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "notes")
public class Note {

    @Id
    private String id;

    private String title;
    private String text;
    private Tag tag;
    private LocalDateTime createdDate;

}
