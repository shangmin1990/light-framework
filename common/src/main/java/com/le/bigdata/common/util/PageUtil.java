package com.le.bigdata.common.util;

import com.github.pagehelper.Page;
import com.le.bigdata.core.dto.CommonPageDTO;

import java.util.List;

/**
 * Created by benjamin on 2017/4/1.
 */
public class PageUtil {
    public static <T> CommonPageDTO convertList2Page(List<T> list, int page, int size) {

        if (list != null) {
            CommonPageDTO commonPageDTO = new CommonPageDTO();
            Page<T> pages = (Page<T>) list;
            commonPageDTO.setCurrentPage(page);
            commonPageDTO.setPageSize(size);
            commonPageDTO.setTotalPage(pages.getPages());
            commonPageDTO.setData(list);
            commonPageDTO.setCount(pages.getTotal());
            return commonPageDTO;
        }
        return null;
    }
}
