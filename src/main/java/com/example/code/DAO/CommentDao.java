package com.example.code.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.example.code.dto.CommentDTO;

@Repository
@Mapper
public interface CommentDao {

    @Select(        " SELECT u.name as UserName,u.avatar as avatar, cm.content as Content , cm.id as CommentID " +
                    " FROM post_comments cm " +
                    " INNER JOIN users u ON u.id = cm.user_id " +
                    " WHERE cm.post_id = #{postID}")
    List<CommentDTO> getComment(int postID);

    @Insert(        " INSERT INTO post_comments(content,user_id,post_id) " +
                    " VALUES (#{content},#{userID},#{id}) ")
    void insertComment(CommentDTO commentDto);

    @Update(        " UPDATE post_comments " +
                    " SET content =  #{content} " +
                    " WHERE id = #{id} AND user_id = #{userID} ")
    void updateComment(CommentDTO commentDto);

    @Delete(        " DELETE FROM post_comments " +
                    " WHERE id = #{id} AND user_id = #{userID} ")
    void deleteComment(CommentDTO commentDto);
}
