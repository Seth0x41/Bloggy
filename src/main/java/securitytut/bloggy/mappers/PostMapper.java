package securitytut.bloggy.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import securitytut.bloggy.domain.CreatePostRequest;
import securitytut.bloggy.domain.UpdatePostRequest;
import securitytut.bloggy.domain.dtos.CreatePostRequestDTO;
import securitytut.bloggy.domain.dtos.PostDTO;
import securitytut.bloggy.domain.dtos.UpdatePostRequestDTO;
import securitytut.bloggy.domain.entities.Post;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostMapper {
    @Mapping(target = "author",source = "author")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "tags", source = "tags")
    PostDTO toDTO(Post post);

    CreatePostRequest toCreatePostRequest(CreatePostRequestDTO dto);
    UpdatePostRequest toUpdatePostRequest(UpdatePostRequestDTO dto);

}
