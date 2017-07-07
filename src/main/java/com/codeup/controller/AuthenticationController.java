package com.codeup.controller;

import com.codeup.models.*;
import com.codeup.repositories.PreferenceRepository;
import com.codeup.svcs.TwilioSvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * Created by roxana on 6/29/17.
 */
@Controller
public class AuthenticationController {
    private PreferenceRepository preferenceRepository;

    @Autowired
    public AuthenticationController(PreferenceRepository preferenceRepository) {
        this.preferenceRepository = preferenceRepository;
    }

    @Autowired
    TwilioSvc twilioSvc;

    @GetMapping("/login")
    public String showLoginForm() {

        twilioSvc.sendMessage("+12104219757","+18304200837","Hello, from Get It and TwilioAPI server");

        return "redirect:/";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());

        //send preferences to the view
        Iterable<Preference> preferences = preferenceRepository.findAll();
        model.addAttribute("preferences", preferences);
        return "users/register";
    }
}
