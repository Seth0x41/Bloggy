package securitytut.bloggy.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import securitytut.bloggy.domain.PostStatus;
import securitytut.bloggy.domain.entities.Category;
import securitytut.bloggy.domain.entities.Post;
import securitytut.bloggy.domain.entities.Tag;
import securitytut.bloggy.domain.entities.User;

import java.util.List;
import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {
    List<Post> findAllByStatusAndCategoryAndTagsContaining(
            PostStatus status, Category category, Tag tag);
    List<Post> findAllByStatusAndCategory(PostStatus postStatus,Category category);
    List<Post> findAllByStatusAndTagsContaining(PostStatus postStatus, Tag tag);
    List<Post> findAllByStatus(PostStatus status);
    List<Post> findAllByAuthorAndStatus(User user,PostStatus status);
}
