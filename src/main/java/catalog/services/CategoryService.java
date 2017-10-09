package catalog.services;

import catalog.domain.Category;
import catalog.dto.CategoryDTO;

import java.util.List;

/**
 * Category specific business layer operations
 */
public interface CategoryService {

    /**
     * Get list of all categories
     *
     * @return list of categories
     */
    List<Category> getAll();

    /**
     * Get category by id
     *
     * @param id category id
     * @return entity if exist, otherwise null
     */
    Category getById(Long id);

    /**
     * Create category by DTO object
     *
     * @param dto object containing properties for new entity
     * @return created entity
     */
    Category createCategory(CategoryDTO dto);

    /**
     * Update category by DTO object
     *
     * @param dto object containing properties for update in existing entity
     * @return updated entity
     */
    Category updateCategory(CategoryDTO dto);

    /**
     * Delete category by its id.
     *
     * @param id id of deleted category
     */
    void deleteCategory(Long id);

}
