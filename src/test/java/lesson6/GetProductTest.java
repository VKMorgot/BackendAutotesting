package lesson6;

import lesson6.db.dao.ProductsMapper;
import lesson6.db.model.ProductsExample;
import lesson6.dto.Product;
import lombok.SneakyThrows;
import org.apache.ibatis.session.SqlSession;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import retrofit2.Response;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class GetProductTest extends CommonProductTest {

    /**
     * Проверяем, что по заданному id=1 выдается искомый продукт
     */
    @SneakyThrows
    @Test
    void getProductById() {
        int id = 1;

        Response<Product> response = productService.getProductById(id).execute();

        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        assertThat(response.body().getId(), CoreMatchers.is(id));
        assertThat(response.body().getCategoryTitle(), CoreMatchers.equalTo("Food"));
        assertThat(response.body().getTitle(), CoreMatchers.equalTo("Milk"));

        //проверяем, что в БД с таким же id находится этот же продукт с такими же параметрами
        try (SqlSession session = getSqlSessionFactory().openSession()) {

            ProductsMapper productsMapper = session.getMapper(ProductsMapper.class);
            ProductsExample example = new ProductsExample();

            example.createCriteria()
                    .andCategory_idEqualTo((long) 1)       // id=1 категории = Food
                    .andTitleEqualTo("Milk");

            assertThat(productsMapper.countByExample(example), equalTo(1L)); // по id найден один продукт
            assertThat(productsMapper.selectByExample(example).get(0).getTitle(), equalTo("Milk"));  // имя продукта
            assertThat(productsMapper.selectByExample(example).get(0).getCategory_id(), equalTo(1L));  // принадлежность к категории
        }
    }

    /**
     * Проверяем, что по несуществующему id = 1000 вернется 404 ошибка
     */
    @SneakyThrows
    @Test
    void getProductByIdNotExist() {
        int id = 1000;

        Response<Product> response = productService.getProductById(id).execute();

        assertThat(response.code(), CoreMatchers.is(404));

        //проверяем, что такого элемента в БД нет
        try (SqlSession session = getSqlSessionFactory().openSession()) {

            ProductsMapper productsMapper = session.getMapper(ProductsMapper.class);
            ProductsExample example = new ProductsExample();

            example.createCriteria().andIdEqualTo((long) id);
            assertThat(productsMapper.countByExample(example), equalTo(0L));
        }
    }

}
