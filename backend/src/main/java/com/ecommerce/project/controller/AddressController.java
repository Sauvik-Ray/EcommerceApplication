package com.ecommerce.project.controller;

import com.ecommerce.project.Repository.AddressRepository;
import com.ecommerce.project.model.Address;
import com.ecommerce.project.model.User;
import com.ecommerce.project.payload.AddressDTO;
import com.ecommerce.project.service.AddressService;
import com.ecommerce.project.util.AuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Addresses", description = "Address management APIs for user shipping and billing addresses")
@RestController
@RequestMapping("/api")
public class AddressController {

    @Autowired
    AuthUtil authUtil;

    @Autowired
    AddressService addressService;
    @Autowired
    private AddressRepository addressRepository;

    @Operation(summary = "Create new address", description = "Add a new address for the current user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Address created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid address data")
    })
    @PostMapping("/addresses")
    public ResponseEntity<AddressDTO> createAddress(@Valid @RequestBody AddressDTO addressDTO){
        User user = authUtil.loggedInUser();
        AddressDTO savedAddressDTO = addressService.createAddress(addressDTO, user);
        return new ResponseEntity<>(savedAddressDTO, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all addresses", description = "Retrieve all addresses (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Addresses retrieved successfully")
    })
    @GetMapping("/addresses")
    public ResponseEntity<List<AddressDTO>> getAddress(){
        List<AddressDTO> addressList = addressService.getAddresses();
        return new ResponseEntity<>(addressList, HttpStatus.OK);
    }

    @Operation(summary = "Get address by ID", description = "Retrieve specific address details by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Address retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Address not found")
    })
    @GetMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDTO>getAddressById(@Parameter(description = "Address ID") @PathVariable Long addressId){
       AddressDTO addressDTO = addressService.getAddressById(addressId);
       return new ResponseEntity<>(addressDTO, HttpStatus.OK);
    }

    @Operation(summary = "Get user addresses", description = "Retrieve all addresses for the current user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User addresses retrieved successfully")
    })
    @GetMapping("/user/addresses")
    public ResponseEntity<List<AddressDTO>>getUserAddresses(){
       User user = authUtil.loggedInUser();
       List<AddressDTO>addressList = addressService.getUserAddresses(user);
       return new ResponseEntity<>(addressList, HttpStatus.OK);
    }

    @Operation(summary = "Update address", description = "Update existing address information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Address updated successfully"),
            @ApiResponse(responseCode = "404", description = "Address not found")
    })
    @PutMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDTO>updateAddressById(@Parameter(description = "Address ID to update") @PathVariable Long addressId,
                                                       @RequestBody AddressDTO addressDTO){
        addressDTO = addressService.updateAddressById(addressId, addressDTO);
        return new ResponseEntity<>(addressDTO, HttpStatus.OK);
    }

    @Operation(summary = "Delete address", description = "Remove an address from the user's address book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Address deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Address not found")
    })
    @DeleteMapping("/addresses/{addressId}")
    public ResponseEntity<String>deleteAddress(@Parameter(description = "Address ID to delete") @PathVariable Long addressId){
        String status = addressService.deleteAddress(addressId);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }
}
