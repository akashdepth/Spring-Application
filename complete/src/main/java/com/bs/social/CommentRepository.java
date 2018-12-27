package com.bs.social;

/**
 * Created by personal on 10/29/18.
 */

import com.bs.social.models.Comment;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface CommentRepository extends CrudRepository<Comment, Integer> {
    List<Comment> findAll();
    List<Comment> findByContentSectionIdOrderByTimestampDesc(Long id);
}
