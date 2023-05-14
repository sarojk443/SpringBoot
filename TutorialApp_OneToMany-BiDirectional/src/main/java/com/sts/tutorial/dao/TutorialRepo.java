package com.sts.tutorial.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sts.tutorial.model.Tutorial;

@Repository
public interface TutorialRepo extends JpaRepository<Tutorial, Integer> {
	
	List<Tutorial> findByTitleContaining(String title);
	
	List<Tutorial> findByPublished(boolean isPublished);


}
