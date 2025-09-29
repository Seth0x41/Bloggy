package securitytut.bloggy.mappers;


import jakarta.persistence.criteria.CriteriaBuilder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import securitytut.bloggy.domain.PostStatus;
import securitytut.bloggy.domain.dtos.TagResponse;
import securitytut.bloggy.domain.entities.Post;
import securitytut.bloggy.domain.entities.Tag;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TagMapper {
    @Mapping(target = "postCount",source = "posts",qualifiedByName = "calculatePostCount")
    TagResponse toTagResponse(Tag tag);

    @Named("calculatePostCount")
    default Integer calculatePostCount(Set<Post> posts){
        if(posts == null){
            return 0;
        }
        return (int) posts.stream().filter(post -> PostStatus.PUBLISHED.equals(post.getStatus()))
                .count();

    }
}
