package com.le.bigdata.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStreamReader;
import java.io.LineNumberReader;

public class CmdProcessRunner {
    private static Logger logger = LoggerFactory.getLogger(CmdProcessRunner.class);

    public static boolean runSellCmd(String input_shel_cmd_str) {
        String shell_cmd_str = "source /etc/profile && " + input_shel_cmd_str;
        boolean success = false;
        StringBuffer sb = new StringBuffer();

        Process process;

        try {
            logger.info("执行cmd命令{}", shell_cmd_str);

            process = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", shell_cmd_str}, null, null);                    //process = Runtime.getRuntime().exec(new String[] {"/usr/local/hadoop/bin/hadoop", shel_cmd_str }, null,null);
            InputStreamReader ir = new InputStreamReader(process.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            String line;

            process.waitFor();


            while ((line = input.readLine()) != null) {
                sb.append(line).append("\n");
            }
            String shell_cmd_result = sb.toString();

            input.close();
            input = null;
            ir.close();
            ir = null;

            int exitValue = process.exitValue();
            if (exitValue == 0) {
                success = true;
            } else {
                String log_str = String.format("cmd命令%s执行失败,返回代码%d", shell_cmd_result, exitValue);
                logger.error(log_str);
            }

        } catch (Exception e) {
            e.printStackTrace();
            String log_str = String.format("%s命令出现异常", shell_cmd_str);
            logger.error(log_str, e);
        }
        sb = null;
        process = null;

        return success;
    }
}
