
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class UserCreateTests {

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
    public  void createUserTest() {
        user = generator.random();
        ValidatableResponse response = client.create(user)
                .assertThat()
                .statusCode(200)
                .body("success", is(true))
                .body("user.email", equalTo(user.getEmail()))
                .body("user.name", equalTo(user.getName()));
        token = client.extractToken(response);
    }

    @Test
    public void doubleUserTest(){
        user = generator.random();
        ValidatableResponse response = client.create(user);
        token = client.extractToken(response);
        client.create(user)
        .assertThat()
        .statusCode(403)
        .body("success", is(false))
        .body( "message",equalTo("User already exists"));
    }

    @Test
    public void noEmailUserTest(){
        user = generator.noEmail();
        client.create(user)
                .assertThat()
                .statusCode(403)
                .body("success", is(false))
                .body("message",equalTo("Email, password and name are required fields"));
    }

}
