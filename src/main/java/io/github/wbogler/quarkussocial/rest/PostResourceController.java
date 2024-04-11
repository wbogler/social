package io.github.wbogler.quarkussocial.rest;

import io.github.wbogler.quarkussocial.domain.domain.PostModel;
import io.github.wbogler.quarkussocial.domain.domain.UserModel;
import io.github.wbogler.quarkussocial.repository.user.PostRepository;
import io.github.wbogler.quarkussocial.repository.user.UserRepository;
import io.github.wbogler.quarkussocial.rest.dto.CreateUserRequest;
import io.github.wbogler.quarkussocial.rest.dto.PostRequest;
import io.github.wbogler.quarkussocial.rest.dto.PostResponse;
import io.github.wbogler.quarkussocial.rest.dto.ResponseError;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Sort;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.LocalDateTime;
import java.util.Set;

@Path("/users/{userId}/posts") //este é um modelo de sub-recursos
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PostResourceController {


    @Inject
    private PostRepository postRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private Validator validator;

    @POST
    @Transactional
    public Response savePost(@PathParam("userId")Long id, PostRequest postRequest){
        var customer = userRegistry(id);
        Set<ConstraintViolation<PostRequest>> violations = validator.validate(postRequest);

        if(!violations.isEmpty()){
            return ResponseError.createFromValidation(violations).withStatusCode(ResponseError.UNPROCESSABLE_ENTITY_STATUS);
        }

        if(customer!= null){
            postRepository.persist(new PostModel(
                    null, postRequest.text(), LocalDateTime.now(), customer
            ));
            return Response.status(Response.Status.CREATED).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    public Response listPosts(@PathParam("userId")Long id){
        var customer = userRegistry(id);
        if(customer!= null){
            var posts = postRepository.find("userModel", Sort.by("postTime", Sort.Direction.Descending), customer).list();
            var postsResponse = posts.stream().map(postModel -> new PostResponse(postModel.getPost(), postModel.getPostTime())).toList();
            return Response.ok(postsResponse).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    private UserModel userRegistry(Long id){
        return userRepository.findById(id);
    }

}
