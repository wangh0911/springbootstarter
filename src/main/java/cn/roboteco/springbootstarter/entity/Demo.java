package cn.roboteco.springbootstarter.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

@Data
@TableName("t_demo")
public class Demo extends Model<Demo> {
    @TableId
    private Long id;

    private String column1;
}
