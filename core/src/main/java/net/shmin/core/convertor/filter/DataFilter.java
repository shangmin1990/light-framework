package net.shmin.core.convertor.filter;


import com.alibaba.fastjson.JSONArray;


/**
 * 取出的数据已经可以显示了,但不满足显示的要求.(比如 脏数据,没有百分比的数据,需要合并某几个字段)
 * 如果你的数据需要通过原始数据计算(比如获取百分比之类), 请实现此接口
 */
public interface DataFilter {

    /**
     * 过滤数据
     *
     * @param array
     * @return [{key:1,value:2,percent:3},key:1,value:2,percent:3},key:1,value:2,percent:3}] 与keyMap中key与header的对应关系
     */
    JSONArray filter(JSONArray array);

}
