package com.book.web;

import com.book.domain.Book;
import com.book.domain.ReaderCard;
import com.book.service.BookService;
import com.book.service.LendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LendController {

    private LendService lendService;
    @Autowired
    public void setLendService(LendService lendService) {
        this.lendService = lendService;
    }
    private BookService bookService;
    @Autowired
    public void setBookService(BookService bookService) {
        this.bookService = bookService;
    }

    @RequestMapping("/lendbook.html")
    public ModelAndView bookLend(HttpServletRequest request){
        long bookId=Integer.parseInt(request.getParameter("bookId"));
        Book book=bookService.getBook(bookId);
       ModelAndView modelAndView=new ModelAndView("admin_book_lend");
       modelAndView.addObject("book",book);
       return modelAndView;
    }



    @RequestMapping("/lendbookdo.html")
    public String bookLendDo(HttpServletRequest request,RedirectAttributes redirectAttributes,int readerId){
        long bookId=Integer.parseInt(request.getParameter("id"));
        boolean lendoverdue=lendService.myLendListOverDue(readerId);
        if(lendoverdue){
            redirectAttributes.addFlashAttribute("error", "图书借阅失败！您尚有超期未归还的书！");
            return "redirect:/allbooks.html";
        }
        boolean lendsucc=lendService.bookLend(bookId,readerId);
        if (lendsucc){
            redirectAttributes.addFlashAttribute("succ", "图书借阅成功！");
            return "redirect:/allbooks.html";
        }else {
            redirectAttributes.addFlashAttribute("error", "图书借阅失败！");
            return "redirect:/allbooks.html";
        }


    }

    @RequestMapping("/returnbook.html")
    public String bookReturn(HttpServletRequest request,RedirectAttributes redirectAttributes){
        long bookId=Integer.parseInt(request.getParameter("bookId"));
        int diffdays=lendService.bookOverDue(bookId);
        boolean retSucc=lendService.bookReturn(bookId);
        if (diffdays>0)
            System.out.println("yse");
        if (retSucc){
            String res="图书归还成功！";
            if(diffdays>0)
                res+="超期"+diffdays+"天！";
            redirectAttributes.addFlashAttribute("succ", res);
            return "redirect:/allbooks.html";
        }
        else {
            redirectAttributes.addFlashAttribute("error", "图书归还失败！");
            return "redirect:/allbooks.html";
        }
    }



    @RequestMapping("/lendlist.html")
    public ModelAndView lendList(){

        ModelAndView modelAndView=new ModelAndView("admin_lend_list");
        modelAndView.addObject("list",lendService.lendList());
        return modelAndView;
    }
    @RequestMapping("/mylend.html")
    public ModelAndView myLend(HttpServletRequest request){
        ReaderCard readerCard=(ReaderCard) request.getSession().getAttribute("readercard");
        ModelAndView modelAndView=new ModelAndView("reader_lend_list");
        modelAndView.addObject("list",lendService.myLendList(readerCard.getReaderId()));
        return modelAndView;
    }




}
