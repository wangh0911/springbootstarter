package cn.roboteco.springbootstarter;

import javax.annotation.Resource;

import cn.roboteco.springbootstarter.config.PropertyConfig;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

    @Resource
    private PropertyConfig propertyConfig;

}
