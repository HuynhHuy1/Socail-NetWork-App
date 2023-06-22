package com.example.code.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.example.code.dto.CommentDTO;
import com.example.code.dto.FriendShipDTO;
import com.example.code.dto.LikeDTO;
import com.example.code.dto.PostDTO;
import com.example.code.dto.UserDTO;

@Repository
@Mapper
public interface PostDao {
        @Select(" SELECT  p.id as PostID, u.name as UserName, p.content as Content , cm.CommentNumber as CommentCount , l.LikeNumber as LikeCount, p.create_at as TimeCreate  "
                        +
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
                        " WHERE f.user1_id = #{id} AND f.status = 1 " +
                        " UNION " +
                        " SELECT  p.id as PostID, u.name as UserName, p.content as Content , cm.CommentNumber as CommentCount , l.LikeNumber as LikeCount,p.create_at as TimeCreate  "
+
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

        @Select(" SELECT post_file FROM post_files " +
                        " WHERE post_id = #{postID} ")
        List<String> getPostDetail(int postID);

        @Insert(" INSERT INTO posts(content, user_id) " +
                        " VALUES ( #{content} , #{userID})")
        void insertPost(String content, int userID);

        @Select("SELECT LAST_INSERT_ID()")
        int getLastInsertedPostID();

        @Insert(" INSERT INTO post_files(post_id,post_file) " +
                        " VALUES ( #{postID}, #{files} ) ")
        void insertPostDetail(int id, String files);

        @Update(" UPDATE posts " +
                        " SET content=#{content}" +
                        " WHERE id = #{id}")
        void updatePost(String content, int id);
 
        @Delete(" DELETE FROM posts " +
                        " WHERE id = #{id} AND user_id = #{userID} ")
        void deletePost(int id, int userID);

        @Delete(" DELETE FROM post_files " +
                        " WHERE post_id = #{postID}")
        void deletePostDetail(int postID);

        @Select(" SELECT  p.id as id, u.name as UserName, p.content as Content , cm.CommentNumber as CommentCount , l.LikeNumber as LikeCount, p.create_at as TimeCreate  "
                        +
                        " FROM posts p " +
                        " INNER JOIN users u ON u.id = p.user_id " +
                        " LEFT JOIN ( " +
                        " SELECT post_id, COUNT(post_id) as LikeNumber " +
                        " FROM post_likes " +
                        " GROUP BY post_id " +
                        " ) l ON p.id = l.post_id " +
                        " LEFT JOIN( " +
                        " SELECT post_id, COUNT(post_id) as CommentNumber " +
                        " FROM post_likes " +
                        " GROUP BY post_id " +
                        " ) cm ON cm.post_id = p.id " +
                        " WHERE u.id = #{userID}")
        List<PostDTO> getProFileByID(int userID);

        @Select(" SELECT u.name as UserName, f.user2_id as UserSend, f.status as Status " +
                        " FROM friendships f " +
                        " INNER JOIN users u ON u.id = f.user2_id " +
                        " WHERE f.user1_id = #{userID} AND f.status = 0 ")
        List<FriendShipDTO> getFriendRequest(int userID);

        @Insert(" INSERT INTO friendships (user1_id,user2_id,status)" +
                        " VAlUES (#{userID},#{user2ID},0)") 
        void insertFriend(FriendShipDTO friendShipDto);

        @Update(" UPDATE friendships SET status = 1 " +
                        " WHERE user1_id = #{userID} AND user2_id = #{user2ID}")
        void updateStatusFriendRequest(FriendShipDTO friendShipDTO);

        @Select(" SELECT u.id,u.name,u.avatar,u.address,u.phone,u.email,u.password " +
                        " FROM friendships af " +
                        " INNER JOIN users u ON af.user1_id = u.id " +
                        " WHERE af.user2_id = #{id} and status = 1 " +
                        " UNION " +
                        " SELECT u.id,u.name,u.avatar,u.address,u.phone,u.email,u.password " +
                        " FROM friendships af " +
                        " INNER JOIN users u ON af.user2_id = u.id " +
                        " WHERE af.user1_id = #{id} and status = 1 ")
        List<UserDTO> getFriend(int id);

        @Update(" UPDATE users " +
                        " SET name= #{name},avatar=#{avatar},address=#{address},phone=#{phone}"
                        +
                        " WHERE users.id = #{id} ")
        void updateUser(UserDTO user);

        @Update(" UPDATE users " +
                        " SET password = #{password} " +
                        " WHERE id = #{userID} ")
        void updatePassWord(String password, int userID);

        @Delete(" DELETE FROM friendships " +
                        " WHERE user1_id = #{userID} AND user2_id = #{user2ID} OR user2_id = #{userID} AND user1_id = #{user2ID} ")
        void deleteFriend(FriendShipDTO friendShipDTO);

        @Select(" SELECT u.name as UserName,u.avatar as avatar " +
                        " FROM post_likes l " +
                        " INNER JOIN users u ON u.id = l.user_id " +
                        " WHERE l.post_id = #{postID} ")
        List<LikeDTO> getLike(int postID);

        @Insert(" INSERT INTO post_likes(post_id,user_id) " +
                        " VALUES (#{postID},#{userID}) ")
        void insertLike(LikeDTO likeDTO);

        @Delete(" DELETE FROM post_likes " +
                        " WHERE post_id = #{postID} and user_id = #{userID} ")
        void deleteLike(int postID, int userID);

        @Select(" SELECT u.name as UserName,u.avatar as avatar, cm.content as Content , cm.id as CommentID "
                        +
                        " FROM post_comments cm " +
                        " INNER JOIN users u ON u.id = cm.user_id " +
                        " WHERE cm.post_id = #{postID}")
        List<CommentDTO> getComment(int postID);

        @Insert(" INSERT INTO post_comments(content,user_id,post_id) " +
                        " VALUES (#{content},#{userID},#{id})")
        void insertComment(PostDTO postDto);

        @Update(" UPDATE post_comments " +
                        " SET content =  #{content} " +
                        " WHERE id = #{commentID} AND user_id = #{userID}")
        void updateComment(String content, int commentID, int userID);

        @Delete(" DELETE FROM post_comments " +
                        " WHERE id = #{id} AND user_id = #{userID} ")
        void deleteComment(int id, int userID);
}
