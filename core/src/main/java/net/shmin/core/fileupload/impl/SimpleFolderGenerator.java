package net.shmin.core.fileupload.impl;

import net.shmin.core.fileupload.IFolderGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by benjamin on 16/7/18.
 */
@Component("simpleFolderGenerator")
public class SimpleFolderGenerator implements IFolderGenerator<Integer> {

    @Value("${fileupload.server.url}")
    private String uploadServerUrl;

    @Value("${fileupload.server.path}")
    private String basePath;

    @Value("${download.http.url}")
    private String downloadServerUrl;

    @Override
    public boolean exist(String path) {
        return false;
    }

    @Override
    public String hash(Integer identity) {
        return "";
    }

    @Override
    public String getServerURL() {
        return uploadServerUrl;
    }

    @Override
    public String getBasePath() {
        return basePath;
    }

    @Override
    public String getUploadPath(String fileName) {
        return uploadServerUrl + '/' + basePath + fileName;
    }

    @Override
    public String getDownloadURLBase() {
        return downloadServerUrl;
    }

    @Override
    public String getDownloadURL(String path) {
        return downloadServerUrl + path;
    }
}
