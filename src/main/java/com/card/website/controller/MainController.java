package com.card.website.controller;

import com.card.website.domain.Page;
import com.card.website.repository.PageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Locale;
import java.util.Map;

@Controller // This means that this class is a Controller
public class MainController {

    @Autowired
    private PageRepository pageRepository;

    //      admin page by id
    @GetMapping
    public String pageAdmin(@RequestParam(name = "id", defaultValue = "1") Integer id, @RequestParam(name = "lang", defaultValue = "eng") String lang, Map<String, Object> model, Locale locale) {

        Iterable<Page> pages = pageRepository.findByLangAndHiddenIsFalse(Page.Lang.valueOf(locale.getLanguage()), Sort.by("menuSequence").ascending());
        Page curPage = pageRepository.findByLangAndLangId(Page.Lang.valueOf(locale.getLanguage()),id);

        model.put("curPage", curPage);
        model.put("pages", pages);


        return "page";
    }
}