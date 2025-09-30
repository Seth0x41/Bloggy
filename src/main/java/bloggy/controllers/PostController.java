package securitytut.bloggy.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import securitytut.bloggy.domain.CreatePostRequest;
import securitytut.bloggy.domain.UpdatePostRequest;
import securitytut.bloggy.domain.dtos.CreatePostRequestDTO;
import securitytut.bloggy.domain.dtos.PostDTO;
import securitytut.bloggy.domain.dtos.UpdatePostRequestDTO;
import securitytut.bloggy.domain.entities.Post;
import securitytut.bloggy.domain.entities.User;
import securitytut.bloggy.mappers.PostMapper;
import securitytut.bloggy.services.PostService;
import securitytut.bloggy.services.UserService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PostMapper postMapper;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<PostDTO>> getAllPosts(
            @RequestParam(required = false)UUID categoryId,
            @RequestParam(required = false)UUID tagId){
        List<Post> posts = postService.getAllPosts(categoryId,tagId);
        List<PostDTO> postsDTO = posts.stream().map(postMapper::toDTO).toList();
        return ResponseEntity.ok(postsDTO);
    }

    @GetMapping(path = "/drafts")
    public ResponseEntity<List<PostDTO>> getDrafts(@RequestAttribute UUID userId){
    User loggedUser =  userService.getUserById(userId);
    List<Post> draftPosts = postService.getDraftPosts(loggedUser);
    List<PostDTO> draftPostsDTO = draftPosts.stream().map(postMapper::toDTO).toList();
    return ResponseEntity.ok(draftPostsDTO);
    }

    @PostMapping
    public ResponseEntity<PostDTO> createPost(
            @Valid @RequestBody CreatePostRequestDTO createPostRequestDTO,
            @RequestAttribute UUID userId
            ){
        User loggedUser =  userService.getUserById(userId);
       CreatePostRequest createPostRequest = postMapper.toCreatePostRequest(createPostRequestDTO);
    Post createdPost= postService.createPost(loggedUser,createPostRequest);
    PostDTO createdPostDTO = postMapper.toDTO(createdPost);
    return new ResponseEntity<>(createdPostDTO, HttpStatus.CREATED);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<PostDTO> updatePost(
            @PathVariable UUID id,
            @Valid  @RequestBody UpdatePostRequestDTO updatePostRequestDTO
            ){
            UpdatePostRequest updatePostRequest= postMapper.toUpdatePostRequest(updatePostRequestDTO);
            Post updatedPost= postService.updatePost(id,updatePostRequest);
            PostDTO updatedPostDTO = postMapper.toDTO(updatedPost);
            return ResponseEntity.ok(updatedPostDTO);

    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<PostDTO> getPost(
            @PathVariable UUID id
    ){
    Post post = postService.getPost(id);
    PostDTO postDTO=  postMapper.toDTO(post);
    return ResponseEntity.ok(postDTO);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable UUID id){
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

}
