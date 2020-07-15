package allure;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Allure;
import model.Post;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class AllureAttachment {

    public static void attachResponse(List<Post> list) {
        var mapper = new ObjectMapper();
        try {
            attachJson("response body", mapper.writeValueAsString(list));
        } catch (IOException e) {
            e.printStackTrace();
            throw new AssertionError("Incorrect attachment data " + e.getMessage());
        }
    }

    public static void attachText(String title, String text) {
        Allure.getLifecycle().addAttachment(title, "text/plain", "txt", text.getBytes(StandardCharsets.UTF_8));
    }

    public static void attachJson(String title, String text) {
        Allure.getLifecycle().addAttachment(title, "application/json", "json", text.getBytes(StandardCharsets.UTF_8));
    }
}
