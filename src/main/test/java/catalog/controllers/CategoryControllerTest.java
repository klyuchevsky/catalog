package catalog.controllers;

import catalog.domain.Category;
import catalog.dto.CategoryDTO;
import catalog.services.CategoryService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CategoryControllerTest.class})
@WebAppConfiguration
public class CategoryControllerTest {

    public MockMvc mvc() {
        return MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private CategoryService categoryService;

    @Bean
    public CategoryService categoryService() {
        return mock(CategoryService.class);
    }

    @Bean
    public CategoryController categoryController() {
        return new CategoryController();
    }

    @Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();

        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix("/WEB-INF/jsp/");
        viewResolver.setSuffix(".jsp");

        return viewResolver;
    }

    @Before
    public void setUp() {
        reset(categoryService);
    }

    @Test
    public void testGetAllCategories() throws Exception {
        Category c1 = new Category();
        c1.setId(1L);
        c1.setName("category name");
        c1.setDescription("category desc");

        Category c2 = new Category();
        c2.setId(2L);
        c2.setName("category name 2");
        c2.setDescription("category desc 2");

        List<Category> categories = new ArrayList<>(Arrays.asList(c1, c2));
        when(categoryService.getAll()).thenReturn(categories);

        mvc().perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(view().name("category"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/category.jsp"))
                .andExpect(model().attribute("categories", hasSize(2)))
                .andExpect(model().attribute("categories", hasItem(
                        allOf(
                                hasProperty("id", is(c1.getId())),
                                hasProperty("name", is(c1.getName())),
                                hasProperty("description", is(c1.getDescription()))
                        )
                ))).andExpect(model().attribute("categories", hasItem(
                        allOf(
                                hasProperty("id", is(c2.getId())),
                                hasProperty("name", is(c2.getName())),
                                hasProperty("description", is(c2.getDescription()))
                        )
                )));

        verify(categoryService).getAll();
    }

    @Test
    public void testAddCategory() throws Exception {
        String name = "category name";
        String description = "category desc";

        mvc().perform(post("/category/add")
                .param("name", name)
                .param("description", description)
        ).andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/categories"));

        ArgumentCaptor<CategoryDTO> captor = ArgumentCaptor.forClass(CategoryDTO.class);
        verify(categoryService).createCategory(captor.capture());

        CategoryDTO dto = captor.getValue();
        assertThat(dto.getId()).isNull();
        assertThat(dto.getName()).isEqualTo(name);
        assertThat(dto.getDescription()).isEqualTo(description);

        verify(categoryService, never()).updateCategory(any());
    }

    @Test
    public void testUpdateCategory() throws Exception {
        Long id = 1L;
        String name = "category name";
        String description = "category desc";

        when(categoryService.updateCategory(any()))
                .thenReturn(new Category());

        mvc().perform(post("/category/add")
                .param("id", id.toString())
                .param("name", name)
                .param("description", description)
        ).andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/categories"));

        verify(categoryService, never()).createCategory(any());

        ArgumentCaptor<CategoryDTO> captor = ArgumentCaptor.forClass(CategoryDTO.class);
        verify(categoryService).updateCategory(captor.capture());

        CategoryDTO dto = captor.getValue();
        assertThat(dto.getId()).isEqualTo(id);
        assertThat(dto.getName()).isEqualTo(name);
        assertThat(dto.getDescription()).isEqualTo(description);
    }

    @Test
    public void testEditCategory() throws Exception {
        Category c1 = new Category();
        c1.setId(1L);
        c1.setName("category name");
        c1.setDescription("category desc");
        when(categoryService.getById(c1.getId()))
                .thenReturn(c1);

        Category c2 = new Category();
        c2.setId(2L);
        c2.setName("category name 2");
        c2.setDescription("category desc 2");

        List<Category> categories = new ArrayList<>(Arrays.asList(c1, c2));
        when(categoryService.getAll()).thenReturn(categories);

        mvc().perform(get("/category/edit/" + c1.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("category"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/category.jsp"))
                .andExpect(model().attribute("form", allOf(
                        hasProperty("id", is(c1.getId())),
                        hasProperty("name", is(c1.getName())),
                        hasProperty("description", is(c1.getDescription()))
                ))).andExpect(model().attribute("categories", hasSize(2)))
                .andExpect(model().attribute("categories", hasItem(
                        allOf(
                                hasProperty("id", is(c1.getId())),
                                hasProperty("name", is(c1.getName())),
                                hasProperty("description", is(c1.getDescription()))
                        )
                ))).andExpect(model().attribute("categories", hasItem(
                        allOf(
                                hasProperty("id", is(c2.getId())),
                                hasProperty("name", is(c2.getName())),
                                hasProperty("description", is(c2.getDescription()))
                        )
                )));

        verify(categoryService).getAll();
        verify(categoryService).getById(c1.getId());
        verifyNoMoreInteractions(categoryService);
    }


    @Test
    public void testRemoveCategory() throws Exception {
        Long id = 1L;
        mvc().perform(delete("/category/remove/" + id))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/categories"));
        verify(categoryService).deleteCategory(id);
    }

}
