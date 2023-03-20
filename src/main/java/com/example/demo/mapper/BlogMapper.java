package com.example.demo.mapper;

import com.example.demo.models.Blog;
import com.example.demo.request.BlogRequest;
import com.example.demo.response.BlogDto;
import org.springframework.stereotype.Component;


@Component
public class BlogMapper {


    public Blog mapToBlog(BlogRequest blogRequest) {
        Blog blog = new Blog();
        blog.setTitle(blogRequest.getTitle());
        blog.setContent(blogRequest.getContent());
        return blog;
    }
}
