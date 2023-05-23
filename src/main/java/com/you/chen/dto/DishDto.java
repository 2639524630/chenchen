package com.you.chen.dto;



import com.you.chen.pojo.Dish;
import com.you.chen.pojo.DishFlavor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

/**
 * 原本的dish不够用了，对dish类进行扩展
 */
@Data
public class DishDto extends Dish {
//   菜品对应的口味数据
    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
