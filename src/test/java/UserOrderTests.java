import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class UserOrderTests {

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
    public void noAuthTest(){
        client.getUserOrders("")
        .assertThat()
        .statusCode(401)
                .body("success",is(false))
                .body("message",equalTo("You should be authorised"));
    }

    @Test
    public void getUserOrderTest(){
        user = generator.random();
        ValidatableResponse response = client.create(user);
        token = client.extractToken(response);
        ingList = new Ingredients();
        ingList.ingredients = client.getIngrList();
        var orderNum = client.extractOrderNum(client.order(ingList,token));
        System.out.println(orderNum);
        client.getUserOrders(token)
        .assertThat()
        .statusCode(200)
        .body("success",is(true))
       .body("orders[0].number",equalTo(orderNum));
    }
}
