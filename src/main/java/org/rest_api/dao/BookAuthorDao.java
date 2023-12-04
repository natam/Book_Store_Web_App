package org.rest_api.dao;

import org.rest_api.db_connection.DBConnection;
import org.rest_api.entity.Author;
import org.rest_api.entity.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class BookAuthorDao {
    public Map<Author, List<Book>> getBooksByAuthor(String authorName) {
        Map<Author, List<Book>> booksList = new HashMap<>();
        try (Connection connection = DBConnection.getConnection()) {
            String sql = "SELECT Books.id, title, price, quantity, authorId, name AS authorName, country " +
                    "FROM Authors " +
                    "JOIN Books ON Authors.id = Books.authorId;";
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Book book = new Book(rs.getInt("id"), rs.getString("title"), rs.getInt("authorId"), rs.getDouble("price"), rs.getInt("quantity"));
                Author author = new Author(rs.getInt("authorId"), rs.getString("name"), rs.getString("country"));
                if(booksList.containsKey(author)){
                    booksList.get(author).add(book);
                }else {
                    booksList.put(author, List.of(book));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return booksList;
    }

    public Map<Book, Author> getBookAndAuthor() {
        Map<Book, Author> booksList = new HashMap<>();
        try (Connection connection = DBConnection.getConnection()) {
            String sql = "SELECT Books.id, title, price, quantity, authorId, name AS authorName, country " +
                    "FROM Authors " +
                    "JOIN Books ON Authors.id = Books.authorId;";
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Book book = new Book(rs.getInt("id"), rs.getString("title"), rs.getInt("authorId"), rs.getDouble("price"), rs.getInt("quantity"));
                Author author = new Author(rs.getInt("authorId"), rs.getString("name"), rs.getString("country"));
                booksList.putIfAbsent(book, author);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return booksList;
    }
}
