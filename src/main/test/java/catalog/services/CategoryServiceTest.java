package catalog.services;

import catalog.domain.Category;
import catalog.dto.CategoryDTO;
import catalog.repositories.CategoryRepository;
import catalog.services.Impl.CategoryServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@Configuration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CategoryServiceTest.class})
public class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Bean
    public CategoryService categoryService() {
        return new CategoryServiceImpl();
    }

    @Bean
    public CategoryRepository categoryRepository() {
        return mock(CategoryRepository.class);
    }

    @Before
    public void setUp() {
        reset(categoryRepository);
    }

    @Test
    public void testCreateCategory() {
        CategoryDTO dto = new CategoryDTO();
        dto.setName("test name");
        dto.setDescription("test description");

        Category category = new Category();
        category.setId(1L);
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        when(categoryRepository.save(any(Category.class)))
                .thenReturn(category);

        Category result = categoryService.createCategory(dto);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo(category.getName());
        assertThat(result.getDescription()).isEqualTo(category.getDescription());

        ArgumentCaptor<Category> captor = ArgumentCaptor.forClass(Category.class);
        verify(categoryRepository).save(captor.capture());

        Category saved = captor.getValue();
        assertThat(saved.getId()).isNull();
        assertThat(saved.getName()).isEqualTo(category.getName());
        assertThat(saved.getDescription()).isEqualTo(category.getDescription());
    }

    @Test
    public void testUpdateCategory() {
        Category category = new Category();
        category.setId(1L);
        category.setName("test name");
        category.setDescription("test description");
        when(categoryRepository.findOne(category.getId()))
                .thenReturn(category);

        when(categoryRepository.save(any(Category.class)))
                .thenReturn(category);

        CategoryDTO dto = new CategoryDTO();
        dto.setId(1L);
        dto.setName("new name");
        dto.setDescription("new description");
        Category result = categoryService.updateCategory(dto);

        assertThat(result.getId()).isEqualTo(dto.getId());
        assertThat(result.getName()).isEqualTo(dto.getName());
        assertThat(result.getDescription()).isEqualTo(dto.getDescription());

        ArgumentCaptor<Category> captor = ArgumentCaptor.forClass(Category.class);
        verify(categoryRepository).save(captor.capture());

        Category saved = captor.getValue();
        assertThat(saved.getId()).isEqualTo(dto.getId());
        assertThat(saved.getName()).isEqualTo(dto.getName());
        assertThat(saved.getDescription()).isEqualTo(dto.getDescription());
    }

    @Test
    public void testDeleteCategory() {
        Long id = 1L;
        categoryService.deleteCategory(id);
        verify(categoryRepository).delete(id);
    }

}
