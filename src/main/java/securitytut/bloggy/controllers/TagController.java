package securitytut.bloggy.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import securitytut.bloggy.domain.dtos.CreateTagsRequest;
import securitytut.bloggy.domain.dtos.TagDTO;
import securitytut.bloggy.domain.entities.Tag;
import securitytut.bloggy.mappers.TagMapper;
import securitytut.bloggy.services.TagService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;
    private final TagMapper tagMapper;
    @GetMapping
    public ResponseEntity<List<TagDTO>> getAllTags(){
        List<Tag> tags = tagService.getTags();
        List<TagDTO> tagRespons = tags.stream().map(tagMapper::toTagResponse).toList();
        return ResponseEntity.ok(tagRespons);
    }

    @PostMapping
    public ResponseEntity<List<TagDTO>> createTags(@RequestBody CreateTagsRequest createTagsRequest){
    List<Tag> savedTags = tagService.createTags(createTagsRequest.getNames());
    List<TagDTO> createTagRespons = savedTags.stream().map(tagMapper::toTagResponse).toList();
    return new ResponseEntity<>(createTagRespons, HttpStatus.CREATED);
    }


    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable UUID id){
        tagService.deleteTag(id);
        return ResponseEntity.noContent().build();
    }
}
