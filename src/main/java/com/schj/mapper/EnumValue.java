package com.schj.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Descriptioin EnumValue
 * @Author AvA
 * @Date 2025-02-21
 */
@Mapper
public interface EnumValue {
    @Select("select enum_name from enum_value where enum_code = #{enum_code}")
    String getEnumByCode(String enum_code);
}
