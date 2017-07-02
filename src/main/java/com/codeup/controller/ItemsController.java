package com.codeup.controller;

import com.codeup.models.Category;
import com.codeup.models.Item;
import com.codeup.repositories.CustomItemsRepository;
import com.codeup.repositories.ItemsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by roxana on 6/28/17.
 */
@Controller
public class ItemsController {
    private final ItemsRepository itemsRepository;
    private final CustomItemsRepository customItemsRepository;

    @Value("${items-img-path}")
    private String itemsImgPath;

    @Autowired
    public ItemsController(ItemsRepository itemsRepository, CustomItemsRepository customItemsRepository) {
        this.itemsRepository = itemsRepository;
        this.customItemsRepository = customItemsRepository;
    }

    @GetMapping("/items")
    public String viewItems(@RequestParam("category_id") long category_id, Model model) {
        List<Item> items = itemsRepository.findByCategory_Id(category_id);
        model.addAttribute("items", items);
        return "items/index";
    }

    @GetMapping("/items/create")
    public String createCategory(Model model) {
        model.addAttribute("items", new Item());
        return "items/create";
    }

    @PostMapping("/items/create")
    public String saveCategory(@ModelAttribute Item item, @RequestParam(name = "file") MultipartFile uploadedFile, Model model) {
        String filename = UsersController.transferUploadedFile(uploadedFile, itemsImgPath, model);
        item.setImgUrl(filename);
        itemsRepository.save(item);
        return "redirect:/home";
    }
}
