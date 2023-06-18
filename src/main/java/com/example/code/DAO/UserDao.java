package com.example.code.DAO;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.example.code.Model.User;

@Repository
@Mapper
public interface UserDao {
        @Select("SELECT * FROM Users")
        List<User> getUserAll();

        @Select(" SELECT * FROM Users " +
        " WHERE UserID = #{id} ")
        User getUserByID(@Param("id") int id);


        @Select(" SELECT * FROM Users " +
                " WHERE Email = #{email} ")
        User getUserByEmail(@Param("email") String email);

        @Select(" SELECT * FROM Users " +  
                " WHERE UserName = #{userName} ")
        List<User> getUserByName(@Param("UserName") String userName);
        
        @Insert(" INSERT INTO Users (UserName, Avatar, UserAddress, UserPhone, Email, PASSWORD) " +
                " VALUES (#{UserName}, #{Avatar}, #{UserAddress}, #{UserPhone}, #{Email}, #{PASSWORD}) ")
        void insertUser(User user);
        @Update(" UPDATE Users " +
                " SET UserName = #{name},Avatar = #{avatar},UserAddress = #{address},UserPhone = #{phone},Email = #{email},PASSWORD =#{password}" +
                " WHERE id = #{id}")
        void updateUser(User user,@Param("id") int id);
        @Update(" UPDATE Users " + 
                " SET PASSWORD = #{password} " +
                " WHERE Email = #{email} "
                )
        void updatePassWord(@Param("password") String password,@Param("email")  String Email);

        @Delete(" DELETE * FROM Users " +
                " where id = #{id} ")
        void delete(@Param("id") int id );
        @Select(" SELECT u.UserID,u.UserName,u.Avatar,u.UserAddress,u.UserPhone,u.Email,u.PASSWORD " +
                " FROM AddFriend af " +
                " INNER JOIN Users u ON af.UserID = u.UserID " +
                " WHERE af.UserIDSend = #{id} and Status = #{id} " +
                " UNION " +
                " SELECT u.UserID,u.UserName,u.Avatar,u.UserAddress,u.UserPhone,u.Email,u.PASSWORD " +
                " FROM AddFriend af " +
                " INNER JOIN Users u ON af.UserIDSend = u.UserID " +
                " WHERE af.UserID = #{id} and Status = #{id} " )
        List<User> getFriend(@Param("id") int id);
        @Select(" SELECT UserID FROM Users " +
                " WHERE Email = #{Email} ")
        int getIDByEmail(@Param("Email") String email);
}
