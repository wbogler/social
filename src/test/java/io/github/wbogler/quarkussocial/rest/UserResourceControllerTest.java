package io.github.wbogler.quarkussocial.rest;

import io.github.wbogler.quarkussocial.rest.dto.user.CreateUserRequest;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserResourceControllerTest {

    @Test
    @DisplayName("Should create an user is successfully")
    @Order(1)
    public void createUserTest(){
        var user = new CreateUserRequest("Willian", 30);
        var response =
                given()
                        .contentType(ContentType.JSON)
                        .body(user)
                .when()
                        .post("/users")
                .then()
                        .extract().response();

        assertEquals(200, response.statusCode());
        assertNotNull(response.jsonPath().getString("id"));
    }

    @Test
    @DisplayName("Should return error when json is not valid")
    @Order(2)
    public void createUserValidationErrorTest(){
        var user = new CreateUserRequest(null, null);
        var response =
                given()
                        .contentType(ContentType.JSON)
                        .body(user)
                .when()
                        .post("/users")
                .then()
                        .extract().response();

        assertEquals(422, response.statusCode());
        assertEquals("Validation Error", response.jsonPath().getString("message"));
    }

    @Test
    @DisplayName("should list all users")
    @Order(3)
    public void listAllUsersTeste(){
        var user = given().contentType(ContentType.JSON)
                    .when().get("/users")
                    .then().statusCode(200).body("size()", Matchers.is(1));


    }
}