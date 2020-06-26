package com.win.techtalentblog.BlogPost;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.BeanProperty.Bogus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class BlogPostController {

    @Autowired
    private BlogPostRepository blogPostRepository;

    private static List<BlogPost> posts = new ArrayList<>();

    @GetMapping(value = "/")
    public String index(BlogPost blogPost, Model model) {
        posts.removeAll(posts); // clear everything out to add others
        for (BlogPost post : blogPostRepository.findAll()) { // find all gets all of the objects of entity and stores
                                                             // them
            posts.add(post);
        }
        model.addAttribute("posts", posts);
        return "blogpost/index";
    }

    private BlogPost blogPost;

    @GetMapping(value = "/blogposts/new")
    public String newBlog(BlogPost blogPost) {
        return "blogpost/new";
    }

    @PostMapping(value = "/blogposts")
    public String addNewBlogPost(BlogPost blogPost, Model model) {
        blogPostRepository.save(new BlogPost(blogPost.getTitle(), blogPost.getAuthor(), blogPost.getBlogEntry()));
        // posts.add(blogPost);
        model.addAttribute("title", blogPost.getTitle());
        model.addAttribute("author", blogPost.getAuthor());
        model.addAttribute("blogEntry", blogPost.getBlogEntry());
        return "blogpost/result";
    }

    // Similar to @PostMapping or @GetMapping, but allows for @PathVariable
    @RequestMapping(value = "/blogposts/{id}", method = RequestMethod.GET)
    // Spring takes whatever value is in {id} and passes it to our method params
    // using @PathVariable
    public String editPostWithId(@PathVariable Long id, BlogPost blogPost, Model model) {
        // findById()returns an Optional<T> which can be null so we can test. Optional
        // is a built in safe incase a method doesn't exist.
        Optional<BlogPost> post = blogPostRepository.findById(id);
        // Test if post actually has anything in it
        if (post.isPresent()) {
            // Unwrap the post from Optional shell
            // is present says this might not be what you are looking for so check it
            BlogPost actualPost = post.get();
            model.addAttribute("blogPost", actualPost);
        }
        return "blogpost/edit";
    }

    @RequestMapping(value = "/blogposts/update/{id}")
    public String updateExistingPost(@PathVariable Long id, BlogPost blogPost, Model model) {
        Optional<BlogPost> post = blogPostRepository.findById(id);
        if (post.isPresent()) {
            BlogPost actualPost = post.get();
            actualPost.setTitle(blogPost.getTitle());
            actualPost.setAuthor(blogPost.getAuthor());
            actualPost.setBlogEntry(blogPost.getBlogEntry());
            // takes new data and saves it in the database
            // Save() is so AWESOME that it works for both creating new posts and
            // overwriting existing posts
            // If the primary key of the Entity we give it matches the primary of a record
            // already in the DB, it will save over it instead of creating a new record
            blogPostRepository.save(actualPost);
            model.addAttribute("blogPost", actualPost);
        }
        return "blogpost/result";
    }

    @RequestMapping(value = "blogposts/delete/{id}")
    public String deletePostsById(@PathVariable Long id, BlogPost blogPost) {
        blogPostRepository.deleteById(id);
        return "blogpost/delete";
    }
}