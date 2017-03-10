package com.le.bigdata.core.fileupload;

/**
 * Created by benjamin on 16/7/18.
 */
public interface IFileUploadService {

    /**
     * 返回改文件的web路径
     *
     * @param localFilePath
     * @return
     */
    String upload(String localFilePath) throws Exception;

    /**
     * 删除指定文件或目录
     *
     * @param remoteFilePath
     * @return
     */
    boolean delete(String remoteFilePath) throws Exception;

    /**
     * 指定文件是否存在
     *
     * @param remoteFilePath
     * @return
     */
    boolean exist(String remoteFilePath);

    /**
     * 是否是文件
     *
     * @param path
     * @return
     */
    boolean isFile(String path);

    /**
     * 文件名称生成器
     *
     * @return
     */
    void setFileNameGenerator(IFileNameGenerator fileNameGenerator);

    /**
     * 文件目录生成器
     *
     * @return
     */
    <T> void setFolderGenerator(IFolderGenerator<T> folderGenerator);

}
