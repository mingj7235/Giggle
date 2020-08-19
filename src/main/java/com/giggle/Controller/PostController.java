package com.giggle.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.giggle.Domain.Entity.CommunityType;
import com.giggle.Domain.Entity.Post;
import com.giggle.Domain.Form.CreatePostForm;
import com.giggle.Service.CategoryService;
import com.giggle.Service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final CategoryService categoryService;
    private final ObjectMapper objectMapper;

    @GetMapping("/post/create/{communityType}/{category}")
    public String create(@PathVariable String communityType, @PathVariable String category, Model model){
        List<String> categoryNameList = categoryService.getCategoryNamesInCommunity(CommunityType.valueOf(communityType));
        model.addAttribute("communityType",communityType);
        model.addAttribute("categoryNow", category);
        model.addAttribute("categoryNameList", categoryNameList);
        return "createPostForm";
    }

    @GetMapping("/post/create/{communityType}")
    public String createPost(@PathVariable String communityType, Model model){
        List<String> categoryNameList = categoryService.getCategoryNamesInCommunity(CommunityType.valueOf(communityType));
        model.addAttribute("communityType",communityType);
        model.addAttribute("categoryNow", null);
        model.addAttribute("categoryNameList", categoryNameList);
        return "createPostForm";
    }


    @PostMapping("/post/create")
    public String createNewPost(CreatePostForm createPostForm) throws JsonProcessingException {
        String result = objectMapper.writeValueAsString(createPostForm);
        Post newPost = new Post();
        newPost.setCategory(createPostForm.getCategory());
        newPost.setCommunityType(CommunityType.valueOf(createPostForm.getCommunity()));
        newPost.setTitle(createPostForm.getTitle());
        newPost.setWriter("tester");
        newPost.setContent(createPostForm.getContent());

        postService.createPost(newPost);
        return "redirect:/post/"+createPostForm.getCategory();
    }
}
