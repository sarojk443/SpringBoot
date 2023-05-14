package com.sts.tutorial.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sts.tutorial.dao.TutorialRepo;
import com.sts.tutorial.exception.ResourceNotFoundException;
import com.sts.tutorial.model.Tutorial;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class TutorialController {

	@Autowired
	TutorialRepo tutorialRepo;

	@PostMapping("/tutorials")
	public ResponseEntity<Tutorial> createTutorial(@RequestBody Tutorial tutorial) {
		Tutorial _tutorial = tutorialRepo.save(tutorial);
		return new ResponseEntity<>(_tutorial, HttpStatus.CREATED);
	}

	@GetMapping("/tutorials")
	public ResponseEntity<List<Tutorial>> fetchAllTutorials(@RequestParam(required = false) String title) {

		List<Tutorial> tuList = new ArrayList<>();

		if (title == null)
			tutorialRepo.findAll().forEach(tuList::add);
		else
			tutorialRepo.findByTitleContaining(title).forEach(tuList::add);

		if (tuList.isEmpty())
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);

		return new ResponseEntity<>(tuList, HttpStatus.OK);

	}

	@GetMapping("/tutorials/{id}")
	public ResponseEntity<Tutorial> getTutorialByID(@PathVariable("id") int id) {
		Tutorial tutorial = tutorialRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No resource found with id = " + id));
		return new ResponseEntity<Tutorial>(tutorial, HttpStatus.OK);
	}

	@PutMapping("/tutorials/{id}")
	public ResponseEntity<Tutorial> updateTutorial(@PathVariable("id") int id, @RequestBody Tutorial tutorial) {

		Tutorial _tutorial = tutorialRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No resource found with id =" + id));

		_tutorial.setTitle(tutorial.getTitle());
		_tutorial.setPublished(tutorial.isPublished());
		_tutorial.setDescription(tutorial.getDescription());

		return new ResponseEntity<>(tutorialRepo.save(_tutorial), HttpStatus.OK);

	}

	@DeleteMapping("/tutorials/{id}")
	public ResponseEntity<HttpStatus> deleteTutorial(@PathVariable("id") int id) {
		tutorialRepo.deleteById(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@DeleteMapping("/tutorials")
	public ResponseEntity<HttpStatus> deleteAllTutorial() {
		tutorialRepo.deleteAll();
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@GetMapping("/tutorials/published")
	public ResponseEntity<List<Tutorial>> findByPublished() {
		List<Tutorial> tutorials = tutorialRepo.findByPublished(true);

		if (tutorials.isEmpty())
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);

		return new ResponseEntity<>(tutorials, HttpStatus.NO_CONTENT);
	}
}
