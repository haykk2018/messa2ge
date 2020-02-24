package com.card.website.controller;

import com.card.website.domain.Page;
import com.card.website.repository.PageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.Date;
import java.util.Map;


@Controller // This means that this class is a Controller
@RequestMapping(path = "/adminpanel") // This means URL's start with /demo (after Application path)
public class AdminController {

    @Autowired
    private PageRepository pageRepository;

    //      admin panel first page
    @GetMapping(path = "/main")
    public String mainAdmin(Map<String, Object> model) {
        Iterable<Page> pages = pageRepository.findAll();

        model.put("pages", pages);

        return "admin/main";
    }

    //      add new page
    @PostMapping(path = "/add-page")
    public String pageAdd(@Valid Page page, BindingResult bindingResult) {

        //validating unique filds
        if (pageRepository.existsByLangAndLangId(page.getLang(), page.getLangId())) {
            bindingResult.rejectValue("langId", "messageCode", "The field must be unique. Page with your value already exist ");
        }
        if (bindingResult.hasErrors()) {
            return "admin/editAddPage";
        }
        // time automatic set if isn"t sent, they aren't important operations
        if (page.getBeginDate() == null) {
            page.setBeginDate(new Date());
        } else {
            page.setEditDate(new Date());
        }
        //if menu sequence isn't changed we dun't push the sequence
        if (page.getId() != null && page.getMenuSequence() != pageRepository.findById(page.getId()).get().getMenuSequence()) {
            pageRepository.pushSequenceOneStep(page.getMenuSequence(), page.getLang());
        }
        pageRepository.save(page);
        return "redirect:/adminpanel/main";
    }

    //      delete page
    @GetMapping(path = "/delete-page")
    public String pageDelete(@RequestParam Integer id) {

        pageRepository.deleteById(id);
        return "redirect:/adminpanel/main";
    }

    @GetMapping(path = "/new-page")
    public String pageEditAdd(@RequestParam(name = "id", required = false) Integer id, Map<String, Object> model) {

        Page page;

        if (id != null) {
            // if id nul its doing edit
            page = pageRepository.findById(id).get();

        } else {
            // if isn"t  nul its doing new
            page = new Page();
        }
        model.put("page", page);
        // pages need for menu sequence
        Iterable<Page> pages = pageRepository.findByLang(page.getLang(), Sort.by("menuSequence").ascending());
        model.put("pages", pages);

        return "admin/editAddPage";
    }

    // ajax request from admin main page
    @GetMapping(path = "/get-pages")
    public String getPagesByLang(@RequestParam String lang, Map<String, Object> model) {

        Iterable<Page> pages;

        if (lang.equals("all")) {
            pages = pageRepository.findAll();
        } else {
            pages = pageRepository.findByLang(Page.Lang.valueOf(lang), Sort.by("menuSequence").ascending());
        }

        model.put("pages", pages);
        return "admin/ajaxPagesByLang :: table-by-pages";
    }

    // ajax request from admin edit or add page
    @GetMapping(path = "/get-sequence")
    public String getSequencesByLang(@RequestParam String lang, @RequestParam(name = "msequence", required = false) Integer msequence, Map<String, Object> model) {

        Iterable<Page> pages = pageRepository.findByLangAndHiddenIsFalse(Page.Lang.valueOf(lang), Sort.by("menuSequence").ascending());

        model.put("currentMenuSequence", msequence);
        model.put("pages", pages);
        return "admin/ajaxSequensesByLang :: menu-sequense";
    }
}