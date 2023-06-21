package com.example.code.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.code.DAO.InteractPostDAO;
import com.example.code.DTO.CommentDTO;
import com.example.code.DTO.LikeDTO;

@Service 
public class InteractPostService  {
    @Autowired
    InteractPostDAO interactDAO;

    public List<LikeDTO> getLike(int statusID){
        return interactDAO.getLike(statusID);
    }
    public void createILike(int statusID, int userID ){
            interactDAO.insertLike(statusID, userID);
    }
    public void deleteLike(int statusID,int userID){
            interactDAO.deleteLike(statusID, userID);
    }
    public List<CommentDTO> getComment(int statusID){
        return interactDAO.getComment(statusID);
    }
    public void createComment(String content,int userID, int postID){
            interactDAO.insertComment(content, userID, postID);
    }
    public void updateComment(String content, int commentID,int userID){
        interactDAO.updateComment(content,commentID,userID);
    }
    public void deleteComment(int statusID, int userID){
            interactDAO.deleteComment(statusID,userID);
    }



}
