package com.web;

import com.model.Advertisment;
import com.model.User;
import com.service.AdvertismentService;

import com.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;


@Controller
public class AdvertismentController {

    @Autowired
    private AdvertismentService advertismentService;

    @Autowired
    private UserService userService;


    @RequestMapping(value = "/advertisments/list", method = RequestMethod.GET)
    public ModelAndView listAdvertisments(ModelAndView model) throws IOException {

        List<Advertisment> listAdv = advertismentService.listAll();
        model.addObject("listAdvertisment", listAdv);
        model.setViewName("advertisment_list");

        return model;
    }


    @RequestMapping(value = "/advertisments/search/bythecity", method = RequestMethod.GET)
    public ModelAndView insertTheCityToSearch(@ModelAttribute("city") String city) throws IOException {

        return new ModelAndView("search", "city", new String());
    }

    @RequestMapping(value = "/advertisments/search/bythecity", method = RequestMethod.POST)
    public ModelAndView listAdvertismentsByCity(ModelAndView modelAndView, @RequestParam(value = "city") String city) throws IOException {

        List<Advertisment> listAdvByTheCity = advertismentService.listByTheCity(city);
        modelAndView.addObject("list_by_city", listAdvByTheCity);
        modelAndView.setViewName("advertisment_list_by_city");

        return modelAndView;
    }

    @RequestMapping(value = "/advertisments/search/byapartmenttype/{apartmentType}", method = RequestMethod.GET)
    public ModelAndView listAdvertismentsByApartmentType(ModelAndView modelAndView, @PathVariable(value = "apartmentType") String apartmentType) throws IOException {

        List<Advertisment> listAdvsByApartmentType = advertismentService.findAdvertismentsByApartment_type(apartmentType);
        modelAndView.addObject("list_by_apartmenttype", listAdvsByApartmentType);
        modelAndView.setViewName("advertisment_list_by_apartmeny_type");

        return modelAndView;
    }

    @RequestMapping(value = "/advertisment/new", method = RequestMethod.GET)
    public ModelAndView openAddNewAdv() {

        return new ModelAndView("new_adv", "adv", new Advertisment());
    }

    @RequestMapping(value = "/advertisment/new", method = RequestMethod.POST)
    public String addNewAdv(@ModelAttribute("adv") Advertisment advertisment) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        User user = userService.findByUsername(username);
        advertisment.setUser(user);

        advertismentService.save(advertisment);

        return "redirect:/welcome";
    }


    @RequestMapping(value = "/advertisment/edit", method = RequestMethod.GET)
    public ModelAndView getEditAdvertismentForm(@RequestParam long id) {

        return new ModelAndView("/edit_advertisment_form", "current_advertisment", advertismentService.get(id));
    }

    @RequestMapping(value = "/advertisment/edit", method = RequestMethod.PUT)
    public String postEditedAdvertismentForm(@ModelAttribute("current_advertisment") Advertisment advertisment) {

        advertismentService.update(advertisment);

        return "redirect:/welcome";
    }


    @RequestMapping(value = "/advertisment/delete", method = RequestMethod.DELETE)
    public String deleteAdvertismentForm(@RequestParam long id) {
        advertismentService.delete(id);
        return "redirect:/welcome";
    }

}