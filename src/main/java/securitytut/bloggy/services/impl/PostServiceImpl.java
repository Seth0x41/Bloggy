package securitytut.bloggy.services.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import securitytut.bloggy.domain.CreatePostRequest;
import securitytut.bloggy.domain.PostStatus;
import securitytut.bloggy.domain.UpdatePostRequest;
import securitytut.bloggy.domain.entities.Category;
import securitytut.bloggy.domain.entities.Post;
import securitytut.bloggy.domain.entities.Tag;
import securitytut.bloggy.domain.entities.User;
import securitytut.bloggy.repositories.PostRepository;
import securitytut.bloggy.services.CategoryService;
import securitytut.bloggy.services.PostService;
import securitytut.bloggy.services.TagService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final CategoryService categoryService;
    private final TagService tagService;

    private final int WORD_PER_MIN = 200;
    @Override
    @Transactional(readOnly = true)
    public List<Post> getAllPosts(UUID categoryId, UUID tagId) {
        if(categoryId != null && tagId != null){
        Category category= categoryService.getCategoryById(categoryId);
        Tag tag = tagService.getTagById(tagId);
        return postRepository.findAllByStatusAndCategoryAndTagsContaining(
                PostStatus.PUBLISHED,
                category,
                tag
        );
        }
        if(categoryId != null){
            Category category= categoryService.getCategoryById(categoryId);
            return postRepository.findAllByStatusAndCategory(
                            PostStatus.PUBLISHED,
                    category
            );
        }
        if(tagId != null){
            Tag tag = tagService.getTagById(tagId);
        return postRepository.findAllByStatusAndTagsContaining(
                PostStatus.PUBLISHED,
                tag
        );
        }
        return postRepository.findAllByStatus(PostStatus.PUBLISHED);
    }

    @Override
    public List<Post> getDraftPosts(User user) {
        return postRepository.findAllByAuthorAndStatus(user, PostStatus.DRAFT);
    }

    @Override
    @Transactional
    public Post createPost(User user, CreatePostRequest createPostRequest) {
        Post newPost = new Post();
        newPost.setTitle(createPostRequest.getTitle());
        newPost.setContent(createPostRequest.getContent());
        newPost.setStatus(createPostRequest.getPostStatus());
        newPost.setAuthor(user);
        newPost.setReadingTime(calculateReadingTime(createPostRequest.getContent()));
        Category category = categoryService.getCategoryById(createPostRequest.getCategoryId());
        newPost.setCategory(category);
        Set<UUID> tagIds = createPostRequest.getTagIds();
        List<Tag> tags = tagService.getTagByIds(tagIds);
        newPost.setTags(new HashSet<>(tags));

        return postRepository.save(newPost);
    }

    @Override
    @Transactional
    public Post updatePost(UUID id, UpdatePostRequest updatePostRequest) {
    Post existingPost= postRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Post does not exist with id"+ id));
        existingPost.setTitle(updatePostRequest.getTitle());
        String postContent =updatePostRequest.getContent();
        existingPost.setContent(postContent);
        existingPost.setStatus(updatePostRequest.getPostStatus());
        existingPost.setReadingTime(calculateReadingTime(postContent));
        UUID updatePostRequestCategoryId =  updatePostRequest.getCategoryId();
        if(!existingPost.getCategory().getId().equals(updatePostRequestCategoryId)){
        Category newCategory = categoryService.getCategoryById(updatePostRequestCategoryId);
        existingPost.setCategory(newCategory);

        }
         Set<UUID> existingTagIds = existingPost.getTags().stream().map(Tag::getId).collect(Collectors.toSet());
        Set<UUID> updatePostRequestTagIds = updatePostRequest.getTagIds();
        if(existingTagIds.equals(updatePostRequestTagIds)){
        List<Tag> newTags=   tagService.getTagByIds(updatePostRequestTagIds);
        existingPost.setTags(new HashSet<>(newTags));
        }
    return postRepository.save(existingPost);
    }

    @Override
    public Post getPost(UUID id) {
    return postRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Post does not exist with ID "+ id));
    }

    @Override
    public void deletePost(UUID id) {
    Post post= getPost(id);
    postRepository.delete(post);
    }

    private Integer calculateReadingTime(String content){
        if(content == null || content.isEmpty()){
            return 0;
        }
    int wordCount= content.trim().split("\\s+").length;
        return (int) Math.ceil((double) wordCount/ WORD_PER_MIN);
    }

}
