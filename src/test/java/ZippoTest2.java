import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class ZippoTest2 {
    @Test
    public void bodyJsonPathTestHasItem() {
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()
                .body("places.state", hasItem("california"))
                .statusCode(200)
        ;
        // places[0].state ->  0 index of the list
        // places.state ->    all list : California,California2   hasItem
        // List<String> list= {'California','California2'}
    }

    @Test
    public void bodyJsonPathTest2() {
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()
                .body("places[0].'place name'", equalTo("Beverly Hills"))
                .statusCode(200)
        ;
    }

    @Test
    public void bodyArrayHasSizeTest() {
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .body("places", hasSize(1))
                .log().body()
                .statusCode(200)
        ;
    }

    public void combiningTest() {
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .body("places", hasSize(1))
                .body("places.state", hasItem("california"))
                .body("places[0].'place name'", equalTo("Beverly Hills"))
                .log().body()
                .statusCode(200)
        ;
    }

    @Test
    public void PathParamTest() {
        String country = "us";
        String zipKod = "90210";

        given()
                .pathParam("country", "us")
                .pathParam("zipKod", "90210")
                .log().uri()

                .when()
                .get("http://api.zippopotam.us/{country}/{zipKod}")

                .then()
                .log().body()
                .body("places", hasSize(1))

        ;
    }

    @Test
    public void PathParamTest2() {
        String country = "us";
        for (int i = 90210; i < 90214; i++) {


            given()
                    .pathParam("country", country)
                    .pathParam("zipKod", i)
                    .log().uri()

                    .when()
                    .get("http://api.zippopotam.us/{country}/{zipKod}")

                    .then()
                    .log().body()
                    .body("places", hasSize(1))

            ;
        }
    }
    //https://gorest.co.in/public/v1/users?page=1
   @Test
    public void queryParamTest(){
        given()

                .param("page",1)
                .log().uri()
                .when()
                .get("https://gorest.co.in/public/v1/users")

                .then()
                .log().body()
                .body("meta.pagination.page",equalTo(1))
                ;

   }
    @Test
    public void queryParamTestCoklu(){
        for (int page = 1; page < 10; page++) {


            given()

                    .param("page", 1)
                    .log().uri()
                    .when()
                    .get("https://gorest.co.in/public/v1/users")

                    .then()
                    .log().body()
                    .body("meta.pagination.page", equalTo(1))
            ;
        }
    }

}
