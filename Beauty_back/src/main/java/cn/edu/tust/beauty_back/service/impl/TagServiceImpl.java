package cn.edu.tust.beauty_back.service.impl;

import cn.edu.tust.beauty_back.bean.Tag;
import cn.edu.tust.beauty_back.mapper.TagMapper;
import cn.edu.tust.beauty_back.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagMapper tagMapper;

    @Override
    public void add(Tag tag) {
        tagMapper.add(tag);
    }

    @Override
    public List<Tag> list() {
        List<Tag> list = tagMapper.list();
        return List.of();
    }

    @Override
    public void del(Integer tag_id) {
        tagMapper.del(tag_id);
    }
}
