package com.le.bigdata.core.fileupload.impl;

import com.le.bigdata.core.fileupload.IFileNameGenerator;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Created by benjamin on 16/6/23.
 */
@Component("uuidFileNameGenerator")
public class UUIDFileNameGenerator implements IFileNameGenerator {
    @Override
    public String generate(String suffix) {
        return UUID.randomUUID().toString() + "." + suffix;
    }
}
