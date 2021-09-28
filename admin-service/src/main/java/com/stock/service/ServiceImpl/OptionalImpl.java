package com.stock.service.ServiceImpl;

import com.mapper.HeYueMapper;
import com.mapper.frontend.OptionalMapper;
import com.stock.models.frontend.optional;
import com.stock.service.OptionalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OptionalImpl implements OptionalService {
    @Autowired
    OptionalMapper OptionalMapper;
    @Override
    public int add_optional(optional op) {
        return OptionalMapper.add_optional(op);
    }

    @Override
    public int delete_optional(optional op) {
        return OptionalMapper.delete_optional(op);
    }

    @Override
    public optional find_optional(HashMap<String,Object> map) {
        return OptionalMapper.find_optional(map);
    }

    @Override
    public List<String> get_my_optional(Integer uid) {
        return OptionalMapper.get_my_optional(uid);
    }
}
