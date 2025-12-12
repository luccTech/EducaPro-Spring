package com.Spring.EducaPro.repositories;

import com.Spring.EducaPro.entities.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findByCourseId(Long courseId);
    List<Lesson> findByCourseIdOrderByLessonOrderAsc(Long courseId);
}

