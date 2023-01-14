import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import java.util.ArrayList;

import static io.restassured.RestAssured.given;

public class UserMethod {

    private static final String USER_API = "/api/auth/";
    private static final String ORDER_API = "/api/orders";
    private static final String BASE_URI = "https://stellarburgers.nomoreparties.site";

    public RequestSpecification spec(){
        return given().log().all()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URI);
    }

    public ValidatableResponse create(User user){
       return spec()
                .body(user)
                .when()
                .post(USER_API + "register")
                .then().log().all();
    }

    public String extractToken(ValidatableResponse response){
       return response.extract()
                .path("accessToken");
    }

    public void delete(String token){
       spec()
                .auth().oauth2(token.replace("Bearer ", ""))
                .when()
                .delete(USER_API + "user")
                .then().log().all();
    }

    public ValidatableResponse login(User user) {
        return spec()
                .body(user)
                .when()
                .post(USER_API + "login")
                .then().log().all();
    }

    public ValidatableResponse change(User data, String token) {
        return spec()
                .auth().oauth2(token.replace("Bearer ", ""))
                .and()
                .body(data)
                .when()
                .patch(USER_API + "user")
                .then().log().all();
    }

    public ValidatableResponse order(Ingredients ingredients, String token) {
        return  spec()
                .auth().oauth2(token.replace("Bearer ", ""))
                .and()
                .body(ingredients)
                .when()
                .post(ORDER_API)
                .then().log().all();
    }

    public ArrayList getIngrList() {
        return spec()
                .when()
                .get("/api/ingredients")
                .then().log().all()
                .extract().body().path("data._id");
    }

    public ValidatableResponse getUserOrders(String token){
        return spec()
                .auth().oauth2(token.replace("Bearer ", ""))
                .when()
                .get(ORDER_API)
                .then().log().all();

    }

    public int extractOrderNum(ValidatableResponse response){
        return response.extract()
                .path("order.number");
    }

}
