package catalog.controllers;

import catalog.dto.ProductDTO;
import catalog.services.ProductService;
import org.apache.commons.io.FilenameUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
public class FileController {

    private Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private ProductService productService;

    @Autowired
    private Environment env;

    public static String filesDirectory;

    @PostConstruct
    private void init() {
        filesDirectory = env.getProperty("files.dir");
    }

    @RequestMapping("file/upload")
    public String files(Model model) {
        try {
            model.addAttribute("product", "");

            List<ProductDTO> products = productService.getAll().stream()
                    .map(ProductDTO::from)
                    .collect(Collectors.toList());
            model.addAttribute("products", products);

            List<ProductDTO> productsWithImage = productService.getAllWithImages().stream()
                    .map(ProductDTO::from)
                    .collect(Collectors.toList());
            model.addAttribute("productsWithImage", productsWithImage);
            return "file-upload";
        } catch (Throwable t) {
            logger.error("Error during get product images list", t);
            model.addAttribute("message", t.getMessage());
            return "file-upload-error";
        }
    }

    @RequestMapping(value = "upload", method = RequestMethod.POST)
    public String upload(HttpServletRequest request, Model model) {
        try {
            Part filePart = request.getPart("file");
            String fileName = filePart.getSubmittedFileName();
            String extension = FilenameUtils.getExtension(fileName);

            String uploadedFileName = UUID.randomUUID() + "." + extension;
            String path = filesDirectory + uploadedFileName;

            try (InputStream fileContent = filePart.getInputStream(); OutputStream out = new FileOutputStream(path)) {
                IOUtils.copy(fileContent, out);
            }

            String product = request.getParameter("product");
            productService.addImageToProduct(product, uploadedFileName);

            return "redirect:file/upload";
        } catch (Throwable t) {
            logger.error("Error during file upload", t);
            model.addAttribute("message", t.getMessage());
            return "file-upload-error";
        }
    }

    @RequestMapping(value = "download/{fileName}", method = RequestMethod.GET)
    public void download(@PathVariable("fileName") String fileName, @RequestParam(name = "ext") String ext, HttpServletResponse response) {
        try {
            File file = new File(filesDirectory + fileName + "." + ext);
            try (InputStream in = new FileInputStream(file); OutputStream out = response.getOutputStream()) {
                IOUtils.copy(in, out);
            }
        } catch (Throwable t) {
            logger.error("Error during file download", t);
        }
    }

}
