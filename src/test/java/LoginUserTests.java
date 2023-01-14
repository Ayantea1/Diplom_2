import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class LoginUserTests {
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
    public void loginSuccessTest(){
        user = generator.random();
        ValidatableResponse response = client.create(user);
        token = client.extractToken(response);
        client.login(user)
                .assertThat()
                .statusCode(200)
                .body("success",is(true))
                .body("user.email", equalTo(user.getEmail()))
                .body("user.name", equalTo(user.getName()));
    }

    @Test
    public void loginFailTest(){
        user = generator.random();
        client.login(user)
                .assertThat()
                .statusCode(401)
                .body("success", is(false))
                .body("message",equalTo("email or password are incorrect"));
    }
}
