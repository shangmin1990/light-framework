package net.shmin.core.fileupload.impl;

import net.shmin.core.fileupload.AbstractFileUploadService;
import net.shmin.core.util.CmdProcessRunner;
import org.springframework.stereotype.Component;

/**
 * Created by benjamin on 16/7/18.
 * 使用rsync 命令进行文件传输
 */
@Component("rsync")
public class RsyncFileUploadServiceImpl extends AbstractFileUploadService {

    private static final String UPLOAD_COMMAND = "rsync -avzP %s %s";

    @Override
    public String uploadInternal(String local, String remoteFolder) {
        //rsync -avzP yanbo_uploadtest.csv ops.rsync.bigdata.le.com::NxEoJOkIisK0JdVyEsPUBBMBHMtNyGFj/yanbo_data/(文件名非必须，可以rename_file)
        // 首先要确定该目录是否存在(初始化一遍目录)

        String upload_cmd_str = String.format(UPLOAD_COMMAND, local, remoteFolder);
        CmdProcessRunner.runSellCmd(upload_cmd_str);
        return folderGenerator.getDownloadURL(remoteFolder.substring(remoteFolder.lastIndexOf('/') + 1));
    }

    @Override
    public boolean delete(String remoteFilePath) throws Exception {
        return false;
    }

    @Override
    public boolean exist(String remoteFilePath) {
        return false;
    }

    @Override
    public boolean isFile(String path) {
        return false;
    }
}
