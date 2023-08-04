package lesson7;

import lombok.SneakyThrows;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;

public class CommonTest {

    private static final String RESOURCE = "mybatis-config.xml";

    @SneakyThrows
    static SqlSessionFactory getSqlSessionFactory() {
        InputStream inputStream = Resources.getResourceAsStream(RESOURCE);
        return new
                SqlSessionFactoryBuilder().build(inputStream);
    }
}
