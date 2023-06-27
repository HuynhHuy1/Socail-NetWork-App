package com.example.code.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import com.example.code.dto.LikeDTO;
import com.example.code.dto.UserDTO;

public interface LikeDao {
    @Select(        " SELECT u.name as name,u.avatar as avatar " +
                    " FROM post_likes l " +
                    " INNER JOIN users u ON u.id = l.user_id " +
                    " WHERE l.post_id = #{postID} ")
    List<UserDTO> getUserLike(int postID);

    @Select(        " SELECT post_id as postID, user_id as userID " +
                    " FROM post_likes " +
                    " WHERE post_id = #{postID} AND user_id = #{userID}")
    LikeDTO getLikeByUserIDAndPostID(int postID,int userID);


    @Insert(        " INSERT INTO post_likes(post_id,user_id) " +
                    " VALUES (#{postID},#{userID}) ")
    void insertLike(LikeDTO likeDTO);

    @Delete(        " DELETE FROM post_likes " +
                    " WHERE post_id = #{postID} and user_id = #{userID} ")
    void deleteLike(int postID, int userID);
}
