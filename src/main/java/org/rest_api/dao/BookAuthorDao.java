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
    public Map<Author, List<Book>> getBooksByAuthor(String bookTitle, double priceFrom, double priceTo, String authorName, String authorCountry, int limit, int offset) {
        Map<Author, List<Book>> booksList = new HashMap<>();
        String sql = "SELECT Books.id, title, price, quantity, authorId, name AS authorName, country " +
                "FROM Authors " +
                "JOIN Books ON Authors.id = Books.authorId";
        try (Connection connection = DBConnection.getConnection()) {
            PreparedStatement stmt = getQueryStatement(connection, sql, bookTitle, priceFrom, priceTo, authorName, authorCountry, limit, offset);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Book book = new Book(rs.getInt("id"), rs.getString("title"), rs.getInt("authorId"), rs.getDouble("price"), rs.getInt("quantity"));
                Author author = new Author(rs.getInt("authorId"), rs.getString("authorName"), rs.getString("country"));
                if (booksList.containsKey(author)) {
                    booksList.get(author).add(book);
                } else {
                    booksList.put(author, List.of(book));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return booksList;
    }

    public Map<Book, Author> getBookAndAuthor(String bookTitle, double priceFrom, double priceTo, String authorName, String country, int limit, int offset) throws SQLException {
        Map<Book, Author> booksList = new HashMap<>();
        Connection connection = DBConnection.getConnection();
            String sql = "SELECT Books.id, title, price, quantity, authorId, name AS authorName, country " +
                    "FROM Authors " +
                    "JOIN Books ON Authors.id = Books.authorId";
            PreparedStatement stmt = getQueryStatement(connection, sql, bookTitle, priceFrom, priceTo, authorName, country, limit, offset);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Book book = new Book(rs.getInt("id"), rs.getString("title"), rs.getInt("authorId"), rs.getDouble("price"), rs.getInt("quantity"));
                Author author = new Author(rs.getInt("authorId"), rs.getString("authorName"), rs.getString("country"));
                booksList.putIfAbsent(book, author);
            }
        connection.close();
        return booksList;
    }

    public int getRowsCount(String bookTitle, double priceFrom, double priceTo, String authorName, String authorCountry) throws SQLException {
        String sql = "SELECT COUNT(*) " +
                "FROM Authors " +
                "JOIN Books ON Authors.id = Books.authorId";
        int rows = 0;
        Connection connection = DBConnection.getConnection();
        PreparedStatement stmt = getQueryStatement(connection, sql, bookTitle, priceFrom, priceTo, authorName, authorCountry, 10000000, 0);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        rows = rs.getInt(1);
        connection.close();
        return rows;
    }

    public PreparedStatement getQueryStatement(Connection connection, String sql, String bookTitle, double priceFrom, double priceTo, String authorName, String authorCountry, int limit, int offset) throws SQLException {

        String sqlEnding = " LIMIT ? OFFSET ?;";
        //condition statement creation
        List<String> conditions = new ArrayList<>();
        if (!bookTitle.isEmpty()) {
            conditions.add("title = ?");
        }
        if (priceFrom >= 0 && (priceTo > 0.0)) {
            conditions.add("price BETWEEN ? AND ?");
        }
        if (!authorName.isEmpty()) {
            conditions.add("authorName = ?");
        }
        if (!authorCountry.isEmpty()) {
            conditions.add("country = ?");
        }
        System.out.println(conditions.size());
        if (!conditions.isEmpty()) {
            String condition = String.join(" AND ", conditions);
            sql = sql + " WHERE " + condition + sqlEnding;
        } else {
            sql = sql + sqlEnding;
        }
        System.out.println(sql);
        PreparedStatement stm = connection.prepareStatement(sql);
        //condition statement variables set
        int args = 1;
        if (!bookTitle.isEmpty()) {
            stm.setString(args, bookTitle);
            args++;
        }
        if (priceFrom >= 0 && (priceTo > 0)) {
            stm.setDouble(args, priceFrom);
            stm.setDouble(args + 1, priceTo);
            args = args + 2;
        }
        if (!authorName.isEmpty()) {
            stm.setString(args, authorName);
            args++;
        }
        if (!authorCountry.isEmpty()) {
            stm.setString(args, authorCountry);
            args++;
        }
        stm.setInt(args, limit);
        stm.setInt(args + 1, offset);
        return stm;
    }
}
