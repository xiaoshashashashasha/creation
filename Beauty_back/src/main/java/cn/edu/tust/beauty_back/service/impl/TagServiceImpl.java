package cn.edu.tust.beauty_back.service.impl;

import cn.edu.tust.beauty_back.bean.PageBean;
import cn.edu.tust.beauty_back.bean.Tag;
import cn.edu.tust.beauty_back.mapper.TagMapper;
import cn.edu.tust.beauty_back.service.TagService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
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
    public PageBean<Tag> list(Integer pageNum, Integer pageSize) {
        PageBean<Tag> pb = new PageBean<>();
        PageHelper.startPage(pageNum, pageSize);

        List<Tag> list = tagMapper.list();

        Page<Tag> p = (Page<Tag>) list;
        pb.setTotal(p.getTotal());
        pb.setItems(p.getResult());

        return pb;
    }

    @Override
    public void del(Integer tag_id) {
        tagMapper.del(tag_id);
    }

    @Override
    public Tag findByTagName(String tag_name) {
        return tagMapper.findTagByName(tag_name);
    }

    @Override
    public Tag findByTagId(Integer tag_id) {
        return tagMapper.findTagById(tag_id);
    }
}
