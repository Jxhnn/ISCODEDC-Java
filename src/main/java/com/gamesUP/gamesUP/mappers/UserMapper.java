package com.gamesUP.gamesUP.mappers;

import com.gamesUP.gamesUP.dto.request.UserRequest;
import com.gamesUP.gamesUP.dto.response.UserResponse;
import com.gamesUP.gamesUP.model.User;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

public interface UserMapper {
    UserResponse userToUserResponse(User user);
    List<UserResponse> usersToUserResponses(List<User> users);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "wishlists", ignore = true) // Don't map collections from simple request
    @Mapping(target = "purchases", ignore = true)
    @Mapping(target = "avis", ignore = true)
    @Mapping(target = "password", ignore = true) // Password handled separately in service
    User userRequestToUser(UserRequest userRequest);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "wishlists", ignore = true)
    @Mapping(target = "purchases", ignore = true)
    @Mapping(target = "avis", ignore = true)
    @Mapping(target = "password", ignore = true) // Password handled separately in service
    void updateUserFromRequest(UserRequest userRequest, @MappingTarget User user);
}