package net.shmin.common.advice;

import net.shmin.core.Constant;
import net.shmin.core.dto.CommonResponseDTO;
import net.shmin.core.exception.BusinessServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

/**
 * Created by benjamin on 16/8/31.
 */
@ControllerAdvice
public class ControllerAspect implements Constant {

    private static Logger logger = LoggerFactory.getLogger(ControllerAspect.class);

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(List.class, new ListPropertyValueEditor());
    }

    @ExceptionHandler
    public void handleException(HttpServletRequest request, HttpServletResponse response, Exception e) {
        e.printStackTrace();
        responseErrorForAjax(response, e);
    }

    private void responseErrorForAjax(HttpServletResponse response, Exception e) {
//        e.printStackTrace();
        PrintWriter printWriter = null;
        try {
            printWriter = response.getWriter();
            response.setCharacterEncoding(Constant.CHARSET_UTF8);
            response.setContentType("application/json;charset=utf-8");
            int errorCode = 500;
            if(e instanceof BusinessServiceException){
                BusinessServiceException ex = (BusinessServiceException) e;
                errorCode = ex.getCode();
//                response.setStatus(errorCode);
            } else {
                // 其他错误一律是500
                response.setStatus(errorCode);
            }
            CommonResponseDTO commonResponseDTO = CommonResponseDTO.error(errorCode, e.toString());
            printWriter.write(commonResponseDTO.toString());
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            if (printWriter != null) {
                printWriter.flush();
                printWriter.close();
            }
        }
    }

    private class ListPropertyValueEditor extends PropertyEditorSupport {

        @Override
        public void setAsText(String text) throws IllegalArgumentException {
            String[] arrays = text.split("-");
            List<String> list = Arrays.asList(arrays);
            setValue(list);
        }
    }
}
