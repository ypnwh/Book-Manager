package com.book.dao;

import com.book.domain.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

@Repository
public class BookDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final static String ADD_BOOK_SQL="INSERT INTO book_info VALUES(NULL ,?,?,?,?,?,?,?,?,?,?,?)";
    private final static String DELETE_BOOK_SQL="delete from book_info where book_id = ?  ";
    private final static String EDIT_BOOK_SQL="update book_info set name= ? ,author= ? ,publish= ? ,ISBN= ? ,introduction= ? ,language= ? ,price= ? ,pubdate= ? ,class_id= ? ,pressmark= ? ,state= ?  where book_id= ? ;";
    private final static String QUERY_ALL_BOOKS_SQL="SELECT * FROM book_info ";
    private final static String QUERY_BOOK_SQL="SELECT * FROM book_info WHERE book_id like  ?  or name like ?   ";
    //查询匹配图书的个数
    private final static String MATCH_BOOK_SQL="SELECT count(*) FROM book_info WHERE book_id like ?  or name like ?  ";
    //根据书号查询图书
    private final static String GET_BOOK_SQL="SELECT * FROM book_info where book_id = ? ";
    //根据书号判断该书是否已被借出
    private final static String GET_BOOKSTATE_SQL="SELECT state FROM book_info WHERE book_id = ? ";

    public int matchBook(String searchWord){
        //String sql="SELECT * FROM book_info WHERE ";
        String swcx="%"+searchWord+"%";
        //sql+=swcx
        //System.out.println("swcx:"+swcx);
        int res=jdbcTemplate.queryForObject(MATCH_BOOK_SQL,new Object[]{swcx,swcx},Integer.class);
        //System.out.println("res:"+res);
        return res;
    }
    public int matchBook(String[] searchWordArray){
        String sql="SELECT count(*) FROM book_info WHERE ";
        String swcx="name like '%"+searchWordArray[0]+"%' ";
        sql+=swcx;
        //System.out.println("swcx:"+swcx);
        for(int i=1;i<searchWordArray.length;i++){
            swcx="and name like '%"+searchWordArray[i]+"%' ";
            sql+=swcx;
        }
        System.out.println("sql="+sql);
        int res=jdbcTemplate.queryForObject(sql,Integer.class);
        System.out.println("res:"+res);
        return res;
    }

    public ArrayList<Book> queryBook(String sw){
        String swcx="%"+sw+"%";
        final ArrayList<Book> books=new ArrayList<Book>();
        jdbcTemplate.query(QUERY_BOOK_SQL, new Object[]{swcx,swcx}, new RowCallbackHandler() {
            public void processRow(ResultSet resultSet) throws SQLException {
                resultSet.beforeFirst();
                while (resultSet.next()){
                    Book book =new Book();
                    book.setAuthor(resultSet.getString("author"));
                    book.setBookId(resultSet.getLong("book_id"));
                    book.setClassId(resultSet.getInt("class_id"));
                    book.setIntroduction(resultSet.getString("introduction"));
                    book.setIsbn(resultSet.getString("isbn"));
                    book.setLanguage(resultSet.getString("language"));
                    book.setName(resultSet.getString("name"));
                    book.setPressmark(resultSet.getInt("pressmark"));
                    book.setPubdate(resultSet.getDate("pubdate"));
                    book.setPrice(resultSet.getBigDecimal("price"));
                    book.setState(resultSet.getInt("state"));
                    book.setPublish(resultSet.getString("publish"));
                    books.add(book);
                }

            }
        });
        /*System.out.println("size:"+books.size());
        for(Book book:books)
            System.out.println(book.getName());*/
        return books;
    }

    public ArrayList<Book> queryBook(String[] searchWordArray){
        String sql="SELECT * FROM book_info WHERE ";
        String swcx="name like '%"+searchWordArray[0]+"%' ";
        sql+=swcx;
        //System.out.println("swcx:"+swcx);
        for(int i=1;i<searchWordArray.length;i++){
            swcx="and name like '%"+searchWordArray[i]+"%' ";
            sql+=swcx;
        }
        final ArrayList<Book> books=new ArrayList<Book>();
        jdbcTemplate.query(sql, new RowCallbackHandler() {
            public void processRow(ResultSet resultSet) throws SQLException {
                resultSet.beforeFirst();
                while (resultSet.next()){
                    Book book =new Book();
                    book.setAuthor(resultSet.getString("author"));
                    book.setBookId(resultSet.getLong("book_id"));
                    book.setClassId(resultSet.getInt("class_id"));
                    book.setIntroduction(resultSet.getString("introduction"));
                    book.setIsbn(resultSet.getString("isbn"));
                    book.setLanguage(resultSet.getString("language"));
                    book.setName(resultSet.getString("name"));
                    book.setPressmark(resultSet.getInt("pressmark"));
                    book.setPubdate(resultSet.getDate("pubdate"));
                    book.setPrice(resultSet.getBigDecimal("price"));
                    book.setState(resultSet.getInt("state"));
                    book.setPublish(resultSet.getString("publish"));
                    books.add(book);
                }

            }
        });
        /*System.out.println("size:"+books.size());
        for(Book book:books)
            System.out.println(book.getName());*/
        return books;
    }

    public ArrayList<Book> getAllBooks(){
        final ArrayList<Book> books=new ArrayList<Book>();

        jdbcTemplate.query(QUERY_ALL_BOOKS_SQL, new RowCallbackHandler() {
            public void processRow(ResultSet resultSet) throws SQLException {
                resultSet.beforeFirst();
                    while (resultSet.next()){
                        Book book =new Book();
                        book.setPrice(resultSet.getBigDecimal("price"));
                        book.setState(resultSet.getInt("state"));
                        book.setPublish(resultSet.getString("publish"));
                        book.setPubdate(resultSet.getDate("pubdate"));
                        book.setName(resultSet.getString("name"));
                        book.setIsbn(resultSet.getString("isbn"));
                        book.setClassId(resultSet.getInt("class_id"));
                        book.setBookId(resultSet.getLong("book_id"));
                        book.setAuthor(resultSet.getString("author"));
                        book.setIntroduction(resultSet.getString("introduction"));
                        book.setPressmark(resultSet.getInt("pressmark"));
                        book.setLanguage(resultSet.getString("language"));
                        books.add(book);
                    }
            }
        });
        return books;

    }

    public int deleteBook(long bookId){

        return jdbcTemplate.update(DELETE_BOOK_SQL,bookId);
    }

    public int addBook(Book book){
        String name=book.getName();
        String author=book.getAuthor();
        String publish=book.getPublish();
        String isbn=book.getIsbn();
        String introduction=book.getIntroduction();
        String language=book.getLanguage();
        BigDecimal price=book.getPrice();
        Date pubdate=book.getPubdate();
        int classId=book.getClassId();
        int pressmark=book.getPressmark();
        int state=book.getState();

        return jdbcTemplate.update(ADD_BOOK_SQL,new Object[]{name,author,publish,isbn,introduction,language,price,pubdate,classId,pressmark,state});
    }

    public Book getBook(Long bookId){
        final Book book =new Book();
        jdbcTemplate.query(GET_BOOK_SQL, new Object[]{bookId}, new RowCallbackHandler() {
            public void processRow(ResultSet resultSet) throws SQLException {
                    book.setAuthor(resultSet.getString("author"));
                    book.setBookId(resultSet.getLong("book_id"));
                    book.setClassId(resultSet.getInt("class_id"));
                    book.setIntroduction(resultSet.getString("introduction"));
                    book.setIsbn(resultSet.getString("isbn"));
                    book.setLanguage(resultSet.getString("language"));
                    book.setName(resultSet.getString("name"));
                    book.setPressmark(resultSet.getInt("pressmark"));
                    book.setPubdate(resultSet.getDate("pubdate"));
                    book.setPrice(resultSet.getBigDecimal("price"));
                    book.setState(resultSet.getInt("state"));
                    book.setPublish(resultSet.getString("publish"));
            }

        });
        return book;
    }
    public int editBook(Book book){
        Long bookId=book.getBookId();
        String name=book.getName();
        String author=book.getAuthor();
        String publish=book.getPublish();
        String isbn=book.getIsbn();
        String introduction=book.getIntroduction();
        String language=book.getLanguage();
        BigDecimal price=book.getPrice();
        Date pubdate=book.getPubdate();
        int classId=book.getClassId();
        int pressmark=book.getPressmark();
        int state=book.getState();

        return jdbcTemplate.update(EDIT_BOOK_SQL,new Object[]{name,author,publish,isbn,introduction,language,price,pubdate,classId,pressmark,state,bookId});
    }


}
