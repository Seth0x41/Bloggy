package securitytut.bloggy.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import securitytut.bloggy.domain.dtos.CategoryDTO;
import securitytut.bloggy.domain.dtos.CreateCategoryRequest;
import securitytut.bloggy.domain.entities.Category;
import securitytut.bloggy.mappers.CategoryMapper;
import securitytut.bloggy.services.CategoryService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> listCategories(){
        List<CategoryDTO> categories = categoryService.listCategories().stream()
                .map(categoryMapper::toDTO).toList();
        return ResponseEntity.ok(categories);
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(
            @Valid @RequestBody CreateCategoryRequest createCategoryRequest){
            Category categoryToCreate= categoryMapper.toEntity(createCategoryRequest);
            Category savedCategory = categoryService.createCategory(categoryToCreate);
            return  new ResponseEntity<>(
                    categoryMapper.toDTO(savedCategory),
                    HttpStatus.CREATED
            );
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteCategoty(@PathVariable UUID id){
        categoryService.deleteCategory(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
