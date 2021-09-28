package com.mapper.frontend;

import com.stock.models.frontend.optional;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper
public interface OptionalMapper {
    int add_optional(optional optional);
    int delete_optional(optional optional);
    optional find_optional(HashMap<String,Object> map);
    List<String> get_my_optional(Integer uid);
}
