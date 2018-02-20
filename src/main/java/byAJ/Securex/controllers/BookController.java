package byAJ.Securex.controllers;

import byAJ.Securex.Service.UserService;
import byAJ.Securex.models.Book;
import byAJ.Securex.models.User;
import byAJ.Securex.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class BookController {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    UserService userService;


    @RequestMapping("/books")
    public String books(Model model, Authentication auth){
      String name = auth.getAuthorities().toString();
      model.addAttribute("user", name);
        return "index";
    }

    @RequestMapping("/list")
    public String listBooks(Model model){
        model.addAttribute("books", bookRepository.findAll());
        return "listbooks";
    }
    @GetMapping("/add")
    public String addBook(Model model){
        model.addAttribute("book", new Book());
        return "bookform";
    }
    @PostMapping("/add")
    public String processBook(@ModelAttribute Book book, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "bookform";
        }
        bookRepository.save(book);
        return "redirect:/list";
    }
    @RequestMapping("/edit/{id}")
    public String editBook(@PathVariable("id")int bookid, Model model){
        Book book = new Book();
        book = bookRepository.findOne(bookid);
        model.addAttribute("book", book);
        return "bookform";
    }
    @RequestMapping("/delete/{id}")
    public String deleteBook(@PathVariable("id")int bookid){
        bookRepository.delete(bookid);
        return "listbooks";
    }

    @GetMapping("/register")
    public String showRegistrationPage(Model model){
        model.addAttribute("user",new User());
        return "registration";
    }

    @PostMapping("/register")
    public String processregistration(@Valid @ModelAttribute("user") User user, BindingResult result, Model model ){

        model.addAttribute("user",user);
        if(result.hasErrors()){
            return "registration";
        }else{
            userService.saveUser(user);
            model.addAttribute("message","User Account Successfully Created");
        }
        return "index";
    }
}
