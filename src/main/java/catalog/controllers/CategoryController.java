package catalog.controllers;

import catalog.dto.CategoryDTO;
import catalog.services.CategoryService;
import org.apache.tomcat.util.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class CategoryController {

    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private CategoryService categoryService;

    @RequestMapping("categories")
    public String category(Model model) {
        try {
            model.addAttribute("form", new CategoryDTO());

            List<CategoryDTO> categories = categoryService.getAll().stream()
                    .map(CategoryDTO::from)
                    .collect(Collectors.toList());
            model.addAttribute("categories", categories);

            return "category";
        } catch (Throwable t) {
            logger.error("Error during get categories list", t);
            model.addAttribute("message", t.getMessage());
            return "category-list-error";
        }
    }

    @RequestMapping(value = "category/add", method = RequestMethod.POST)
    public String addCategory(@ModelAttribute("form") CategoryDTO dto, Model model) {
        try {
            Optional.ofNullable(dto.getId())
                    .map(id -> categoryService.updateCategory(dto))
                    .orElseGet(() -> categoryService.createCategory(dto));
            return "redirect:/categories";
        } catch (Throwable t) {
            logger.error("Error during category add or update", t);
            model.addAttribute("message", t.getMessage());
            return "category-edit-error";
        }
    }

    @RequestMapping("category/edit/{id}")
    public String editCategory(@PathVariable("id") Long id, Model model) {
        try {
            CategoryDTO dto = Optional.ofNullable(categoryService.getById(id))
                    .map(CategoryDTO::from)
                    .orElse(new CategoryDTO());
            model.addAttribute("form", dto);
            model.addAttribute("categories", categoryService.getAll());
            return "category";
        } catch (Throwable t) {
            logger.error("Error during category edit", t);
            model.addAttribute("message", t.getMessage());
            return "category-edit-error";
        }
    }

    @RequestMapping("category/remove/{id}")
    public String removeCategory(@PathVariable("id") Long id, Model model) {
        try {
            categoryService.deleteCategory(id);
            return "redirect:/categories";
        } catch (Throwable t) {
            logger.error("Error during category delete", t);
            model.addAttribute("message", t.getMessage());
            return "category-delete-error";
        }
    }

}
