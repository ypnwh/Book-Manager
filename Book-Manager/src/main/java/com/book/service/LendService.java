package com.book.service;

import com.book.dao.BookDao;
import com.book.dao.LendDao;
import com.book.domain.Lend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;

@Service
public class LendService {
    private LendDao lendDao;

    @Autowired
    public void setLendDao(LendDao lendDao) {
        this.lendDao = lendDao;
    }

    public boolean bookReturn(long bookId){
        return lendDao.bookReturnOne(bookId)>0 && lendDao.bookReturnTwo(bookId)>0;
    }


    public boolean bookLend(long bookId,int readerId){
        return lendDao.bookLendOne(bookId,readerId)>0 && lendDao.bookLendTwo(bookId)>0;
    }

    public ArrayList<Lend> lendList(){
        return lendDao.lendList();
    }
    public ArrayList<Lend> myLendList(int readerId){
        return lendDao.myLendList(readerId);
    }

    public boolean myLendListOverDue(int readerId){
        ArrayList<Lend> list=lendDao.myLendList(readerId);
        Date currentdate=new Date();
        for(Lend lend:list) {
            if (lend.getBackDate()== null &&currentdate.getTime()>lend.getDueDate().getTime())
                return true;
        }
        return false;
    }

    public int bookOverDue(long bookId){
        System.out.println(bookId);
        Date date=lendDao.bookReturnInf(bookId);
        //ArrayList<Lend> list=lendDao.lendList();
        //System.out.println(lend.getBookId()+'\t'+lend.getReaderId());
        Date currentdate=new Date();
        int diff=(int)((currentdate.getTime()-date.getTime())/(1000*3600*24));
        return diff;
        //return 1;
    }


    public boolean myLendListNotReturn(int readerId){
        ArrayList<Lend> list=lendDao.myLendList(readerId);
        for(Lend lend:list) {
            if (lend.getBackDate()== null)
                return true;
        }
        return false;
    }
}
