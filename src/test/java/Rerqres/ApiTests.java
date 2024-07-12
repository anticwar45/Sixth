package Rerqres;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ValidatableResponse;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class ApiTests {
    private SoftAssertions softAssert;

    @Test
    public void singleResourceTest() {
        JsonPath response = RestAssured
                .given()
                .get("https://reqres.in/api/unknown/5")
                .jsonPath();

        softAssert = new SoftAssertions();
        softAssert.assertThat(Integer.parseInt(response.get("data.id").toString())).isEqualTo(5);
        softAssert.assertThat(response.get("data.name").toString()).isEqualTo("tigerlily");
        softAssert.assertThat(response.get("data.year").toString()).isEqualTo("2004");
        softAssert.assertAll();
        response.prettyPrint();
    }

    @Test
    public void successfulLoginTest() {

        Map<String, String> body = new HashMap<>();
        body.put("email", "eve.holt@reqres.in");
        body.put("password", "cityslicka");
        JsonPath response = RestAssured
                .given()
                .contentType("application/json")
                .body(body)
                .when()
                .post("https://reqres.in/api/login")
                .jsonPath();

        softAssert = new SoftAssertions();
        softAssert.assertThat(response.get("token").toString()).isNotEmpty();
        softAssert.assertAll();
        response.prettyPrint();
    }

    @Test
    public void updateTest() {
        Map<String, String> body = new HashMap<>();
        body.put("name", "morpheus");
        body.put("job", "zion resident");
        JsonPath response = RestAssured
                .given()
                .contentType("application/json")
                .body(body)
                .when()
                .put("https://reqres.in/api/users/3")
                .jsonPath();

        softAssert = new SoftAssertions();
        softAssert.assertThat(response.get("name").toString()).isEqualTo(body.get("name"));
        softAssert.assertThat(response.get("job").toString()).isEqualTo(body.get("job"));
        softAssert.assertThat(response.get("updatedAt").toString()).isNotEmpty();
        softAssert.assertAll();
        response.prettyPrint();
    }

    @Test
    public void deleteTest() {
        ValidatableResponse response = RestAssured
                .given()
                .when()
                .delete("https://reqres.in/api/users/65")
                .then()
                .statusCode(204);
    }

    @Test
    public void patchUpdateTest() {
        Map<String, String> body = new HashMap<>();
        body.put("name", "morpheus");
        JsonPath response = RestAssured
                .given()
                .contentType("application/json")
                .body(body)
                .when()
                .patch("https://reqres.in/api/users/35")
                .jsonPath();

        softAssert = new SoftAssertions();
        softAssert.assertThat(response.get("name").toString()).isEqualTo(body.get("name"));
        softAssert.assertThat(response.get("updatedAt").toString()).isNotEmpty();
        softAssert.assertAll();
        response.prettyPrint();
    }

    @Test
    public void unsuccessfulRegisterTest() {
        Map<String, String> body = new HashMap<>();
        body.put("password", "sydneyfife");
        JsonPath response = RestAssured
                .given()
                .contentType("application/json")
                .body(body)
                .when()
                .post("https://reqres.in/api/register")
                .jsonPath();

        softAssert = new SoftAssertions();
        String errorMessageNoEmailOrUsername = "Missing email or username";
        String errorMessageNoPassword = "Missing password";

        softAssert.assertThat(response.get("error").toString()).isEqualTo(errorMessageNoEmailOrUsername);
        response.prettyPrint();

        body.clear();
        body.put("email", "sydney@fife");
        response = RestAssured
                .given()
                .contentType("application/json")
                .body(body)
                .when()
                .post("https://reqres.in/api/register")
                .jsonPath();
        softAssert.assertThat(response.get("error").toString()).isEqualTo(errorMessageNoPassword);
        response.prettyPrint();
        softAssert.assertAll();
    }

    @Test
    public void successfulRegisterTest() {
        Map<String, String> body = new HashMap<>();
        body.put("email", "eve.holt@reqres.in");
        body.put("password", "pistol");
        JsonPath response = RestAssured
                .given()
                .contentType("application/json")
                .body(body)
                .when()
                .post("https://reqres.in/api/register")
                .jsonPath();

        softAssert = new SoftAssertions();
        softAssert.assertThat(response.get("id").toString()).isNotEmpty();
        softAssert.assertThat(response.get("token").toString()).isNotEmpty();
        response.prettyPrint();
        softAssert.assertAll();
    }

    @Test
    public void createTest() {
        Map<String, String> body = new HashMap<>();
        body.put("name", "morpheus");
        body.put("job", "leader");
        JsonPath response = RestAssured
                .given()
                .contentType("application/json")
                .body(body)
                .when()
                .post("https://reqres.in/api/users")
                .jsonPath();

        softAssert = new SoftAssertions();
        softAssert.assertThat(response.get("name").toString()).isEqualTo(body.get("name"));
        softAssert.assertThat(response.get("job").toString()).isEqualTo(body.get("job"));
        softAssert.assertThat(response.get("id").toString()).isNotEmpty();
        softAssert.assertThat(response.get("createdAt").toString()).isNotEmpty();
        response.prettyPrint();
        softAssert.assertAll();
    }
}