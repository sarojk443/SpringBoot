package com.sts.tutorial.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sts.tutorial.model.Comment;

@Repository
public interface CommentRepo extends JpaRepository<Comment, Integer>{

}
