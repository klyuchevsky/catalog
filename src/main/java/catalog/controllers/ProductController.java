package catalog.controllers;

import catalog.domain.Product;
import catalog.dto.CategoryDTO;
import catalog.dto.FilterDTO;
import catalog.dto.ProductDTO;
import catalog.services.CategoryService;
import catalog.services.ProductService;
import org.apache.tomcat.util.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @RequestMapping("/")
    public String index(Model model) {
        try {
            model.addAttribute("form", new ProductDTO());
            model.addAttribute("filterForm", new FilterDTO());

            List<CategoryDTO> categories = categoryService.getAll().stream()
                    .map(CategoryDTO::from)
                    .collect(Collectors.toList());
            categories.add(0, null);
            model.addAttribute("categories", categories);

            List<ProductDTO> products = productService.getAll().stream()
                    .map(ProductDTO::from)
                    .collect(Collectors.toList());
            model.addAttribute("products", products);

            return "index";
        } catch (Throwable t) {
            logger.error("Error during get products list", t);
            model.addAttribute("message", t.getMessage());
            return "product-list-error";
        }
    }

    @RequestMapping(value = "product/category/", method = RequestMethod.GET)
    public String filterProduct(@RequestParam("name") String name, Model model) {
        try {
            model.addAttribute("form", new ProductDTO());
            model.addAttribute("filterForm", new FilterDTO());

            List<CategoryDTO> categories = categoryService.getAll().stream()
                    .map(CategoryDTO::from)
                    .collect(Collectors.toList());
            categories.add(0, null);
            model.addAttribute("categories", categories);

            List<Product> products = Optional.ofNullable(name)
                    .filter(StringUtils::hasText)
                    .map(productService::getByCategory)
                    .orElseGet(productService::getAll);
            List<ProductDTO> productsDTO =products.stream()
                    .map(ProductDTO::from)
                    .collect(Collectors.toList());

            model.addAttribute("products", productsDTO);
            model.addAttribute("filterBy", name);

            return "index";
        } catch (Throwable t) {
            logger.error("Error during get products list", t);
            model.addAttribute("message", t.getMessage());
            return "product-list-error";
        }
    }

    @RequestMapping(value = "product/add", method = RequestMethod.POST)
    public String addProduct(@ModelAttribute("form") ProductDTO dto, Model model) {
        try {
            Optional.ofNullable(dto.getId())
                    .map(id -> productService.updateProduct(dto))
                    .orElseGet(() -> productService.createProduct(dto));

            return "redirect:/";
        } catch (Throwable t) {
            logger.error("Error during product add or update", t);
            model.addAttribute("message", t.getMessage());
            return "product-edit-error";
        }
    }

    @RequestMapping("product/edit/{id}")
    public String editProduct(@PathVariable("id") Long id, Model model) {
        try {
            ProductDTO dto = Optional.ofNullable(productService.getById(id))
                    .map(ProductDTO::from)
                    .orElse(new ProductDTO());
            model.addAttribute("form", dto);
            model.addAttribute("filterForm", new FilterDTO());

            String selectedCategory = dto.getCategory();
            model.addAttribute("selectedCategory", selectedCategory);

            List<CategoryDTO> categories = categoryService.getAll().stream()
                    .map(CategoryDTO::from)
                    .collect(Collectors.toList());
            categories.add(0, null);
            model.addAttribute("categories", categories);

            model.addAttribute("products", new ArrayList<>(Collections.singletonList(dto)));

            return "index";
        } catch (Throwable t) {
            logger.error("Error during product add or update", t);
            model.addAttribute("message", t.getMessage());
            return "product-edit-error";
        }
    }

    @RequestMapping("product/remove/{id}")
    public String removeProduct(@PathVariable("id") Long id, Model model) {
        try {
            productService.deleteProduct(id);
            return "redirect:/";
        } catch (Throwable t) {
            logger.error("Error during product delete", t);
            model.addAttribute("message", t.getMessage());
            return "product-delete-error";
        }
    }

}
