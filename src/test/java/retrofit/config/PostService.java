package retrofit.config;

import model.Post;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.List;

public interface PostService {

    @GET("/posts")
    Call<List<Post>> getAllPosts();

    @GET("/posts")
    Call<List<Post>> getListByParam(@Query("userId") int userId);

    @GET("/posts/{id}")
    Call<Post> getPostById(@Path("id") int postId);
}
