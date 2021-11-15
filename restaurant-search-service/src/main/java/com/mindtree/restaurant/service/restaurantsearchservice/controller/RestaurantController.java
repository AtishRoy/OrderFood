package com.mindtree.restaurant.service.restaurantsearchservice.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mindtree.restaurant.service.restaurantsearchservice.builder.SearchResponseBuilder;
import com.mindtree.restaurant.service.restaurantsearchservice.exception.InvalidRequestException;
import com.mindtree.restaurant.service.restaurantsearchservice.exception.NoRecordsFoundException;
import com.mindtree.restaurant.service.restaurantsearchservice.model.Restaurant;
import com.mindtree.restaurant.service.restaurantsearchservice.service.impl.RestaurantServiceImpl;
import com.mindtree.restaurant.service.restaurantsearchservice.validator.Validator;
import com.mindtree.restaurant.service.restaurantsearchservice.vo.ItemVO;
import com.mindtree.restaurant.service.restaurantsearchservice.vo.ReviewVO;
import com.mindtree.restaurant.service.restaurantsearchservice.vo.SearchRestaurantResponseVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("restaurant")
@Api(value = "Restaurant Search", produces = MediaType.APPLICATION_JSON_VALUE, tags = { "Restaurant Search" },description="API to search restaurants")
public class RestaurantController {

    @Autowired
    RestaurantServiceImpl restaurantService;

    @Autowired
    SearchResponseBuilder responseBuilder;

    @Autowired
    Validator validator;
    @Value("${response.failure.code}")
    private String FAILURE_CODE;
    @Value("${response.failure.message}")
    private String FAILURE_MESSAGE;
    @Value("${response.success.code}")
    private String SUCCESS_CODE;
    @Value("${response.search.success.message}")
    private String SUCCESS_MESSAGE;

    @ApiOperation(value = "Api to add restaurants", response = Restaurant.class)
    @ApiResponses(value = { @ApiResponse(code = 204, message = "Restaurant added successfully"),
            @ApiResponse(code = 401, message = "You are not authorized to add restaurants"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden") })
    @PostMapping
    public List<Restaurant> save(@RequestBody List<Restaurant> restaurant) {
        return restaurantService.save(restaurant);
    }

    @ApiOperation(value = "Api to search restaurants based on Search criteria", response = SearchRestaurantResponseVO.class)
    @ApiResponses(value = { @ApiResponse(code = 204, message = "Restaurant search successfull"),
            @ApiResponse(code = 401, message = "You are not authorized to search restaurants"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden") })
    @GetMapping("/searchbyfilter")
    public ResponseEntity<SearchRestaurantResponseVO> getRestaurantsBySearchCriteria(
        @RequestParam(value = "name", required = false) String name,
        @RequestParam(value = "overallRating", required = false) String overallRating,
        @RequestParam(value = "Average cost for two ", required = false) String budget,
        @RequestParam(value = "cuisine", required = false) String cuisine,
        @RequestParam(value = "city", required = false) String city,
        @RequestParam(value = "dishName", required = false) String dishName) throws NoRecordsFoundException {
        SearchRestaurantResponseVO responseVO = null;
        if (validator.isValidRequest(budget, overallRating)) {
            List<Restaurant> restaurantList = restaurantService.getRestaurantsBySearchCriteria(name, overallRating,
                budget, cuisine, city, dishName);
            if (!restaurantList.isEmpty()) {
                responseVO = responseBuilder.buildResponse(restaurantList, HttpStatus.OK, SUCCESS_CODE,
                    SUCCESS_MESSAGE);
            }
            else {
                throw new NoRecordsFoundException(String.valueOf(HttpStatus.NO_CONTENT), FAILURE_CODE, FAILURE_MESSAGE);
            }
        }
        return new ResponseEntity<SearchRestaurantResponseVO>(responseVO, HttpStatus.OK);
    }
    

    @ApiOperation(value = "Api to search restaurants", response = SearchRestaurantResponseVO.class)
    @ApiResponses(value = { @ApiResponse(code = 204, message = "Restaurant search successfull"),
            @ApiResponse(code = 401, message = "You are not authorized to search restaurants"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden") })
    @GetMapping("/search")
    public ResponseEntity<SearchRestaurantResponseVO> search(
        @RequestParam(value = "query", required = true) String query)
        throws NoRecordsFoundException, InvalidRequestException {
        List<Restaurant> restaurantList = restaurantService.getRestaurantsBySearchParam(query);
        SearchRestaurantResponseVO responseVO = null;
        if (!restaurantList.isEmpty()) {
            responseVO = responseBuilder.buildResponse(restaurantList, HttpStatus.OK, SUCCESS_CODE, SUCCESS_MESSAGE);
        }
        else {
            throw new NoRecordsFoundException(String.valueOf(HttpStatus.NO_CONTENT), FAILURE_CODE, FAILURE_MESSAGE);
        }
        return new ResponseEntity<SearchRestaurantResponseVO>(responseVO, HttpStatus.OK);
    }

    @ApiOperation(value = "Get Menu of a restaurant", response = ItemVO.class)
    @ApiResponses(value = { @ApiResponse(code = 204, message = "Retreived Menu of the restaurant"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden") })
    @GetMapping("/getrestaurantmenu")
    public ResponseEntity<List<ItemVO>> getRestaurantMenu(
        @RequestParam(value = "restaurantId", required = true) String restaurantId) throws NoRecordsFoundException {
        Optional<Restaurant> restaurant = restaurantService.getRestaurantMenu(restaurantId);
        List<ItemVO> responseVO = null;
        if (restaurant.isPresent() && restaurant.get().getRestaurantId() != null
            && restaurant.get().getRestaurantId().equalsIgnoreCase(restaurantId)) {
            responseVO = responseBuilder.buildItems(restaurant.get().getCategoryList());
        }
        else {
            throw new NoRecordsFoundException(String.valueOf(HttpStatus.NO_CONTENT), FAILURE_CODE, FAILURE_MESSAGE);
        }
        return new ResponseEntity<List<ItemVO>>(responseVO, HttpStatus.OK);
    }

    @ApiOperation(value = "Api to update average rating for a restaurant", response = String.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Rating updated successfully"),
            @ApiResponse(code = 401, message = "You are not authorized to update the rating"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "Restaurant not available to update average rating") })
    @PutMapping
    public ResponseEntity<Object> updateAverageRating(
        @ApiParam(value = "Review Details", required = true) @Validated @RequestBody List<ReviewVO> reviews)
        throws NoRecordsFoundException {
        restaurantService.updateAverageRating(reviews);
        return new ResponseEntity<Object>("Review updated successfully", HttpStatus.OK);
    }
}
