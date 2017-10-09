package catalog.services;

/**
 * File system operations
 */
public interface FileService {

    /**
     * Delete file by its file name.
     *
     * @param fileName file name to delete
     * @return result of operation: true if file was deleted successfully, otherwise - false.
     */
    boolean deleteFile(String fileName);

}
