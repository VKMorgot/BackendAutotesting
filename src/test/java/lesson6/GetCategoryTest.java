package lesson6;

import lesson6.api.CategoryService;
import lesson6.dto.GetCategoryResponse;
import lesson6.utils.RetrofitUtils;
import lombok.SneakyThrows;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import retrofit2.Response;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class GetCategoryTest {

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
        //todo проверить совпадение числа продуктов данной категории
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
    }
}
