import allure.AllureAttachment;
import org.apache.http.HttpStatus;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import retrofit.config.PostService;
import retrofit.config.RetrofitConfig;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


public class RetrofitTest {
    private final Retrofit retrofitClient = RetrofitConfig.buildRetrofitClient();

    @Test(description = "Request for a list of all posts")
    public void allPostsTest() throws IOException {
        var postService = retrofitClient.create(PostService.class);
        var response = postService.getAllPosts().execute();
        assertThat(response.code(), equalTo(HttpStatus.SC_OK));
        assertThat(response.body(), notNullValue());
        AllureAttachment.attachResponse(response.body());
        assertThat(response.body(), hasSize(100));
    }

    @Parameters({"userId"})
    @Test(description = "Filtering by query parameters userId")
    public void filteringByQueryParamUserId(int userId) throws IOException {
        var postService = retrofitClient.create(PostService.class);
        var response = postService.getListByParam(userId).execute();
        assertThat(response.code(), equalTo(HttpStatus.SC_OK));
        assertThat(response.body(), notNullValue());
        assertThat(response.body().size(), is(greaterThan(0)));
        AllureAttachment.attachResponse(response.body());
        response.body().forEach(post -> assertThat(post.getUserId(), is(equalTo(userId))));
    }

    @Test(description = "Getting a resource by id - positive", dataProvider = "positiveId")
    public void gettingResourceById(int id) throws IOException {
        var postService = retrofitClient.create(PostService.class);
        var response = postService.getPostById(id).execute();
        assertThat(response.code(), equalTo(HttpStatus.SC_OK));
        assertThat(response.body(), notNullValue());
        AllureAttachment.attachResponse(Collections.singletonList(response.body()));
        assertThat(response.body().getId(), is(equalTo(id)));
    }

    @Test(description = "Getting a resource by id - negative", dataProvider = "negativeId")
    public void gettingResourceByIdNegative(int id) throws IOException {
        var postService = retrofitClient.create(PostService.class);
        var response = postService.getPostById(id).execute();
        assertThat(response.code(), equalTo(HttpStatus.SC_NOT_FOUND));
        assertThat(response.body(), nullValue());
        AllureAttachment.attachResponse(Collections.singletonList(response.body()));
    }

    @DataProvider(name = "positiveId")
    public static Object[][] positiveId() {
        return new Object[][]{{1}, {50}, {100}};
    }

    @DataProvider(name = "negativeId")
    public static Object[][] negativeId() {
        return new Object[][]{{-1}, {0}};
    }
}
