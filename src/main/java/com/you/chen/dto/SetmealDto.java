package com.you.chen.dto;



import com.you.chen.pojo.Setmeal;
import com.you.chen.pojo.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
