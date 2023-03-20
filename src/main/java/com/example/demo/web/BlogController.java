package com.example.demo.web;

import com.example.demo.models.Blog;
import com.example.demo.request.BlogRequest;
import com.example.demo.services.BlogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api")
public class BlogController {

    private final BlogService blogService;


    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    @PostMapping("/blog")
    public ResponseEntity<?> saveBlog(@RequestBody BlogRequest request) {
        blogService.saveBlog(request);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/blog/{id}")
    public ResponseEntity<?> updateBlog(@RequestBody BlogRequest request, @PathVariable Long id) {
        blogService.updateBlog(request, id);
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("/blog/{id}")
    public ResponseEntity<?> deleteBlog(@PathVariable Long id) {
        blogService.deleteBlog(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/blog/{id}")
    public ResponseEntity<Blog> getBlog(@PathVariable Long id) {
        return ResponseEntity.ok(blogService.getBlog(id));
    }


    @GetMapping("/blogs")
    public ResponseEntity<List<Blog>> getBlogs() {
        return ResponseEntity.ok(blogService.getAllBlogs());
    }


}
