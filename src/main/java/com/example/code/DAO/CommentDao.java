package com.example.code.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.example.code.dto.CommentDTO;
import com.example.code.dto.UserDTO;

@Repository
@Mapper
public interface CommentDao {

    @Select(        " SELECT u.name as userName,u.avatar as avatar, cm.content as content , cm.id as id, cm.post_id postId,cm.user_id as userID " +
                    " FROM post_comments cm " +
                    " INNER JOIN users u ON u.id = cm.user_id " +
                    " WHERE cm.post_id = #{postID}")
    List<CommentDTO> getComment(int postID);

    @Select(        " SELECT user_id as id " +
                    " FROM post_comments " +
                    " WHERE id = #{commentID} ")
    UserDTO getUserIDByCommentID(int commentID);

    @Insert(        " INSERT INTO post_comments(content,user_id,post_id) " +
                    " VALUES (#{content},#{userId},#{postId}) ")
    void insertComment(CommentDTO commentDto);

    @Update(        " UPDATE post_comments " +
                    " SET content =  #{content} " +
                    " WHERE id = #{id} AND user_id = #{userId} ")
    int updateComment(CommentDTO commentDto);

    @Delete(        " DELETE FROM post_comments " +
                    " WHERE id = #{id} AND user_id = #{userId} ")
    void deleteComment(CommentDTO commentDto);
}
