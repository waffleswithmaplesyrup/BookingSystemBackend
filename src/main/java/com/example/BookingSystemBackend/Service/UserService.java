package com.example.BookingSystemBackend.Service;

import com.example.BookingSystemBackend.DTO.ChangePasswordRequestDTO;
import com.example.BookingSystemBackend.DTO.ResetPasswordRequestDTO;
import com.example.BookingSystemBackend.DTO.UserRequestDTO;
import com.example.BookingSystemBackend.Enum.Role;
import com.example.BookingSystemBackend.Exception.CredentialsMismatchException;
import com.example.BookingSystemBackend.Exception.EmailAlreadyTakenException;
import com.example.BookingSystemBackend.Exception.InvalidEmailException;
import com.example.BookingSystemBackend.Exception.PasswordTooShortException;
import com.example.BookingSystemBackend.Model.User;
import com.example.BookingSystemBackend.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository,
                       EmailService emailService,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(UserRequestDTO userRequestDTO) {

        // check if email has already been registered
        String email = userRequestDTO.getEmail();

        Optional<User> userInDB = userRepository.findByEmail(email);

        if(userInDB.isPresent()) throw new EmailAlreadyTakenException(userInDB.get().getEmail() + " has already been registered.");

        passwordLengthCheck(userRequestDTO.getPassword());

        User newUser = new User(userRequestDTO.getUsername(),
                userRequestDTO.getEmail(),
                passwordEncoder.encode(userRequestDTO.getPassword()),
                Role.CUSTOMER,
                userRequestDTO.getCountry());

        if (!emailService.sendVerifyEmail(newUser.getEmail())) throw new InvalidEmailException();

        return userRepository.save(newUser);
    }

    public User viewProfile(Long userId) {
        return checkIfUserExists(userId);
    }

    public void changePassword(ChangePasswordRequestDTO changePasswordRequestDTO) {

        User userInDB = checkIfUserExists(changePasswordRequestDTO.getUserId());

        passwordLengthCheck(changePasswordRequestDTO.getNewPassword());

        // check if old password entered matches password in db
        if (changePasswordRequestDTO.getOldPassword().equals(userInDB.getPassword())) throw new CredentialsMismatchException();

        userInDB.setPassword(passwordEncoder.encode(changePasswordRequestDTO.getNewPassword()));

        userRepository.save(userInDB);
    }

    public void resetPassword(ResetPasswordRequestDTO resetPasswordRequest) {

        // need approval from email before user can reset password
        if (!emailService.sendVerifyEmail(resetPasswordRequest.getEmail())) throw new InvalidEmailException();

        User userInDB = checkIfUserExists(resetPasswordRequest.getUserId());

        passwordLengthCheck(resetPasswordRequest.getNewPassword());

        userInDB.setPassword(passwordEncoder.encode(resetPasswordRequest.getNewPassword()));

        userRepository.save(userInDB);
    }

    private void passwordLengthCheck(String password) {
        if (password.length() < 8) throw new PasswordTooShortException();
    }

    private User checkIfUserExists(Long userId) {
        Optional<User> userInDB = userRepository.findById(userId);
        if (userInDB.isEmpty()) throw new NoSuchElementException();

        return userInDB.get();
    }
}
