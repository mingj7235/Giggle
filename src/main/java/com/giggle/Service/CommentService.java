package com.giggle.Service;

import com.giggle.Domain.Entity.Post;
import com.giggle.Domain.Form.CreateCommentForm;
import com.giggle.Repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.giggle.Domain.Entity.Comment;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostService postService;

    @Transactional
    public void createComment(CreateCommentForm createCommentForm){
        Comment newComment = new Comment();
        long postId = Long.parseLong(createCommentForm.getPostId());
        Post post = postService.findById(postId);

        if(createCommentForm.getCommentId()== null){
            newComment.setLevel(1);
        }

        else{
            long supCommentId = Long.parseLong(createCommentForm.getCommentId());
            Comment supComment = commentRepository.findById(supCommentId);
            newComment.setLevel(supComment.getLevel()+1);
            newComment.setSuperComment(supComment);
            supComment.getSubComment().add(newComment);
        }

        newComment.setContent(createCommentForm.getContent());
        newComment.setPost(post);
        newComment.setWriter("Tester");

        commentRepository.save(newComment);
    }

    @Transactional
    public void deleteComment(long commentId){
        Comment comment = commentRepository.findById(commentId);

        Comment superComment = comment.getSuperComment();
        while(superComment != null){
            superComment.getSubComment().remove(comment);
            superComment = superComment.getSuperComment();
        }

        commentRepository.deleteById(commentId);
    }


    public Comment findById(long id){
        return commentRepository.findById(id);
    }

    @Transactional
    public void editComment(long id, String content){
        Comment comment = commentRepository.findById(id);
        comment.setContent(content);

        commentRepository.editComment(comment);
    }
}