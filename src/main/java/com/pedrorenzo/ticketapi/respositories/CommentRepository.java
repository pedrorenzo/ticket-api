package com.pedrorenzo.ticketapi.respositories;

import com.pedrorenzo.ticketapi.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

}
