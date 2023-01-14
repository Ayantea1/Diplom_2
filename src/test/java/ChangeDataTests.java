import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class ChangeDataTests {

    private UserGenerator generator = new UserGenerator();
    private UserMethod client = new UserMethod();

    private User user;
    private String token;


    @After
    public void deleteUser(){
        if (token != null){
            client.delete(token);}
    }

    @Test
    public void newDataTest(){
        user = generator.random();
       ValidatableResponse response = client.create(user);
       token = client.extractToken(response);
      var data = generator.random();
       client.change(data,token)
       .assertThat()
       .statusCode(200)
       .body("user.email", equalTo(data.getEmail()))
       .body("user.name", equalTo(data.getName()));
    }

    @Test
    public void WithoutAuthChangeTest(){
        user = generator.random();
        ValidatableResponse response = client.create(user);
        token = client.extractToken(response);
        var data = generator.random();
        client.change(data,"")
                .assertThat()
                .statusCode(401)
                .body("message", equalTo("You should be authorised"))
                .body("success", is(false));
    }
}
