package com.le.bigdata.core.dto;

/**
 * Created by benjamin on 16/5/17.
 */
public class CommonPageDTO {

    private Integer pageSize;
    private Integer currentPage;
    private Integer totalPage;
    private Long count;
    private Object data;

    public CommonPageDTO() {

    }

    public CommonPageDTO(Integer totalPage, Object data) {
        this.totalPage = totalPage;
        this.data = data;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
