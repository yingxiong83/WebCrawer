package cn.itcast.jingdong.service;

import cn.itcast.jingdong.pojo.Item;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ItemService {

    void save(Item item);

    List<Item> findAll(Item item);

}
