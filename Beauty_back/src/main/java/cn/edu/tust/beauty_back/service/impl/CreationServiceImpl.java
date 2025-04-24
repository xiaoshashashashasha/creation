package cn.edu.tust.beauty_back.service.impl;

import cn.edu.tust.beauty_back.bean.Class;
import cn.edu.tust.beauty_back.bean.Creation;
import cn.edu.tust.beauty_back.bean.PageBean;
import cn.edu.tust.beauty_back.bean.Tag;
import cn.edu.tust.beauty_back.mapper.ClassMapper;
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
    @Autowired
    private ClassMapper classMapper;

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
        PageBean<Creation> pb = new PageBean<>();

        PageHelper.startPage(pageNum, pageSize);
        List<Creation> list_1 = creationMapper.list(title, class_id);

        if (tag_id != null) {
            List<Integer> list_2 = creationMapper.getCIdByTId(tag_id);

            List<Creation> filteredList = list_1.stream()
                    .filter(creation -> list_2.contains(creation.getCreation_id()))
                    .collect(Collectors.toList());

            int total = filteredList.size();
            int fromIndex = Math.min((pageNum - 1) * pageSize, total);
            int toIndex = Math.min(fromIndex + pageSize, total);
            List<Creation> paged = filteredList.subList(fromIndex, toIndex);

            pb.setTotal(total);
            pb.setItems(paged);
            return pb;
        }

        // 没有 tag_id，直接走 PageHelper 分页
        Page<Creation> p = (Page<Creation>) list_1;
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
    public PageBean<Creation> myList(Integer pageNum, Integer pageSize, Integer user_id,Integer examine) {
        PageBean<Creation> pb = new PageBean<>();
        PageHelper.startPage(pageNum, pageSize);

        List<Creation> list = creationMapper.myList(user_id,examine);
        for (Creation creation : list){
            Class c = classMapper.findClassById(creation.getClass_id());
            creation.setClass_name(c.getClass_name());
        }

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
        for(Creation creation : list){
            Class c = classMapper.findClassById(creation.getClass_id());
            creation.setClass_name(c.getClass_name());
        }

        Page<Creation> p = (Page<Creation>) list;
        pb.setTotal(p.getTotal());
        pb.setItems(p.getResult());

        return pb;
    }

    @Override
    public PageBean<Creation> listToExamine(Integer pageNum, Integer pageSize, String title, Integer class_id, Integer tag_id, Integer examine) {
        PageBean<Creation> pb = new PageBean<>();

        // 启用 PageHelper 分页（只对 list_1 有效）
        PageHelper.startPage(pageNum, pageSize);
        List<Creation> list_1 = creationMapper.listToExamine(title, class_id, examine);

        if (tag_id != null) {

            List<Integer> matchIds = creationMapper.getCIdByTId(tag_id);

            List<Creation> filteredList = list_1.stream()
                    .filter(creation -> matchIds.contains(creation.getCreation_id()))
                    .collect(Collectors.toList());


            for (Creation creation : filteredList) {
                Class c = classMapper.findClassById(creation.getClass_id());
                creation.setClass_name(c.getClass_name());
            }

            // 手动分页 filteredList
            int total = filteredList.size();
            int fromIndex = Math.min((pageNum - 1) * pageSize, total);
            int toIndex = Math.min(fromIndex + pageSize, total);
            List<Creation> paged = filteredList.subList(fromIndex, toIndex);

            pb.setTotal(total);
            pb.setItems(paged);
            return pb;
        }

        // tag_id 为 null 的情况，保留 PageHelper 返回值
        for (Creation creation : list_1) {
            Class c = classMapper.findClassById(creation.getClass_id());
            creation.setClass_name(c.getClass_name());
        }

        // list_1 此时仍为 Page 对象（PageHelper 接管的）
        Page<Creation> p = (Page<Creation>) list_1;
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
    public void changeClass(Integer creation_id, Integer class_id) {
        creationMapper.updateClass(creation_id,class_id);
    }

}
