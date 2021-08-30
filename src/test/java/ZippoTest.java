import POJO.Locatıon;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class ZippoTest {

    @Test
    public void test(){
        given()
                .when()
                .then()
        ;
    }
    @Test
    public void statusCodeTest(){
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()

                //.log().all()--> all respons
                .log().body()
                .statusCode(200)
        ;

    }
    @Test
    public void contentTypeTest(){
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .contentType(ContentType.JSON)
        ;

    }
    @Test
    public void logTest(){
        given()
                .log().all()
                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()

        ;
    }
    @Test
    public void checkStateInResponseBody (){
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .body("country",equalTo("United States"))
                .statusCode(200)
        ;
    }
    @Test
    public void bodyJsonPathTest (){
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()
                .body("places[0].state",equalTo("california"))
                .statusCode(200)
        ;
    }
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

    private ResponseSpecification responseSpecification;
    private RequestSpecification requestSpecification;

    @BeforeClass
    public void setup(){

    baseURI="http://api.zippopotam.us";

    requestSpecification=new RequestSpecBuilder()
            .log(LogDetail.URI)
            .setAccept(ContentType.JSON)
            .build();

    responseSpecification=new ResponseSpecBuilder()
            .expectStatusCode(200)
            .expectContentType(ContentType.JSON)
            .log(LogDetail.BODY)
            .build();


    }
    @Test
    public void bodyArrayHasSizeTest_requestSpecification() {
        given()
                .spec(requestSpecification)
                .when()
                .get("/us/90210")

                .then()
                .body("places", hasSize(1))
                .spec(responseSpecification);
    }

    @Test
    public void bodyArrayHasSizeTest_responseSpecification() {
        given()
                .log().uri()
                .when()
                .get("/us/90210")

                .then()
                .body("places", hasSize(1))
                .spec(responseSpecification)
        ;
    }


    @Test
    public void bodyArrayHasSizeTest_baseUriTest() {
        given()
                .log().uri()
                .when()
                .get("/us/90210")

                .then()
                .body("places", hasSize(1))
                .log().body()
                .statusCode(200)
        ;
    }

@Test
    public void extractingJsonPath(){
        String place_name= given()
                //.spec(requestSpecification)
                .when()
                .get("/us/90210")
                .then()
                .spec(responseSpecification)
                //.body("places[0].'place name'",equalTo("Beverly Hills"))
                .extract().path("places[0].'place name'")
              //" " -->String, 1-->integer

        ;
    System.out.println("place name = " + place_name);

}

    @Test
    public void extractingJsonPathInt() {
        Integer limit=
        given()

                .param("page",1)
                //.log().uri()
                .when()
                .get("https://gorest.co.in/public/v1/users")

                .then()
                //.log().body()
                .extract().path("meta.pagination.limit")
        ;
        System.out.println("limit ="+limit);
    }

    @Test
    public void extractingJsonPathIntList() {
      List<Integer> ids=
                given()

                        .param("page",1)
                        //.log().uri()
                        .when()
                        .get("https://gorest.co.in/public/v1/users")

                        .then()
                        //.log().body()
                        .extract().path("data.id")
                ;
        System.out.println("ids ="+ids);
    }
    @Test
    public void extractingJsonPathStringList() {
        List<String> placeNames=
                given()

                        .when()
                        .get("/tr/01000")

                        .then()
                        //.spec(responseSpecification)
                        .extract().path("places.'place name'")
                ;
        System.out.println("placeNames ="+placeNames);
        Assert.assertTrue(placeNames.contains("Gölbaşi Köyü"));
    }
    @Test
    public void exractingJsonPOJO(){
        
        Locatıon locatıon=
        given()

                .when()
                .get("/us/90210")

                .then()
                .extract().as(Locatıon.class)

       ;
        System.out.println("locatıon = " + locatıon);
        System.out.println("locatıon.getCountry() = " + locatıon.getCountry());
        System.out.println("locatıon.getPlaces() = " + locatıon.getPlaces());
        System.out.println("locatıon.getPlaces().get(0).getPlacename() = " + locatıon.getPlaces().get(0).getPlacename());
    }
}


