package catalog.services.Impl;

import catalog.domain.Category;
import catalog.dto.CategoryDTO;
import catalog.repositories.CategoryRepository;
import catalog.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Category specific business layer operations
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> getAll() {
        return categoryRepository.findAll(new Sort(Sort.Direction.ASC, "id"));
    }

    @Override
    public Category getById(Long id) {
        return categoryRepository.findOne(id);
    }

    /**
     * Create category by DTO object
     *
     * @param dto object containing properties for new entity
     * @return created entity
     */
    @Override
    public Category createCategory(CategoryDTO dto) {
        Category category = new Category();
        return fill(category, dto);
    }

    /**
     * Update category by DTO object
     *
     * @param dto object containing properties for update in existing entity
     * @return updated entity
     */
    @Override
    public Category updateCategory(CategoryDTO dto) {
        Long id = Optional.ofNullable(dto.getId())
                .orElseThrow(() -> new NoSuchElementException("Id field must be presented in DTO"));
        return Optional.ofNullable(categoryRepository.findOne(id))
                .map(category -> fill(category, dto))
                .orElse(null);
    }

    /**
     * Delete category by its id.
     *
     * @param id id of deleted category
     */
    @Override
    public void deleteCategory(Long id) {
        categoryRepository.delete(id);
    }

    private Category fill(Category category, CategoryDTO dto) {
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        return categoryRepository.save(category);
    }

}
