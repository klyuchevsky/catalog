package catalog.services.Impl;

import catalog.controllers.FileController;
import catalog.services.FileService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * File system operations
 */
@Service
public class FileServiceImpl implements FileService {
    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    /**
     * Delete file by its file name.
     *
     * @param fileName file name to delete
     * @return result of operation: true if file was deleted successfully, otherwise - false.
     */
    @Override
    public boolean deleteFile(String fileName) {
        try {
            File file = new File(FileController.filesDirectory + fileName);
            return file.delete();
        } catch (Exception e) {
            logger.error("Error during file delete", e);
            return false;
        }
    }

}
