package com.example.demo.services;

import com.example.demo.mapper.BlogMapper;
import com.example.demo.models.Blog;
import com.example.demo.models.User;
import com.example.demo.repo.BlogRepo;
import com.example.demo.request.BlogRequest;
import com.example.demo.response.BlogDto;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BlogService {

    private final BlogRepo blogRepo;
    private final BlogMapper blogMapper;

    private final UserClientService userClientService;

    public BlogService(BlogRepo blogRepo, BlogMapper blogMapper, UserClientService userClientService) {
        this.blogRepo = blogRepo;
        this.blogMapper = blogMapper;
        this.userClientService = userClientService;
    }


    public Blog saveBlog(BlogRequest request) {
        Blog blog = blogMapper.mapToBlog(request);
        User user = userClientService.getUserByUserName(request.getUsername());
        blog.setUser(user);
        return blogRepo.saveAndFlush(blog);
    }

    public void updateBlog(BlogRequest request, Long id) {
        Blog blogNotFound = blogRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog not found"));
        Blog blog = blogMapper.mapToBlog(request);
        blog.setId(id);
        blogRepo.save(blog);
    }


    public void deleteBlog(Long id) {
        Blog blog = blogRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog not found"));
        blogRepo.delete(blog);
    }


    public Blog getBlog(Long id) {
        return blogRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog not found"));
    }

    public List<Blog> getAllBlogs() {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        return blogRepo.findAll(sort);
    }

}
