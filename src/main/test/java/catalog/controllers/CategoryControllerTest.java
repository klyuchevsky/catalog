package catalog.controllers;

import catalog.domain.Category;
import catalog.services.CategoryService;
import org.junit.Test;
import org.junit.runner.RunWith;
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

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

}
