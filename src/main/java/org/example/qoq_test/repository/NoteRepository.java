package org.example.qoq_test.repository;

import org.example.qoq_test.entity.Note;
import org.example.qoq_test.enumeration.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteRepository extends MongoRepository<Note,String> {

    @Query(value = "{'_id' : ?0}", fields = "{'text' : 1}")
    Note getTextById(String id);

    Page<Note> findAllByTag(Tag tag, Pageable pageable);

    @Query(value = "{}", fields ="{'title' :  1, 'createdDate' : 1, '_id' : 1}")
    Page<Note> getAllDateAndTitle (Pageable pageable);

}
