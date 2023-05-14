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
import org.springframework.web.bind.annotation.RestController;

import com.sts.tutorial.dao.CommentRepo;
import com.sts.tutorial.dao.TutorialRepo;
import com.sts.tutorial.exception.ResourceNotFoundException;
import com.sts.tutorial.model.Comment;
import com.sts.tutorial.model.Tutorial;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class CommentController {

	@Autowired
	CommentRepo commentRepo;

	@Autowired
	TutorialRepo tutorialRepo;

	@PostMapping("/tutorials/{tutorialId}/comments")
	public ResponseEntity<Comment> createComment(@PathVariable(value = "tutorialId") int tutorialId,
			@RequestBody Comment commentReq) {
		Comment comment = tutorialRepo.findById(tutorialId).map(tutorial -> {
			tutorial.getComments().add(commentReq);
			return commentRepo.save(commentReq);
		}).orElseThrow(() -> new ResourceNotFoundException("No tutorial found for id = " + tutorialId));
		return new ResponseEntity<>(comment, HttpStatus.CREATED);
	}

	@PutMapping("/comments/{commentId}")
	public ResponseEntity<Comment> updateComment(@PathVariable(value = "commentId") int commentId,
			@RequestBody Comment commentReq) {
		Comment comment = commentRepo.findById(commentId)
				.orElseThrow(() -> new ResourceNotFoundException("Comment not found for id = " + commentId));
		comment.setContent(commentReq.getContent());
		return new ResponseEntity<>(commentRepo.save(comment), HttpStatus.OK);
	}

	@GetMapping("/tutorials/{tutorialId}/comments")
	public ResponseEntity<List<Comment>> getAllCommentByTutorialId(@PathVariable(value = "tutorialId") int tutorialId) {
		Tutorial tutorial = tutorialRepo.findById(tutorialId)
				.orElseThrow(() -> new ResourceNotFoundException("Tutorial not found for id = " + tutorialId));
		List<Comment> commList = new ArrayList<>();
		commList.addAll(tutorial.getComments());
		return new ResponseEntity<>(commList, HttpStatus.OK);
	}

	@GetMapping("/comments/{id}")
	public ResponseEntity<Comment> getCommentById(@PathVariable(value = "id") int id) {
		Comment comment = commentRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Comment not found for id = " + id));

		return new ResponseEntity<>(comment, HttpStatus.OK);
	}

	@DeleteMapping("/comments/{id}")
	public ResponseEntity<HttpStatus> deleteComment(@PathVariable(value = "id") int id) {
		commentRepo.deleteById(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@DeleteMapping("/tutorials/{tutorialId}/comments")
	public ResponseEntity<HttpStatus> deleteAllCommentByTutorialId(@PathVariable(value = "tutorialId") int tutorialId) {
		Tutorial tutorial = tutorialRepo.findById(tutorialId)
				.orElseThrow(() -> new ResourceNotFoundException("No tutorials found for id = " + tutorialId));
		tutorial.removeContent();
		tutorialRepo.save(tutorial);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}
