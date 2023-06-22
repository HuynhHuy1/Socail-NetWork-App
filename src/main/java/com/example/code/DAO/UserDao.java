package com.example.code.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.example.code.dto.UserDTO;
import com.example.code.model.ForgotPassword;

@Repository
@Mapper
public interface UserDao {

        @Select("SELECT * FROM users")
        List<UserDTO> getUserAll();

        @Select(" SELECT * FROM users " +
                " WHERE id = #{id} ")
        UserDTO getUserByID( int id);


        @Select(" SELECT * FROM users " +
                " WHERE email = #{email} ")
        UserDTO getUserByEmail( String email);
        
        @Insert(" INSERT INTO users (name, avatar, address, phone, email, password) " +
                " VALUES (#{name}, #{avatar}, #{address}, #{phone}, #{email}, #{password}) ")
        void insertUser(UserDTO user);

        @Update(" UPDATE users " +
                " SET name = #{name},avatar = #{avatar},address = #{address},phone = #{phone},email = #{email},password=#{password}" +
                " WHERE id = #{id}")
        void updateUser(UserDTO user);

        @Update(" UPDATE users " + 
                " SET password = #{password} " +
                " WHERE email = #{email} "
                )
        void updatePassWord( String password,  String email);

        @Delete(" DELETE * FROM users " +
                " where id = #{id} ")
        void delete( int id );

        @Select(" SELECT id FROM users " +
                " WHERE email = #{email} ")
        int getIDByEmail( String email);

        @Select(" SELECT * FROM users " +
                " WHERE name LIKE CONCAT('%', #{userName}, '%')")
        List<UserDTO> getUsersByName(String userName);

        @Insert(" INSERT INTO password_reset (email, number_key) " +
                " VALUES (#{email},#{numberKey})")
        void generateKey(String email,int numberKey);
        
        @Select(" SELECT * FROM password_reset " +
                " WHERE number_key = #{numberKey} ")
        ForgotPassword getForgotPassword(int numberKey);
        
        @Select(" SELECT Email " +
                " FROM password_reset " +
                " WHERE number_ = #{numberKey} ")
        String getEmailByKey(int numberKey);

        @Update(" UPDATE users " + 
                " SET password = #{password} " +
                " WHERE email = #{email}")
        void resetPassWord(String email ,String password);
        
        @Delete(" DELETE " +
                " FROM password_reset " +
                " WHERE email = #{email} And number_key = #{numberKey} ")
        void deletePorgotPassword(String email, int numberKey );

}