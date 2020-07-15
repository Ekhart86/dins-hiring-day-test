import io.restassured.specification.RequestSpecification;
import model.Post;
import org.apache.http.HttpStatus;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import restassured.config.RestAssuredConfig;

import java.util.Arrays;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class RestAssuredTest {

    private final RequestSpecification requestSpecification = RestAssuredConfig.getRequestSpecification();

    @Test(description = "Request for a list of all posts")
    public void allPostsTest() {
        var response = given().spec(requestSpecification).get("/posts");
        assertThat(response.statusCode(), equalTo(HttpStatus.SC_OK));
        assertThat(response.body(), notNullValue());
        Post[] posts = response.body().as(Post[].class);
        assertThat(Arrays.asList(posts), hasSize(100));
    }

    @Parameters({"userId"})
    @Test(description = "Filtering by query parameters userId")
    public void filteringByQueryParamUserId(int userId) {
        var response = given()
                .spec(requestSpecification)
                .queryParam("userId", userId)
                .get("/posts");
        assertThat(response.statusCode(), equalTo(HttpStatus.SC_OK));
        assertThat(response.body(), notNullValue());
        Post[] posts = response.body().as(Post[].class);
        assertThat(posts.length, is(greaterThan(0)));
        Arrays.asList(posts).forEach(post -> assertThat(post.getUserId(), is(equalTo(userId))));
    }

    @Test(description = "Getting a resource by id - positive", dataProvider = "positiveId")
    public void gettingResourceByIdPositive(int id) {
        var response = given()
                .spec(requestSpecification)
                .pathParam("id", id)
                .get("/posts/{id}");
        assertThat(response.statusCode(), equalTo(HttpStatus.SC_OK));
        assertThat(response.body(), notNullValue());
        Post post = response.body().as(Post.class);
        assertThat(post.getId(), is(equalTo(id)));
    }

    @Test(description = "Getting a resource by id -negative", dataProvider = "negativeId")
    public void gettingResourceByIdNegative(int id) {
        given().spec(requestSpecification)
                .pathParam("id", id)
                .get("/posts/{id}").then().statusCode(HttpStatus.SC_NOT_FOUND);
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
