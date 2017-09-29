package catalog.services;

import catalog.domain.Category;
import catalog.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {

    List<Category> getAll();

    Category getById(Long id);

    Category createCategory(CategoryDTO dto);

    Category updateCategory(CategoryDTO dto);

    void deleteCategory(Long id);

}
