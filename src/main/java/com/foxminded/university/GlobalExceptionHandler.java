package com.foxminded.university;

import com.foxminded.university.domain.Person;
import com.foxminded.university.domain.Teacher;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String handleCustomException(Exception exception, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String currentRequest = request.getServletPath();
        String redirectRequest;
        if (currentRequest.contains("save")) {
            redirectRequest = currentRequest.replace("save", "new");
        } else {
            redirectRequest = currentRequest.replace("update", "edit");
        }
        redirectAttributes.addFlashAttribute("error", exception.getMessage());
        return "redirect:" + redirectRequest;
    }
}
