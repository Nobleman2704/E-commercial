package com.example.ecommercial.controller;

import com.example.ecommercial.domain.dto.request.CategoryCreateAndUpdateRequest;
import com.example.ecommercial.domain.dto.request.UserCreateAndUpdateRequest;
import com.example.ecommercial.domain.dto.response.BaseResponse;
import com.example.ecommercial.service.category.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import static com.example.ecommercial.controller.UserController.extractAllErrors;

@Controller
@EnableMethodSecurity
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping("/add")
    public ModelAndView addCategory(
            @Valid @ModelAttribute("category") CategoryCreateAndUpdateRequest createAndUpdateRequest,
            BindingResult bindingResult
            ){
        ModelAndView modelAndView = new ModelAndView("dashboard");
        modelAndView.addObject("status", 1);
        if (bindingResult.hasErrors())
            modelAndView.addObject("message", extractAllErrors(bindingResult));
        else{
            BaseResponse response = categoryService.save(createAndUpdateRequest);
            modelAndView.addObject("message", response.getMessage());
        }
        modelAndView.addObject("categories", categoryService.getALl().getData());
        return modelAndView;
    }

    @PostMapping("/update")
    public ModelAndView updateCategory(
            @Valid @ModelAttribute("category") CategoryCreateAndUpdateRequest categoryUpdateRequest,
            BindingResult bindingResult
            ){
        ModelAndView modelAndView = new ModelAndView("dashboard");
        modelAndView.addObject("status", 1);
        if (bindingResult.hasErrors())
            modelAndView.addObject("message", extractAllErrors(bindingResult));
        else {
            BaseResponse response = categoryService.update(categoryUpdateRequest);
            modelAndView.addObject("message", response.getMessage());
        }
        modelAndView.addObject("categories", categoryService.getALl().getData());
        return modelAndView;
    }

    @GetMapping("get_all")
    public ModelAndView getAllCategories(){
        ModelAndView modelAndView = new ModelAndView("dashboard");
        modelAndView.addObject("categories", categoryService.getALl().getData());
        modelAndView.addObject("status", 1);
        return modelAndView;
    }
}