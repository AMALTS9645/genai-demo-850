```java
//code-start
package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
//code-end

package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.User;
import com.example.demo.service.UserService;

import javax.validation.Valid;

// Security: Use @Validated on the class level to enable validation
@RestController
@RequestMapping("/api/login")
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Endpoint to login a user based on username and age.
     * 
     * @param user User details for login.
     * @return ResponseEntity with login status.
     */
    @PostMapping
    public ResponseEntity<String> loginUser(@Valid @RequestBody User user) {
        try {
            boolean authenticated = userService.authenticateUser(user);
            if (authenticated) {
                return new ResponseEntity<>("Login successful", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Invalid username or age", HttpStatus.UNAUTHORIZED);
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Invalid user input", HttpStatus.BAD_REQUEST);
        }
        // Specific exception handling
    }
}

package com.example.demo.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

public class User {

    @NotBlank(message = "Username is mandatory")    // Security: Ensure username is provided
    private String username;

    @Min(value = 18, message = "Age should not be less than 18")
    private int age;    // Security: Ensure age is reasonable

    // Getters and setters

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}

package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.model.User;

@Service
public class UserService {

    /**
     * Authenticate user based on username and age.
     * 
     * @param user User details
     * @return boolean indicating success/failure of authentication
     * @throws IllegalArgumentException if user input is invalid
     */
    public boolean authenticateUser(User user) {
        if (user == null || user.getUsername() == null || user.getAge() < 0) {
            throw new IllegalArgumentException();
        }
        // Simulate a user verification logic
        // In a real application, this method would communicate with the database to verify user credentials.
        return "admin".equals(user.getUsername()) && user.getAge() == 30;
    }

}
//code-end
```