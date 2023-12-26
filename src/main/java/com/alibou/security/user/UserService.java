package com.alibou.security.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {

        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        // check if the current password is correct
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }
        // check if the two new passwords are the same
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new IllegalStateException("Password are not the same");
        }

        // update the password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        // save the new password
        userRepository.save(user);
    }
//    public User createUser(User user){
//        return userRepository.save(user);
//    }


    public Collection<User> getUsers(){
        return userRepository.findAll();
    }

    public Optional<User> updateUser(Integer id, User newUser){
        Optional<User> result = userRepository.findById(id);

        try{
            User temp = result.get();
            temp.setFirstname(newUser.getFirstname());
            temp.setLastname(newUser.getLastname());
            temp.setContact(newUser.getContact());
            temp.setDob(newUser.getDob());
            temp.setEmail(newUser.getEmail());
            return Optional.of(userRepository.save(temp));
        }catch(Exception e){
            return result;
        }
    }

    public Optional<User> deleteUser(Integer id){
        Optional<User> result = userRepository.findById(id);
        if(result.isPresent()){
            userRepository.deleteById(id);
        }
        return result;
    }


    public Optional<User> getUser(Integer id){
        return userRepository.findById(id);
    }
}
