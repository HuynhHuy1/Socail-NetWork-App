package com.example.code.DTO;

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
    private int StatusID;
    private String UserName, Content;
    private List<String> image; 
    private int LikeCount, CommentCount;
}
