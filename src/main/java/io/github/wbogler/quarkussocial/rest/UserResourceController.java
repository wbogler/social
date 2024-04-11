package io.github.wbogler.quarkussocial.rest;

import io.github.wbogler.quarkussocial.domain.domain.UserModel;
import io.github.wbogler.quarkussocial.repository.user.UserRepository;
import io.github.wbogler.quarkussocial.rest.dto.CreateUserRequest;
import io.github.wbogler.quarkussocial.rest.dto.ResponseError;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Set;

@Path("/users")
public class UserResourceController {

    @Inject
    Validator validator;

    @Inject
    UserRepository userRepository;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response createUser(CreateUserRequest createUserRequest){
        Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(createUserRequest);
        if(!violations.isEmpty()){
            return ResponseError.createFromValidation(violations).withStatusCode(ResponseError.UNPROCESSABLE_ENTITY_STATUS);
        }
        UserModel customer = new UserModel();
        customer.setName(createUserRequest.name());
        customer.setAge(createUserRequest.age());
        userRepository.persist(customer);
        return Response.ok(customer).build();
    }

    @GET
    public Response listUser(){
        return Response.ok(userRepository.findAll().stream().toList()).build();
    }


    @DELETE
    @Path("{id}")
    @Transactional
    public Response deleteUser(@PathParam("id") Long id){
        var customer = userRepository.findById(id);
        if (customer != null){
            userRepository.deleteById(id);
            return Response.noContent().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response updateUser(@PathParam("id") Long id, CreateUserRequest createUserRequest){
        UserModel customer = userRepository.findById(id);
        if (customer != null){
            customer.setName(createUserRequest.name());
            customer.setAge(createUserRequest.age());
            return Response.noContent().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
