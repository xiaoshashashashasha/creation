package cn.edu.tust.beauty_back.service.impl;

import cn.edu.tust.beauty_back.mapper.HairStyleMapper;
import cn.edu.tust.beauty_back.service.HairStyleService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HairStyleServiceImpl implements HairStyleService {

    @Autowired
    private HairStyleMapper hairStyleMapper;

}
