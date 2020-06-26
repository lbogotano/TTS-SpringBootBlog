package com.win.techtalentblog.BlogPost;

import org.springframework.data.repository.CrudRepository;

public interface BlogPostRepository extends CrudRepository<BlogPost, Long> {
    // now we have access to all of the CRUD methods
}