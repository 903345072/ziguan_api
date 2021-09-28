package com.stock.service.ServiceImpl;

import com.mapper.ConfigMapper;
import com.stock.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ConfigServiceImpl implements ConfigService {
    @Autowired
    ConfigMapper configMapper;
    @Override
    public List<Map> getConfig() {
        return configMapper.getConfig();
    }
}
