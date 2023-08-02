package lesson6;

import lesson6.api.CategoryService;
import lesson6.db.dao.CategoriesMapper;
import lesson6.db.dao.ProductsMapper;
import lesson6.db.model.Categories;
import lesson6.db.model.CategoriesExample;
import lesson6.db.model.Products;
import lesson6.db.model.ProductsExample;
import lesson6.dto.GetCategoryResponse;
import lesson6.utils.RetrofitUtils;
import lombok.SneakyThrows;
import org.apache.ibatis.session.SqlSession;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import retrofit2.Response;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class GetCategoryTest extends CommonTest {

    static CategoryService categoryService;

    @BeforeAll
    static void BeforeAll() {
        categoryService = RetrofitUtils.getRetrofit().create(CategoryService.class);
    }

    /**
     * Проверяем, что по заданному id=1 выдается искомая категория с заданными параметрами
     */
    @SneakyThrows
    @Test
    void getCategoryById() {
        int id = 1;
        String categoryTitle = "Food";

        Response<GetCategoryResponse> response = categoryService.getCategory(id).execute();

        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        assert response.body() != null;
        assertThat(response.body().getId(), equalTo(id));
        assertThat(response.body().getTitle(), equalTo(categoryTitle));
        response.body().getProducts().forEach(product ->
                assertThat(product.getCategoryTitle(), equalTo(response.body().getTitle())));

        //проверяем совпадение числа продуктов в данной категории: api vs db
        try (SqlSession session = getSqlSessionFactory().openSession()) {

            ProductsMapper productsMapper = session.getMapper(ProductsMapper.class);
            ProductsExample example = new ProductsExample();

            example.createCriteria().andCategory_idEqualTo((long) id);
            List<Products> list = productsMapper.selectByExample(example);

            assertThat(response.body().getProducts().size(), equalTo(list.size()));
        }
    }

    /**
     * Проверяем, что по несуществующему в базе id=3 вернется 404 код возврата
     */
    @SneakyThrows
    @Test
    void getCategoryByIdNotExist() {
        int id = 3;

        Response<GetCategoryResponse> response = categoryService.getCategory(id).execute();

        assertThat(response.code(), CoreMatchers.equalTo(404));

        // проверяем, что в db нет id = 3
        try (SqlSession session = getSqlSessionFactory().openSession()) {

            CategoriesMapper categoriesMapper = session.getMapper(CategoriesMapper.class);
            CategoriesExample example = new CategoriesExample();

            example.createCriteria().andIdEqualTo((long) id);
            List<Categories> list = categoriesMapper.selectByExample(example);

            assertThat(list.size(), equalTo(0));
        }
    }
}
