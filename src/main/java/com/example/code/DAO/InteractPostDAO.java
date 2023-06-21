package com.example.code.DAO;

import java.util.List;


import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.example.code.DTO.CommentDTO;
import com.example.code.DTO.LikeDTO;

@Mapper
@Repository
public interface InteractPostDAO {
    
    @Select(    " SELECT u.UserName as UserName,u.Avatar as Avatar " +
                " FROM Post_likes l " +
                " INNER JOIN Users u ON u.UserID = l.UserID " +
                " WHERE l.PostID = #{PostID} ")
    List<LikeDTO> getLike(@Param("PostID") int PostID);

    @Insert(    " INSERT INTO Post_likes(PostID,UserID) " +
                " VALUES (#{PostID},#{UserID}) ")
    void insertLike(@Param("PostID") int statusID, @Param("UserID") int userID);

    @Delete(    " DELETE FROM Post_likes " +
                " WHERE PostID = #{StatusID} and UserID = #{UserID} ")
    void deleteLike(@Param("StatusID") int statusID, @Param("UserID") int userID);

    @Select(    " SELECT u.UserName as UserName,u.Avatar as Avatar, cm.Content as Content , cm.CommentID as CommentID " +
                " FROM Post_Comment cm " +
                " INNER JOIN Users u ON u.UserID = cm.UserID " +
                " WHERE cm.PostID = #{postID}" )
    List<CommentDTO> getComment(@Param("postID") int postID);

    @Insert(    " INSERT INTO Post_Comment(Content,userID,PostID) " +
                " VALUES (#{Content},#{userID},#{PostID})")
    void insertComment(@Param("Content") String content, @Param("userID") int userID,@Param("PostID") int postID);
    
    @Update(    " UPDATE Post_Comment " +
                " SET Content =  #{Content} " +    
                " WHERE CommentID = #{CommentID} AND userID = #{userID}" )
    void updateComment(@Param("Content") String content, @Param("CommentID") int commentID,@Param("userID") int userID);

    @Delete(    " DELETE FROM Post_Comment " +
                " WHERE CommentID = #{CommentID} AND userID = #{userID} ")
    void deleteComment(@Param("CommentID") int id, int userID);
}
