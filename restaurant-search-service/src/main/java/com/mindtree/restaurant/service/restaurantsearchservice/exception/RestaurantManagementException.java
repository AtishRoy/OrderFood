package com.mindtree.restaurant.service.restaurantsearchservice.exception;

public class RestaurantManagementException extends Exception {
    private String status;
    private String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public RestaurantManagementException(String status, String message) {
        super();
        this.status = status;
        this.message = message;
    }

}
