package Rerqres;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class ApiTests {
    private SoftAssertions softAssert;

    @Test
    public void singleResourceTest() {
        Response response = RestAssured
                .given()
                .get("https://reqres.in/api/unknown/5");

        JsonPath jsonResponse = response.jsonPath();

        softAssert = new SoftAssertions();
        softAssert.assertThat(response.statusCode()).isEqualTo(200);
        softAssert.assertThat(Integer.parseInt(jsonResponse.get("data.id").toString())).isEqualTo(5);
        softAssert.assertThat(jsonResponse.get("data.name").toString()).isEqualTo("tigerlily");
        softAssert.assertThat(jsonResponse.get("data.year").toString()).isEqualTo("2004");
        softAssert.assertAll();
        jsonResponse.prettyPrint();
    }

    @Test
    public void successfulLoginTest() {

        Map<String, String> body = new HashMap<>();
        body.put("email", "eve.holt@reqres.in");
        body.put("password", "cityslicka");
        Response response = RestAssured
                .given()
                .contentType("application/json")
                .body(body)
                .when()
                .post("https://reqres.in/api/login")
                .andReturn();

        JsonPath jsonResponse = response.jsonPath();
        softAssert = new SoftAssertions();
        softAssert.assertThat(response.statusCode()).isEqualTo(200);
        softAssert.assertThat(jsonResponse.get("token").toString()).isNotEmpty();
        softAssert.assertAll();
        jsonResponse.prettyPrint();
    }

    @Test
    public void updateTest() {
        Map<String, String> body = new HashMap<>();
        body.put("name", "morpheus");
        body.put("job", "zion resident");
        Response response = RestAssured
                .given()
                .contentType("application/json")
                .body(body)
                .when()
                .put("https://reqres.in/api/users/3")
                .andReturn();

        JsonPath jsonResponse = response.jsonPath();
        softAssert = new SoftAssertions();
        softAssert.assertThat(response.statusCode()).isEqualTo(200);
        softAssert.assertThat(jsonResponse.get("name").toString()).isEqualTo(body.get("name"));
        softAssert.assertThat(jsonResponse.get("job").toString()).isEqualTo(body.get("job"));
        softAssert.assertThat(jsonResponse.get("updatedAt").toString()).isNotEmpty();
        softAssert.assertAll();
        jsonResponse.prettyPrint();
    }

    @Test
    public void deleteTest() {
        Response response = RestAssured
                .given()
                .when()
                .delete("https://reqres.in/api/users/65");
        softAssert = new SoftAssertions();
        softAssert.assertThat(response.statusCode()).isEqualTo(204);
        softAssert.assertAll();
    }

    @Test
    public void patchUpdateTest() {
        Map<String, String> body = new HashMap<>();
        body.put("name", "morpheus");
        Response response = RestAssured
                .given()
                .contentType("application/json")
                .body(body)
                .when()
                .patch("https://reqres.in/api/users/35")
                .andReturn();

        JsonPath jsonResponse = response.jsonPath();
        softAssert = new SoftAssertions();
        softAssert.assertThat(response.statusCode()).isEqualTo(200);
        softAssert.assertThat(jsonResponse.get("name").toString()).isEqualTo(body.get("name"));
        softAssert.assertThat(jsonResponse.get("updatedAt").toString()).isNotEmpty();
        softAssert.assertAll();
        jsonResponse.prettyPrint();
    }

    @Test
    public void unsuccessfulRegisterTest() {
        Map<String, String> body = new HashMap<>();
        body.put("password", "sydneyfife");
        Response response = RestAssured
                .given()
                .contentType("application/json")
                .body(body)
                .when()
                .post("https://reqres.in/api/register")
                .andReturn();

        JsonPath jsonResponse = response.jsonPath();
        softAssert = new SoftAssertions();
        softAssert.assertThat(response.statusCode()).isEqualTo(400);
        String errorMessageNoEmailOrUsername = "Missing email or username";
        String errorMessageNoPassword = "Missing password";

        softAssert.assertThat(jsonResponse.get("error").toString()).isEqualTo(errorMessageNoEmailOrUsername);
        response.prettyPrint();

        body.clear();
        body.put("email", "sydney@fife");
        response = RestAssured
                .given()
                .contentType("application/json")
                .body(body)
                .when()
                .post("https://reqres.in/api/register")
                .andReturn();

        jsonResponse = response.jsonPath();
        softAssert.assertThat(response.statusCode()).isEqualTo(400);
        softAssert.assertThat(jsonResponse.get("error").toString()).isEqualTo(errorMessageNoPassword);
        jsonResponse.prettyPrint();
        softAssert.assertAll();
    }

    @Test
    public void successfulRegisterTest() {
        Map<String, String> body = new HashMap<>();
        body.put("email", "eve.holt@reqres.in");
        body.put("password", "pistol");
        Response response = RestAssured
                .given()
                .contentType("application/json")
                .body(body)
                .when()
                .post("https://reqres.in/api/register")
                .andReturn();

        JsonPath jsonResponse = response.jsonPath();
        softAssert = new SoftAssertions();
        softAssert.assertThat(response.statusCode()).isEqualTo(200);
        softAssert.assertThat(jsonResponse.get("id").toString()).isNotEmpty();
        softAssert.assertThat(jsonResponse.get("token").toString()).isNotEmpty();
        jsonResponse.prettyPrint();
        softAssert.assertAll();
    }

    @Test
    public void createTest() {
        Map<String, String> body = new HashMap<>();
        body.put("name", "morpheus");
        body.put("job", "leader");
        Response response = RestAssured
                .given()
                .contentType("application/json")
                .body(body)
                .when()
                .post("https://reqres.in/api/users")
                .andReturn();

        JsonPath jsonResponse = response.jsonPath();
        softAssert = new SoftAssertions();
        softAssert.assertThat(response.statusCode()).isEqualTo(201);
        softAssert.assertThat(jsonResponse.get("name").toString()).isEqualTo(body.get("name"));
        softAssert.assertThat(jsonResponse.get("job").toString()).isEqualTo(body.get("job"));
        softAssert.assertThat(jsonResponse.get("id").toString()).isNotEmpty();
        softAssert.assertThat(jsonResponse.get("createdAt").toString()).isNotEmpty();
        jsonResponse.prettyPrint();
        softAssert.assertAll();
    }
}