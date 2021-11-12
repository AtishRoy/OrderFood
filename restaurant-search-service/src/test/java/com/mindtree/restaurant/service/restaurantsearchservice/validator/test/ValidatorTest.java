package com.mindtree.restaurant.service.restaurantsearchservice.validator.test;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.mindtree.restaurant.service.restaurantsearchservice.exception.InvalidRequestException;
import com.mindtree.restaurant.service.restaurantsearchservice.validator.Validator;

import lombok.val;

public class ValidatorTest {

    @MockBean
    private Validator validator;

    @org.junit.Before
    public void setup() {
        validator = new Validator();
        validator.invalid_budget_field="Enter a valid number for Average cost for two field";
        validator.invalid_rating_field="Enter a valid number for overall rating field";
        validator.invalid_rating_range="Enter the range between 0 to 5 for overall rating field";
    }

    @Test
    public void testInvalidRatingWithStringValue() {
        try {
            validator.isValidRequest(null, "sssss");
            Assert.fail();
        }
        catch (InvalidRequestException e) {
            Assert.assertEquals("Enter a valid number for overall rating field", e.getMessage());
        }
    }

    @Test
    public void testInvalidRatingWithInvalidRangeValue() {
        try {
            validator.isValidRequest(null, "8.0");
            Assert.fail();
        }
        catch (InvalidRequestException e) {
            Assert.assertEquals("Enter the range between 0 to 5 for overall rating field", e.getMessage());
        }
    }

    @Test
    public void testValidRating() {
        try {
            boolean valid = validator.isValidRequest(null, "4.0");
            Assert.assertTrue(valid);
        }
        catch (InvalidRequestException e) {
            Assert.fail();
        }
    }

    @Test
    public void testInvalidCostForTwoWithStringValue() {
        try {
            validator.isValidRequest("sssss", null);
            Assert.fail();
        }
        catch (InvalidRequestException e) {
            Assert.assertEquals("Enter a valid number for Average cost for two field", e.getMessage());
        }
    }

    @Test
    public void testValidCostForTwoValue() {
        try {
            boolean valid = validator.isValidRequest("4.0", null);
            Assert.assertTrue(valid);
        }
        catch (InvalidRequestException e) {
            Assert.fail();
        }
    }

}
