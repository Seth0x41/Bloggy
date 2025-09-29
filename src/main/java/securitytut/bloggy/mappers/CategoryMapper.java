package securitytut.bloggy.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import securitytut.bloggy.domain.PostStatus;
import securitytut.bloggy.domain.dtos.CategoryDTO;
import securitytut.bloggy.domain.dtos.CreateCategoryRequest;
import securitytut.bloggy.domain.entities.Category;
import securitytut.bloggy.domain.entities.Post;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {
    @Mapping(target = "postCount",source ="posts",qualifiedByName = "calculatePostCount")
    CategoryDTO toDTO(Category category);

    Category toEntity(CreateCategoryRequest createCategoryRequest);


    @Named("calculatePostCount")
    default long calculatePostCount(List<Post> posts){
        if(null == posts){
            return 0;
        }
        return
                posts.stream().filter(post -> PostStatus.PUBLISHED.equals(post.getStatus()))
                        .count();
    };
}