package com.eying.pcss.workflow.config;

import com.eying.pcss.core.exception.SystemException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.IOException;
import java.util.List;

/**
 * List转换类型，用来实现List类型与Postgre类型的转换
 */
@Converter
public class ListJsonConverter implements AttributeConverter<List<String>, String> {
    /**
     * 转换成数据库列
     * @param attribute
     * @return
     */
    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        if (attribute == null || attribute.size() == 0) {
            return "[]";
        }
        ObjectMapper mapper = new ObjectMapper();
        String json = null;
        try {
            json = mapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new SystemException(e);
        }
        return json;
    }

    /**
     * 转换成实体属性
     * @param dbData
     * @return
     */
    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        ObjectMapper mapper = new ObjectMapper();
        List<String> list = null;
        try {
            list =  mapper.readValue(dbData, new TypeReference<List<String>>(){});
        } catch (IOException e) {
            throw new SystemException(e);
        }
        return list;
    }
}
