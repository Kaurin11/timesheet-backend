package com.example.demo.service.impl;

import com.example.demo.dto.request.CreateUserRequest;
import com.example.demo.dto.request.LoginRequest;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.entity.User;
import com.example.demo.repository.IUserRepository;
import com.example.demo.service.IUserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService {

    private final IUserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(IUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponse getUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        return mapUserToUserResponse(user.get());
    }

    @Override
    public UserResponse createUser(CreateUserRequest request) throws Exception {
//        List<User> allUsers = userRepository.findAll();
//        for(User u: allUsers){
//            if(u.getUserName().equals(request.getUserName())){
//                throw new Exception("Already exists.");
//            }
//        }
        if(userRepository.findOneByUserName(request.getUsername()) != null){
            throw new Exception("Already exists.");
        }
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUserName(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        User savedUser = userRepository.save(user);
        return mapUserToUserResponse(savedUser);
    }

    @Override
    public List<UserResponse> getAllUsers() {
        List<User> allUsers = userRepository.findAll();

//        List<UserResponse> responses = new ArrayList<>();
//        for(User u: allUsers){
//            UserResponse response = mapUserToUserResponse(u);
//            responses.add(response);
//        }
//        return responses;

        return allUsers.stream()
                .map(user -> mapUserToUserResponse(user))
                .collect(Collectors.toList());
    }

    @Override
    public UserResponse login(LoginRequest loginRequest) throws Exception {
        User user = userRepository.findOneByUserName(loginRequest.getUsername());
        if(user == null){
            throw new Exception("User don't exist");
        }

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new Exception("Wrong password");
        }

        return mapUserToUserResponse(user);
    }

    private UserResponse mapUserToUserResponse(User user){
        UserResponse userResponse = new UserResponse();
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setUserName(user.getUserName());
        userResponse.setId(user.getId());
        return userResponse;
    }
}

