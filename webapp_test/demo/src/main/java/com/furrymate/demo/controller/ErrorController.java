package com.furrymate.demo.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ControllerAdvice
public class ErrorController {

    private static final Logger logger = LoggerFactory.getLogger(ErrorController.class);

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleException(Exception e, Model model) {
        // Log the exception details for debugging purposes
        logger.error("Unhandled exception occurred: ", e);

        // Add exception details to the model to display them in the view
        model.addAttribute("errorMessage", "Internal Server Error: " + e.getMessage());
        model.addAttribute("stackTrace", e.getStackTrace());

        // Return the name of the HTML file that will display the error details
        return "errorPage";
    }
}
