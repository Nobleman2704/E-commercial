package com.example.ecommercial.controller;

import com.example.ecommercial.domain.dto.request.CategoryCreateRequest;
import com.example.ecommercial.domain.dto.request.CategoryUpdateRequest;
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
            @Valid @ModelAttribute("category")CategoryCreateRequest categoryCreateRequest,
            BindingResult bindingResult
            ){
        ModelAndView modelAndView = new ModelAndView("viewName");
        if (bindingResult.hasErrors())
            modelAndView.addObject("message", extractAllErrors(bindingResult));
        else{
            BaseResponse response = categoryService.save(categoryCreateRequest);
            modelAndView.addObject("message", response.getMessage());
        }
        return modelAndView;
    }

    @PutMapping("/update")
    public ModelAndView updateCategory(
            @Valid @ModelAttribute("category")CategoryUpdateRequest categoryUpdateRequest,
            BindingResult bindingResult
            ){
        ModelAndView modelAndView = new ModelAndView("viewName");
        if (bindingResult.hasErrors())
            modelAndView.addObject("message", extractAllErrors(bindingResult));
        else {
            BaseResponse response = categoryService.update(categoryUpdateRequest);
            modelAndView.addObject("message", response.getMessage());
        }
        return modelAndView;
    }

    @GetMapping("get_all")
    public ModelAndView getAllCategories(){
        return new ModelAndView("viewName",
                "categories", categoryService.getALl().getData());
    }
}
