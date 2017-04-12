package net.shmin.core.fileupload;

import javax.annotation.Resource;
import java.io.File;

/**
 * Created by benjamin on 16/7/18.
 */
public abstract class AbstractFileUploadService implements IFileUploadService {

    @Resource(name = "uuidFileNameGenerator")
    protected IFileNameGenerator fileNameGenerator;

    @Resource(name = "simpleFolderGenerator")
    protected IFolderGenerator folderGenerator;

    @Override
    public void setFileNameGenerator(IFileNameGenerator fileNameGenerator) {
        this.fileNameGenerator = fileNameGenerator;
    }

    @Override
    public <T> void setFolderGenerator(IFolderGenerator<T> folderGenerator) {
        this.folderGenerator = folderGenerator;
    }

    @Override
    public String upload(String localFilePath) throws Exception {
        File file = new File(localFilePath);
        String fileName;
        if (file.isFile() && file.exists()) {
            fileName = file.getName();
        } else {
            throw new Exception("上传文件失败, 本地文件不是文件或者不存在");
        }
        String generatedFileName = fileNameGenerator.generate(fileName.substring(fileName.indexOf(".") + 1));
        String path = folderGenerator.getServerURL() + "/" + folderGenerator.getBasePath();
        return uploadInternal(localFilePath, path + generatedFileName);
    }

    public abstract String uploadInternal(String local, String remoteFolder);
}
