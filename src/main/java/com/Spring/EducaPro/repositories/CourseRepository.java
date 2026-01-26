package com.Spring.EducaPro.repositories;

import com.Spring.EducaPro.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByCategory(String category);
    List<Course> findByEducationLevel(String educationLevel);
    List<Course> findByInstructorId(Long instructorId);
    
    @Query("SELECT c FROM Course c WHERE c.title LIKE %:searchTerm% OR c.description LIKE %:searchTerm%")
    List<Course> searchByTitleOrDescription(@Param("searchTerm") String searchTerm);
}

