package cn.edu.tust.beauty_back.service.impl;

import cn.edu.tust.beauty_back.bean.Creation;
import cn.edu.tust.beauty_back.bean.PageBean;
import cn.edu.tust.beauty_back.bean.Tag;
import cn.edu.tust.beauty_back.mapper.CreationMapper;
import cn.edu.tust.beauty_back.mapper.TagMapper;
import cn.edu.tust.beauty_back.service.CreationService;
import cn.edu.tust.beauty_back.utils.ThreadLocalUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CreationServiceImpl implements CreationService {

    @Autowired
    private CreationMapper creationMapper;
    @Autowired
    private TagMapper tagMapper;

    @Override
    public void add(Creation creation) {
        //从线程中获取创建者id
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer user_id = (Integer) map.get("user_id");
        creation.setUser_id(user_id);
        creationMapper.add(creation);
    }

    @Override
    public void update(Creation creation) {
        //从线程中获取创建者id,确保更改者与作者为同一人
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer user_id = (Integer) map.get("user_id");
        creation.setUser_id(user_id);
        creationMapper.update(creation);
    }

    @Override
    public Creation getCreationByCId(int creation_id) {
        creationMapper.view(creation_id);
        Creation creation = creationMapper.getCreationByCId(creation_id);
        return creation;
    }

    @Override
    public PageBean<Creation> list(Integer pageNum, Integer pageSize, String title, Integer class_id, Integer tag_id) {

        //实现思路：1.根据title、class_id和examine交由mapper获取creation的list1
        //2.根据tag_id获取关联creation_id的list2
        //3.校验list1，将与list2中id对应的creation加入list3
        //4.封装list3提交
        PageBean<Creation> pb = new PageBean<>();
        PageHelper.startPage(pageNum, pageSize);

        List<Creation> list_1 = creationMapper.list(title,class_id);

        if(tag_id != null){
            List<Integer> list_2 = creationMapper.getCIdByTId(tag_id);
            List<Creation> list_3 = list_1.stream()
                    .filter(creation -> list_2.contains(creation.getCreation_id()))
                    .collect(Collectors.toList());

            Page<Creation> p = (Page<Creation>) list_3;
            pb.setTotal(p.getTotal());
            pb.setItems(p.getResult());

            return pb;
        }

        Page p = (Page) list_1;
        pb.setTotal(p.getTotal());
        pb.setItems(p.getResult());

        return pb;
    }

    @Override
    public void del(int creation_id) {
        //从线程中获取创建者id,确保删除者与作者为同一人
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer user_id = (Integer) map.get("user_id");
        creationMapper.del(creation_id,user_id);
    }

    @Override
    public PageBean<Creation> myList(Integer pageNum, Integer pageSize, Integer user_id) {
        PageBean<Creation> pb = new PageBean<>();
        PageHelper.startPage(pageNum, pageSize);

        List<Creation> list = creationMapper.myList(user_id);

        Page<Creation> p = (Page<Creation>) list;
        pb.setTotal(p.getTotal());
        pb.setItems(p.getResult());

        return pb;
    }

    @Override
    public PageBean<Creation> otherList(Integer pageNum, Integer pageSize, Integer user_id) {
        PageBean<Creation> pb = new PageBean<>();
        PageHelper.startPage(pageNum, pageSize);

        List<Creation> list = creationMapper.otherList(user_id);

        Page<Creation> p = (Page<Creation>) list;
        pb.setTotal(p.getTotal());
        pb.setItems(p.getResult());

        return pb;
    }

    @Override
    public PageBean<Creation> listToExamine(Integer pageNum, Integer pageSize, String title, Integer class_id, Integer tag_id, Integer examine) {
        PageBean<Creation> pb = new PageBean<>();
        PageHelper.startPage(pageNum, pageSize);

        List<Creation> list_1 = creationMapper.listToExamine(title,class_id,examine);

        if(tag_id != null){
            List<Integer> list_2 = creationMapper.getCIdByTId(tag_id);
            List<Creation> list_3 = list_1.stream()
                    .filter(creation -> list_2.contains(creation.getCreation_id()))
                    .collect(Collectors.toList());

            Page<Creation> p = (Page<Creation>) list_3;
            pb.setTotal(p.getTotal());
            pb.setItems(p.getResult());

            return pb;
        }

        Page p = (Page) list_1;
        pb.setTotal(p.getTotal());
        pb.setItems(p.getResult());

        return pb;
    }



    @Override
    public List<Tag> getTagsByCId(Integer creation_id) {
        List<Integer> list_Tid = creationMapper.getTIdByCId(creation_id);
        List<Tag> list = new ArrayList<>();
        for( Integer Tid : list_Tid){
            list.add(tagMapper.findTagById(Tid));
        }
        return list;
    }



    @Override
    public void connectTag(Integer creation_id, Integer tag_id) {
        creationMapper.connectTag(creation_id,tag_id);
    }

    @Override
    public void cancelConnect(Integer creation_id, Integer tag_id) {
        creationMapper.cancelConnect(creation_id,tag_id);
    }

    @Override
    public void examine(Integer creation_id, Integer examine, String review_comments) {
        creationMapper.updateExamine(creation_id,examine,review_comments);
    }

    @Override
    public void updateCover(String coverUrl, Integer creation_id) {
        creationMapper.updateCover(coverUrl, creation_id);
    }
}
