package com.mapper;



import com.stock.models.HeYue;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

@Mapper
public interface ConfigMapper {
    List<Map> getConfig();
}
