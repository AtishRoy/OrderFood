package com.mindtree.restaurant.service.restaurantsearchservice.validator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.mindtree.restaurant.service.restaurantsearchservice.exception.InvalidRequestException;

@Component
public class Validator {
    @Value("${invalid.budget.field}")
    public String invalid_budget_field;
    
    @Value("${invalid.rating.field}")
    public String invalid_rating_field;
    
    @Value("${invalid.rating.range}")
    public String invalid_rating_range;

    public boolean isDigit(String value) {
        if (value.matches("[0-9]*\\.?[0-9]+")) {
            return true;
        }
        else {
            return false;
        }

    }


    public boolean isValidRequest(String budget, String rating) {
        boolean isValid = true;
        if (budget != null) {
            if (isDigit(budget)) {
                isValid = true;
            }
            else {
                isValid = false;
                throw new InvalidRequestException(invalid_budget_field);
            }
        }
        if (rating != null) {
            if (isDigit(rating)) {
                isValid = true;
            }
            else {
                isValid = false;
                throw new InvalidRequestException(invalid_rating_field);
            }
            float valu = Float.parseFloat(rating);
            if (valu > 0 && valu <= 5) {
                isValid = true;
            }
            else {
                isValid = false;
                throw new InvalidRequestException(invalid_rating_range);
            }
        }
        return isValid;
    }

}
