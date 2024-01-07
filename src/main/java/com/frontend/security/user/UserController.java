package com.frontend.security.user;

import com.frontend.security.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collection;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PatchMapping
    public ResponseEntity<?> changePassword(
          @RequestBody ChangePasswordRequest request,
          Principal connectedUser
    ) {
        userService.changePassword(request, connectedUser);
        return ResponseEntity.ok().build();
    }

//    @PostMapping("/user")
//    public ResponseEntity<Object> createUser(@PathVariable("id") Integer userId,
//                                             @RequestBody User userRequest) {
//        Optional<Object> user = userService.getUser(userId).map(temp -> {
//            userRequest.setId(temp.getId());
//            return userService.createUser(userRequest);
//        });
//
//        return new ResponseEntity<>(user, HttpStatus.CREATED);
//    }

    @GetMapping("/user/all")
    public ResponseEntity<Object> getProducts() {

        Collection<User> result;

        result = userService.getUsers();

        if(!result.isEmpty()){
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else{
            return new ResponseEntity<>("No Users Available", HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/user/{id}")
    public ResponseEntity<Object> getUsers(@PathVariable("id") Integer id) {

        User result = userService.getUser(id).orElseThrow(() -> new ResourceNotFoundException(id));
        return new ResponseEntity<>(result, HttpStatus.OK);

    }


    @PutMapping("/user/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable("id") Integer id ,@Valid @RequestBody User newUser) {

        User result = userService.updateUser(id, newUser).orElseThrow(() -> new ResourceNotFoundException(id ));

        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(userService.deleteUser(id), HttpStatus.OK);

    }
}
