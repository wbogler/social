package io.github.wbogler.quarkussocial.rest;

import io.github.wbogler.quarkussocial.domain.domain.FollowerModel;
import io.github.wbogler.quarkussocial.repository.user.FollowerRepository;
import io.github.wbogler.quarkussocial.repository.user.UserRepository;
import io.github.wbogler.quarkussocial.rest.dto.follow.FollowerPerUserResponse;
import io.github.wbogler.quarkussocial.rest.dto.follow.FollowerRequest;
import io.github.wbogler.quarkussocial.rest.dto.follow.FollowerResponse;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/users/{userId}/followers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FollowerResourceController {

    @Inject
    private FollowerRepository followerRepository;

    @Inject
    private UserRepository userRepository;

    @PUT
    @Transactional
    public Response followUser(@PathParam("userId") Long id, FollowerRequest followerRequest){

        if(id.equals(followerRequest.followerId())){
            return Response.status(Response.Status.CONFLICT).entity("you can't follow yourself").build();
        }
        var customer = userRepository.findById(id);
        if(customer == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        var follower = userRepository.findById(followerRequest.followerId());

        if(!followerRepository.followers(follower, customer)){
            followerRepository.persist(new FollowerModel(
                    null, customer, follower
            ));
        }
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @GET
    public Response listFollower(@PathParam("userId") Long id){
        var customer = userRepository.findById(id);
        if(customer == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        List<FollowerResponse> result = followerRepository.findByUser(id)
                .stream()
                .map(
                        fl->new FollowerResponse(fl.getId(), fl.getFollower().getName()))
                .toList();
        return Response.ok(new FollowerPerUserResponse(
                result.size(), result
        )).build();
    }

    @DELETE
    @Transactional
    public Response unfollowUser(
            @PathParam("userId") Long id,
            @QueryParam("followerId") Long followerId){
        var user = userRepository.findById(id);
        if(user == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        followerRepository.deleteByFollowerAndUser(followerId, id);

        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
