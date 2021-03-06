package com.codeup.models;

import javax.persistence.*;
import java.util.List;

/**
 * Created by roxana on 7/6/17.
 */
@Entity
@Table(name = "recipes")
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String imgUrl;

    @Column(nullable = false, columnDefinition = "Text")
    private String directions;

    @Column
    private String time;

    @ManyToOne
    @JoinColumn(name = "preference_id")
    private Preference preference;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "recipe")
    private List<RecipeItem> recipeItems;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Recipe() {
    }

    public Recipe(String name, String imgUrl, String directions, String time) {
        this.name = name;
        this.imgUrl = imgUrl;
        this.directions = directions;
        this.time = time;
    }

    public Recipe(String name, String imgUrl, String directions, User user) {
        this.name = name;
        this.imgUrl = imgUrl;
        this.directions = directions;
        this.user = user;
    }

    public Recipe(String name, String imgUrl, String directions, String time, Preference preference, User user) {
        this.name = name;
        this.imgUrl = imgUrl;
        this.directions = directions;
        this.time = time;
        this.preference = preference;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getDirections() {
        return directions;
    }

    public void setDirections(String directions) {
        this.directions = directions;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<RecipeItem> getRecipeItems() {
        return recipeItems;
    }

    public void setRecipeItems(List<RecipeItem> recipeItems) {
        this.recipeItems = recipeItems;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Preference getPreference() {
        return preference;
    }

    public void setPreference(Preference preference) {
        this.preference = preference;
    }
}
