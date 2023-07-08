package com.blaquesystems.backend.controllers;

import com.blaquesystems.backend.models.Business;
import com.blaquesystems.backend.payload.response.MessageResponse;
import com.blaquesystems.backend.repository.BusinessRepository;
import com.blaquesystems.backend.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Objects;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/business")
public class BusinessController {

    @Autowired
    BusinessRepository businessRepository;

    @Autowired
    UserRepository userRepository;

    @PostMapping("/create/{id}")
    public ResponseEntity<?> create(@RequestBody Business business, @PathVariable("id") Long id){
        if (businessRepository.existsByName(business.getName())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: name is already taken!"));
        } else if (businessRepository.existsByEmail(business.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: email is already taken!"));
        } else if (businessRepository.existsByContact(business.getContact())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: contact  already in use!"));
        }
        if (!Objects.nonNull(userRepository.findById(id).get())){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: User does not exist!"));
        }
        Date date = new Date();
        business.setCreatedAt(date);
        business.setUpdatedAt(date);
        business.setUser(userRepository.findById(id).get());
       return ResponseEntity.ok(businessRepository.save(business));
    };

    @Operation(
            description = "Business update",
            summary = "This is an endpoint update business",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200"),
                    @ApiResponse(description = "Unauthorized / Invalid Token", responseCode = "401")
            }

    )
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@RequestBody Business business, @PathVariable("id") Long id){

        if (!Objects.nonNull(businessRepository.findById(id).get())){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Business does not exist!"));
        }

        if (businessRepository.existsByName(business.getName())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: name is already taken!"));
        } else if (businessRepository.existsByEmail(business.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: email is already taken!"));
        } else if (businessRepository.existsByContact(business.getContact())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: contact  already in use!"));
        }
        Date date = new Date();
        business.setUpdatedAt(date);
        return ResponseEntity.ok(businessRepository.save(business));
    };

//    To Do: Add media upload
@Operation(
        description = "Business add avatar",
        summary = "This is an endpoint for add avatar to business",
        responses = {
                @ApiResponse(description = "Success", responseCode = "200"),
                @ApiResponse(description = "Unauthorized / Invalid Token", responseCode = "401")
        }

)
    @PutMapping("/update/avatar/{id}")
    public ResponseEntity<?> updateAvatar(@RequestBody Business business, @PathVariable("id") Long id){

        if (!Objects.nonNull(businessRepository.findById(id).get())){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Business does not exist!"));
        }

        if (businessRepository.existsByName(business.getName())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: name is already taken!"));
        } else if (businessRepository.existsByEmail(business.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: email is already taken!"));
        } else if (businessRepository.existsByContact(business.getContact())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: contact  already in use!"));
        }
        Date date = new Date();
        business.setUpdatedAt(date);
        return ResponseEntity.ok(businessRepository.save(business));
    };

    @Operation(
            description = "Business list one",
            summary = "This is an endpoint for listing one business",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200"),
                    @ApiResponse(description = "Unauthorized / Invalid Token", responseCode = "401")
            }

    )
    @GetMapping("/list/{id}")
    public ResponseEntity<?> findOne(@PathVariable("id") Long id){

        if (!Objects.nonNull(businessRepository.findById(id).get())){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Business does not exist!"));
        }

        return ResponseEntity.ok(businessRepository.findById(id).get());
    };

    @Operation(
            description = "Business list all",
            summary = "This is an endpoint for listing all businesses",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200"),
                    @ApiResponse(description = "Unauthorized / Invalid Token", responseCode = "401")
            }

    )
    @GetMapping("/list")
    public ResponseEntity<?> findALl(){

        if (!Objects.nonNull(businessRepository.findAll())){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("No Businesses found!"));
        }

        return ResponseEntity.ok(businessRepository.findAll());
    };

    @Operation(
            description = "Business delete one",
            summary = "This is an endpoint for deleting one business",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200"),
                    @ApiResponse(description = "Unauthorized / Invalid Token", responseCode = "401")
            }

    )
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteOne(@PathVariable("id") Long id){

        if (!Objects.nonNull(businessRepository.findById(id).get())){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Business does not exist!"));
        }
        businessRepository.deleteById(id);

        return ResponseEntity.ok().body(new MessageResponse("Business deleted successfully!"));
    };

    @Operation(
            description = "Business delete all",
            summary = "This is an endpoint for deleting all businesses",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200"),
                    @ApiResponse(description = "Unauthorized / Invalid Token", responseCode = "401")
            }

    )
    @DeleteMapping("/list/delete")
    public ResponseEntity<?> deleteAll(){
        businessRepository.deleteAll();

        return ResponseEntity.ok().body(new MessageResponse("All Businesses deleted successfully!"));
    };
}
