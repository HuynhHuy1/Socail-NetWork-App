package com.example.code.dto;

import java.sql.Timestamp;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {
    private int id;
    private String userName, content;
    private List<String> image; 
    private int likeCount, commentCount;
    private Timestamp timeCreate;
    private int userID;
}
