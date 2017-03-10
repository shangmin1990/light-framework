package com.le.bigdata.core.fileupload;

/**
 * Created by benjamin on 16/7/18.
 */
public interface IFolderGenerator<T> {

    boolean exist(String path);

    /**
     * @param identity
     * @return
     */
    String hash(T identity);

    String getBasePath();

    String getServerURL();

    String getUploadPath(String fileName);

    String getDownloadURLBase();

    String getDownloadURL(String path);

}
