package com.www.ipcdemo;

import  com.www.ipcdemo.Book;

interface IBookManager{

    List<Book> getBookList();
    void addBook(in Book book);
}

