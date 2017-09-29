package catalog.services;

import catalog.domain.Category;
import catalog.domain.Product;
import catalog.dto.CategoryDTO;
import catalog.dto.ProductDTO;
import catalog.repositories.CategoryRepository;
import catalog.repositories.ProductRepository;
import catalog.services.Impl.CategoryServiceImpl;
import catalog.services.Impl.ProductServiceImpl;
import org.assertj.core.api.ArraySortedAssert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.in;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@Configuration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ProductServiceTest.class})
public class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private FileService fileService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Bean
    public ProductService productService() {
        return new ProductServiceImpl();
    }

    @Bean
    public FileService fileService() {
        return mock(FileService.class);
    }

    @Bean
    public ProductRepository productRepository() {
        return mock(ProductRepository.class);
    }

    @Bean
    public CategoryRepository categoryRepository() {
        return mock(CategoryRepository.class);
    }

    @Before
    public void setUp() {
        reset(
                fileService,
                productRepository,
                categoryRepository
        );
    }

    @Test
    public void testCreateProduct() {
        ProductDTO dto = new ProductDTO();
        dto.setName("test name");
        dto.setDescription("test description");
        dto.setManufacturer("test manufacturer");
        dto.setPrice(123.4);
        dto.setCreated(new Date());
        dto.setCategory("test category");

        Category category = new Category();
        category.setId(1L);
        category.setName("test category");
        category.setDescription("test description");
        when(categoryRepository.findOneByName(category.getName()))
                .thenReturn(category);

        Product product = new Product();
        product.setId(1L);
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setManufacturer(dto.getManufacturer());
        product.setPrice(dto.getPrice());
        product.setCreated(dto.getCreated());
        product.setCategory(category);
        when(productRepository.save(any(Product.class)))
                .thenReturn(product);

        Product result = productService.createProduct(dto);

        assertThat(result.getId()).isEqualTo(product.getId());
        assertThat(result.getName()).isEqualTo(product.getName());
        assertThat(result.getDescription()).isEqualTo(product.getDescription());
        assertThat(result.getManufacturer()).isEqualTo(product.getManufacturer());
        assertThat(result.getPrice()).isEqualTo(product.getPrice());
        assertThat(result.getCreated()).isEqualTo(product.getCreated());
        assertThat(result.getCategory()).isEqualToComparingFieldByField(category);

        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(captor.capture());

        Product saved = captor.getValue();
        assertThat(saved.getId()).isNull();
        assertThat(saved.getName()).isEqualTo(product.getName());
        assertThat(saved.getDescription()).isEqualTo(product.getDescription());
        assertThat(saved.getManufacturer()).isEqualTo(product.getManufacturer());
        assertThat(saved.getPrice()).isEqualTo(product.getPrice());
        assertThat(saved.getCreated()).isCloseTo(product.getCreated(), 1000);
        assertThat(saved.getCategory()).isEqualToComparingFieldByField(category);
    }

    @Test
    public void testUpdateProduct() {
        Category category = new Category();
        category.setId(1L);
        category.setName("test category");
        category.setDescription("test description");
        when(categoryRepository.findOneByName(category.getName()))
                .thenReturn(category);

        Product product = new Product();
        product.setId(1L);
        product.setName("test name");
        product.setDescription("test description");
        product.setManufacturer("test manufacturer");
        product.setPrice(123.4);
        product.setCreated(new Date());
        product.setCategory(category);
        when(productRepository.findOne(product.getId()))
                .thenReturn(product);

        Category newCategory = new Category();
        newCategory.setId(2L);
        newCategory.setName("new category");
        newCategory.setDescription("new description");
        when(categoryRepository.findOneByName(newCategory.getName()))
                .thenReturn(newCategory);

        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName("new name");
        dto.setDescription("new description");
        dto.setManufacturer("new manufacturer");
        dto.setPrice(333.3);
        dto.setCreated(new Date());
        dto.setCategory(newCategory.getName());

        Product newProduct = new Product();
        newProduct.setId(dto.getId());
        newProduct.setName(dto.getName());
        newProduct.setDescription(dto.getDescription());
        newProduct.setManufacturer(dto.getManufacturer());
        newProduct.setPrice(dto.getPrice());
        newProduct.setCreated(product.getCreated());
        newProduct.setCategory(newCategory);
        when(productRepository.save(any(Product.class)))
                .thenReturn(newProduct);

        Product result = productService.updateProduct(dto);

        assertThat(result.getId()).isEqualTo(dto.getId());
        assertThat(result.getName()).isEqualTo(dto.getName());
        assertThat(result.getDescription()).isEqualTo(dto.getDescription());
        assertThat(result.getManufacturer()).isEqualTo(dto.getManufacturer());
        assertThat(result.getPrice()).isEqualTo(dto.getPrice());
        assertThat(result.getCreated()).isEqualTo(product.getCreated());
        assertThat(result.getCategory()).isEqualToComparingFieldByField(newCategory);

        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(captor.capture());

        Product saved = captor.getValue();
        assertThat(saved.getId()).isEqualTo(dto.getId());
        assertThat(saved.getName()).isEqualTo(dto.getName());
        assertThat(saved.getDescription()).isEqualTo(dto.getDescription());
        assertThat(saved.getManufacturer()).isEqualTo(dto.getManufacturer());
        assertThat(saved.getPrice()).isEqualTo(dto.getPrice());
        assertThat(saved.getCreated()).isCloseTo(dto.getCreated(), 1000);
        assertThat(saved.getCategory()).isEqualToComparingFieldByField(newCategory);
    }

    @Test
    public void testDeleteProduct() {
        Long id = 1L;
        productService.deleteProduct(id);
        verify(productRepository).delete(id);
    }

    @Test
    public void testGetProductsWithImages() {
        Product p1 = new Product();
        p1.setImage("image 1 url");
        Product p2 = new Product();
        p2.setImage("image 2 url");
        List<Product> products = new ArrayList<>(Arrays.asList(p1, p2));
        when(productRepository.findAllByImageIsNotNullOrderById())
                .thenReturn(products);

        List<Product> result = productService.getAllWithImages();

        assertThat(result).containsOnlyElementsOf(products);
        verify(productRepository).findAllByImageIsNotNullOrderById();
    }

    @Test
    public void testAddImageToProduct() {
        Category category = new Category();
        category.setId(1L);
        category.setName("test category");
        category.setDescription("test description");

        Product product = new Product();
        product.setId(1L);
        product.setName("test name");
        product.setDescription("test description");
        product.setManufacturer("test manufacturer");
        product.setPrice(123.4);
        String oldImage = "old image";
        product.setImage(oldImage);
        product.setCreated(new Date());
        product.setCategory(category);

        when(productRepository.findOneByName(product.getName()))
                .thenReturn(product);

        String newImage = "new image";

        productService.addImageToProduct(product.getName(), newImage);

        verify(fileService).deleteFile(oldImage);

        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(captor.capture());

        Product saved = captor.getValue();
        assertThat(saved.getId()).isEqualTo(product.getId());
        assertThat(saved.getName()).isEqualTo(product.getName());
        assertThat(saved.getDescription()).isEqualTo(product.getDescription());
        assertThat(saved.getManufacturer()).isEqualTo(product.getManufacturer());
        assertThat(saved.getPrice()).isEqualTo(product.getPrice());
        assertThat(saved.getImage()).isEqualTo(newImage);
        assertThat(saved.getCreated()).isEqualTo(product.getCreated());
        assertThat(saved.getCategory()).isEqualToComparingFieldByField(product.getCategory());
    }

}
