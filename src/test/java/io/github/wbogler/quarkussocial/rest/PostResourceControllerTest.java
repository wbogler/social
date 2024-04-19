package io.github.wbogler.quarkussocial.rest;

import io.github.wbogler.quarkussocial.domain.domain.FollowerModel;
import io.github.wbogler.quarkussocial.domain.domain.UserModel;
import io.github.wbogler.quarkussocial.repository.user.FollowerRepository;
import io.github.wbogler.quarkussocial.repository.user.UserRepository;
import io.github.wbogler.quarkussocial.rest.dto.post.PostRequest;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


@QuarkusTest
@TestHTTPEndpoint(PostResourceController.class) //próprio resource fornecerá a url
class PostResourceControllerTest {

    @Inject
    UserRepository userRepository;
    @Inject
    FollowerRepository followerRepository;

    Long userId;
    Long userNotFollowerId;
    Long userFollowerId;

    @BeforeEach
    @Transactional
    public void insertData(){
        //usario padrão
        var user = new UserModel(null,"Willian", 30);
        userRepository.persist(user);
        userId = user.getId();

        //Follower is not a follower
        var userNotFollower = new UserModel(null, "Bogler", 40);
        userRepository.persist(userNotFollower);
        userNotFollowerId = userNotFollower.getId();

        //Follower is a follower
        var userFollower = new UserModel(null, "Silva", 50);
        userRepository.persist(userFollower);
        userFollowerId = userFollower.getId();

        FollowerModel follower = new FollowerModel(null,user, userFollower);
        followerRepository.persist(follower);
    }

    @Test
    @DisplayName("Should create an post for a user")
    public void createPostTest(){
        var postRequest = new PostRequest(
                "Some text"
        );

        var userId = 1;
        RestAssured.given().contentType(ContentType.JSON).body(postRequest).pathParams("userId", userId)
                .when().post()
                .then().statusCode(201);
    }

    @Test
    @DisplayName("Should return 404 when trying to make a post for inexistent user")
    public void postForAnInexistentUserTest(){
        var postRequest = new PostRequest("some text");
        var idUser = 999;
        RestAssured.given()
                .contentType(ContentType.JSON).body(postRequest).pathParam("userId",idUser)
                .when().post()
                .then().statusCode(404);
    }

    @Test
    @DisplayName("Should return 400 when user does not exist")
    public void listPostUserNotFoundTest(){
        var inexistentUserId = 99;

        RestAssured.given().pathParam("userId", inexistentUserId)
                .when().get()
                .then().statusCode(400);
    }

    @Test
    @DisplayName("Should return 400 when followerId header is not present")
    public void listPostFollowheaderNotSendTest(){

        var followerId = 100;

        RestAssured.given().pathParam("userId",userId)
                .when().get()
                .then().statusCode(400).body(Matchers.is("You forgot the header"));

    }

    @Test
    @DisplayName("Should return 400 when follower does not exist")
    public void listPostFollowerheaderNotFoundTest(){

        RestAssured.given().pathParam("userId", userId)
                .when().get()
                .then().statusCode(400).body(Matchers.is("You forgot the header"));

    }

    @Test
    @DisplayName("Should return 400 the follower doesn't exist")
    public void listPostUserIsNotAFollowerTest(){

        var inexistentfollowerId = 80;

        RestAssured.given().pathParam("userId", userId).header("followerId",inexistentfollowerId)
                .when().get()
                .then().statusCode(400).body(Matchers.is("Inexist follower id"));

    }

    @Test
    @DisplayName("Should return 400 the follower doesn't is a follower")
    public void listPostUserfollowerIsNotAFollowerTest(){

        RestAssured.given().pathParam("userId", userId).header("followerId",userNotFollowerId)
                .when().get()
                .then().statusCode(403).body(Matchers.is("You cant see these posts"));

    }

//    @Test
//    @DisplayName("Should return posts")
//    public void listPostTest(){
//        RestAssured.given().pathParam("userId", userId).header("followerId",userFollowerId)
//                .when().get()
//                .then().statusCode(200).body("size()",Matchers.is(0));
//    }

}