package com.example.code.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.example.code.dto.LikeDTO;
import com.example.code.dto.PostDTO;

@Repository
@Mapper
public interface PostDao {
        @Select(        " SELECT  p.id as PostID, u.name as UserName, p.content as Content , cm.CommentNumber as CommentCount , l.LikeNumber as LikeCount, p.create_at as TimeCreate "+
                        " FROM posts p " +
                        " INNER JOIN friendships f ON f.user2_id = p.user_id " +
                        " INNER JOIN users u ON u.id = p.user_id " +
                        " LEFT JOIN ( " +
                                " SELECT post_id, COUNT(post_id) as LikeNumber " +
                                " FROM post_likes " +
                                " GROUP BY post_id " +
                        " ) l ON p.id = l.post_id " +
                        " LEFT JOIN( " +
                                " SELECT post_id, COUNT(post_id) as CommentNumber " +
                                " FROM post_comments " +
                                " GROUP BY post_id " +
                        " ) cm ON cm.post_id = p.id " +
                        "       WHERE f.user1_id = #{id} AND f.status = 1 " +
                        " UNION " +
                        " SELECT  p.id as PostID, u.name as UserName, p.content as Content , cm.CommentNumber as CommentCount , l.LikeNumber as LikeCount,p.create_at as TimeCreate  " +
                                " FROM posts p " +
                        " INNER JOIN friendships f ON f.user1_id = p.user_id " +
                        " INNER JOIN users u ON u.id = p.user_id " +
                        " LEFT JOIN ( " +
                                " SELECT post_id, COUNT(post_id) LikeNumber " +
                                " FROM post_likes " +
                        " GROUP BY post_id " +
                        " ) l ON p.id = l.post_id " +
                        " LEFT JOIN( " +
                                " SELECT post_id, COUNT(post_id) CommentNumber " +
                                " FROM post_comments " +
                                " GROUP BY post_id " +
                        " ) cm ON cm.post_id = p.id " +
                        " WHERE f.user2_id = #{id} AND f.Status = 1 ")
        List<PostDTO> getPostFriend(int userID);


        @Select (
                " SELECT  p.id as PostID,u.avatar as avatar , u.name as UserName, p.user_id as UserID , p.content as Content , cm.CommentNumber as CommentCount , l.LikeNumber as LikeCount, p.create_at as TimeCreate "+
                " FROM posts p " +
                " INNER JOIN friendships f ON f.user2_id = p.user_id " +
                " INNER JOIN users u ON u.id = p.user_id " +
                " LEFT JOIN ( " +
                        " SELECT post_id, COUNT(post_id) as LikeNumber " +
                        " FROM post_likes " +
                        " GROUP BY post_id " +
                " ) l ON p.id = l.post_id " +
                " LEFT JOIN( " +
                        " SELECT post_id, COUNT(post_id) as CommentNumber " +
                        " FROM post_comments " +
                        " GROUP BY post_id " +
                " ) cm ON cm.post_id = p.id " +
                "       WHERE f.user1_id = #{id} " +
                " UNION" +
                " SELECT  p.id as PostID,u.avatar as avatar, u.name as UserName, p.user_id as UserID , p.content as Content , cm.CommentNumber as CommentCount , l.LikeNumber as LikeCount, p.create_at as TimeCreate "+
                " FROM posts p " +
                " INNER JOIN users u ON u.id = p.user_id " +
                " LEFT JOIN ( " +
                        " SELECT post_id, COUNT(post_id) as LikeNumber " +
                        " FROM post_likes " +
                        " GROUP BY post_id " +
                " ) l ON p.id = l.post_id " +
                " LEFT JOIN( " +
                        " SELECT post_id, COUNT(post_id) as CommentNumber " +
                        " FROM post_comments " +
                        " GROUP BY post_id " +
                " ) cm ON cm.post_id = p.id " +
                "       WHERE p.user_id = #{id} "
        )
        List<PostDTO> getPostFollowingDtos(int UserID);

        @Select(        " SELECT post_file FROM post_files " +
                        " WHERE post_id = #{postID} " +
                        "LIMIT 1")
        String getPostDetail(int postID);

        @Select(        " SELECT user_id as userID, post_id as postID FROM post_likes " +
                        " WHERE post_id = #{postId} AND user_id = #{userId}")
        LikeDTO getLikeDTO(int postId, int userId);
        

        @Select(        " SELECT id, user_id as UserID FROM posts " +
                        " WHERE id = #{postID} ")
        PostDTO getPostByID(int postID );

        @Insert(        " INSERT INTO posts(content, user_id) " +
                        " VALUES (#{content} , #{userID})")
        void insertPost(String content, int userID);

        @Select(        "SELECT LAST_INSERT_ID()")
        int getLastInsertedPostID();

        @Insert(        " INSERT INTO post_files(post_id,post_file) " +
                        " VALUES ( #{postID}, #{postFile} ) ")
        void insertPostDetail(int postID, String postFile);

        @Update(        " UPDATE posts " +
                        " SET content=#{content} " +
                        " WHERE id = #{id} ")
        int updatePost(String content, int id);
 
        @Delete(        " DELETE FROM posts " +
                        " WHERE id = #{id} ")
        int deletePost(int id);

        @Delete(        " DELETE FROM post_files " +
                        " WHERE post_id = #{postID}")
        int deletePostDetail(int postID);

        @Select(        " SELECT p.id as PostID, u.name as UserName, p.user_id as UserID , p.content as Content , pcm.CommentNumber as CommentCount , pl.LikeNumber as LikeCount, p.create_at as TimeCreate,f.post_file as image " +
                        " FROM users u " +
                        " INNER JOIN posts p ON u.id = p.user_id " +
                        " INNER JOIN post_files f ON f.post_id = p.id "  +
                        " LEFT JOIN( " +
                        " SELECT COUNT(l.post_id) as LikeNumber, l.post_id " +
                        " FROM post_likes l " +
                        "   GROUP BY l.post_id " +
                        " ) pl ON pl.post_id = p.id "+
                        " LEFT JOIN( " +
                        " SELECT COUNT(cm.post_id) as CommentNumber, cm.post_id " +
                        " FROM post_comments cm " +
                        "   GROUP BY cm.post_id " +
                        " ) pcm ON pcm.post_id = p.id " +
                        "WHERE u.id = #{id} " )
        List<PostDTO> getImageById(int id);

}
