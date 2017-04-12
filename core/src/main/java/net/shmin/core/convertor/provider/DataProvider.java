package net.shmin.core.convertor.provider;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by benjamin on 2017/1/11.
 */
public interface DataProvider {

    Object provideData(JSONObject object) throws Exception;

}
