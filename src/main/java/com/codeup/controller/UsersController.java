package com.codeup.controller;

import com.codeup.models.*;
import com.codeup.repositories.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by roxana on 6/29/17.
 */
@Controller
public class UsersController {
    private final UsersRepository usersRepository;
    private RolesRepository rolesRepository;
    private PasswordEncoder passwordEncoder;
    private ItemsRepository itemsRepository;
    private UserItemsRepository userItemsRepository;
    private PreferenceRepository preferenceRepository;
    private CategoriesRepository categoriesRepository;
    private UserCategoryRepository userCategoryRepository;

    @Value("${users-img-path}")
    private String usersImgPath;

    @Autowired
    public UsersController(UsersRepository usersRepository, RolesRepository rolesRepository, PasswordEncoder passwordEncoder,
                           ItemsRepository itemsRepository, UserItemsRepository userItemsRepository, PreferenceRepository preferenceRepository,
                           CategoriesRepository categoriesRepository, UserCategoryRepository userCategoryRepository) {
        this.usersRepository = usersRepository;
        this.rolesRepository = rolesRepository;
        this.passwordEncoder = passwordEncoder;
        this.itemsRepository = itemsRepository;
        this.userItemsRepository = userItemsRepository;
        this.preferenceRepository = preferenceRepository;
        this.categoriesRepository = categoriesRepository;
        this.userCategoryRepository = userCategoryRepository;
    }

    @PostMapping("/users/register")
    public String saveUser(@ModelAttribute User user, @RequestParam(name = "preference") String preference, @RequestParam(name = "file") MultipartFile uploadedFile, Model model) {
        String filename = transferUploadedFile(uploadedFile, usersImgPath, model);

        user.setImgUrl(filename);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        usersRepository.save(user);

        // create a default role for each user ROLE_USER
        UserRole userRole = new UserRole(user.getId(), "ROLE_USER");
        rolesRepository.save(userRole);

        //update table users_categories
        Preference prefByName = preferenceRepository.findByName(preference);
        List<Category> categories = categoriesRepository.findByUser_Id(1);
        for (Category category : categories) {
            String[] split = category.getPreferences().split(" ");
            for(int i=0; i<split.length; i++) {
                if (Integer.parseInt(split[i]) == prefByName.getId()) {
                    userCategoryRepository.save(new UserCategory(category, user, prefByName));
                }
            }
        }

        //update table users_items
        List<Item> items = itemsRepository.findByUser_Id(1);

        for (Item item : items) {
            String[] split = item.getPreferences().split(" ");
            for(int i=0; i<split.length; i++) {
                if (Integer.parseInt(split[i]) == prefByName.getId()) {
                    userItemsRepository.save(new UserItem(user, item));
                }
            }
        }

        return "redirect:/login";
    }


    public static String transferUploadedFile(MultipartFile uploadedFile, String imgPath, Model model) {
        String filename = uploadedFile.getOriginalFilename();
        String filepath = Paths.get(imgPath, filename).toString();
        File destinationFile = new File(filepath);

        try {
            uploadedFile.transferTo(destinationFile);
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("message", "Oops! Something went wrong! " + e);
        }
        return filename;
    }
}
