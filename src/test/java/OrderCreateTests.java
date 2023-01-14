import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;

import java.util.ArrayList;

import static org.hamcrest.Matchers.*;

public class OrderCreateTests {

    private UserGenerator generator = new UserGenerator();
    private UserMethod client = new UserMethod();

    private User user;
    private String token;
    private Ingredients ingList;


    @After
    public void deleteUser(){
        if (token != null){
            client.delete(token);}
    }

    @Test
    public void noIngredientsTest(){
        ingList = new Ingredients();
        client.order(ingList,"")
        .assertThat()
        .statusCode(400)
        .body("success",is(false))
        .body("message",equalTo("Ingredient ids must be provided"));
    }

    @Test
    public void noAuthTest(){
        ingList = new Ingredients();
       ingList.ingredients = client.getIngrList();
        client.order(ingList,"")
                .assertThat()
                .statusCode(200)
                .body("success",is(true))
                .body("name",notNullValue())
                .body("order.owner", equalTo(null));
    }

    @Test
    public void AuthTest(){
        user = generator.random();
       ValidatableResponse response = client.create(user);
        token = client.extractToken(response);
        ingList = new Ingredients();
        ingList.ingredients = client.getIngrList();
        client.order(ingList,token)
                .assertThat()
                .statusCode(200)
                .body("success",is(true))
                .body("name",notNullValue())
                .body("order.owner",notNullValue());
    }

    @Test
    public void  invalidIngredientTest(){
        ingList = new Ingredients();
        ingList.ingredients = new ArrayList<>();
        ingList.ingredients.add("63c2f4c3c161da001b7ac111");
        client.order(ingList,"")
                .assertThat()
                .statusCode(500) //согласно спеке
                .body("success",is(false))
                .body("message",equalTo("One or more ids provided are incorrect"));
    }
}
