package com.example.code.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;
import com.example.code.dto.PasswordResetDTO;
import com.example.code.dto.PostDTO;
import com.example.code.dto.UserDTO;

@Repository
@Mapper
public interface UserDao {

        @Select(        "SELECT * FROM users")
        List<UserDTO> getUserAll();

        @Select(        " SELECT * FROM users " +
                        " WHERE id = #{id} ")
        UserDTO getUserByID(int id);

        @Select(        " SELECT * FROM users " +
                        " WHERE email = #{email} ")
        UserDTO getUserByEmail(String email);

        @Select(        " SELECT * FROM users " +
                        " WHERE name LIKE CONCAT('%', #{userName}, '%')")
        List<UserDTO> getUsersByName(String userName);

        @Select(        " SELECT email " +
                        " FROM password_reset " +
                        " WHERE number_key = #{numberKey} ")
        String getEmailByKey(int numberKey);

        @Select(        " SELECT  p.id as id, u.name as UserName, p.content as Content , cm.CommentNumber as CommentCount , l.LikeNumber as LikeCount, p.create_at as TimeCreate  "
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

        @Select(        " SELECT * FROM password_reset " +
                        " WHERE number_key = #{numberKey} ")
        PasswordResetDTO getPasswordResetRequest(int numberKey);

        @Select(        " SELECT id FROM users " +
                        " WHERE email = #{email} ")
        int getIDByEmail(String email);

        @Insert(        " INSERT INTO users (name, avatar, address, phone, email, password) " +
                        " VALUES (#{name}, #{avatar}, #{address}, #{phone}, #{email}, #{password}) ")
        void insertUser(UserDTO user);

        @Select(        "SELECT LAST_INSERT_ID()")
        int getLastInsertedUserID();

        @Insert(        " INSERT INTO password_reset (email, number_key) " +
                        " VALUES (#{email},#{numberKey})")
        void insertPasswordReset(String email, int numberKey);

        
        @Update(        " UPDATE users " +
                        " SET name = #{name},avatar = #{avatar},address = #{address},phone = #{phone}" +
                        " WHERE id = #{id} ")
        void updateUser(UserDTO user);

        @Update(        " UPDATE users " +
                        " SET password = #{password} " +
                        " WHERE email = #{email} ")
        void updatePasswordByEmail(String email, String password);

        @Update(        " UPDATE users " +
                        " SET password = #{password} " +
                        " WHERE id = #{userID}" )
        void updatePasswordByID(String password, int userID);

        @Delete(        " DELETE " +
                        " FROM password_reset " +
                        " WHERE email = #{email} AND number_ = #{numberKey} ")
        void deletePasswordReset(String email, int numberKey);
}