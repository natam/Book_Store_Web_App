package org.rest_api.dao;

import org.rest_api.db_connection.DBConnection;
import org.rest_api.entity.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDao {
    public List<Book> getBooks(int offset, int limit) throws SQLException {
        List<Book> bookstList = new ArrayList<>();
        Connection connection = DBConnection.getConnection();
        String sql = "SELECT * FROM Books LIMIT ? OFFSET ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, limit);
        stmt.setInt(2, offset);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            Book book = new Book(rs.getInt("id"), rs.getString("title"), rs.getInt("authorId"), rs.getDouble("price"), rs.getInt("quantity"));
            bookstList.add(book);
        }
        connection.close();
        return bookstList;
    }

    public int getBooksCount() throws SQLException {
        int rows = 0;
        Connection connection = DBConnection.getConnection();
        String sql = "SELECT COUNT(*) FROM Books";
        Statement st = connection.createStatement();
        ResultSet resultSet = st.executeQuery(sql);
        resultSet.next();
        rows = resultSet.getInt(1);
        connection.close();
        return rows;
    }

    public Book getBook(int id) throws SQLException {
        Connection connection = DBConnection.getConnection();
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM Books WHERE id = ?");
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return new Book(rs.getInt("id"), rs.getString("title"), rs.getInt("authorId"), rs.getDouble("price"), rs.getInt("quantity"));
        }
        connection.close();
        return null;
    }

    public void addBook(Book book) throws SQLException {
        Connection connection = DBConnection.getConnection();
        connection.setAutoCommit(false);
        String addBookSql = "INSERT INTO Books (title, authorId, price, quantity) VALUES (?, ?, ?, ?)";

        PreparedStatement insertBook = connection.prepareStatement(addBookSql);
        insertBook.setString(1, book.getTitle());
        insertBook.setInt(2, book.getAuthorId());
        insertBook.setDouble(3, book.getPrice());
        insertBook.setInt(4, book.getQuantity());
        insertBook.executeUpdate();
        connection.close();
    }

    public void updateBook(Book book) throws SQLException {
        Connection connection = DBConnection.getConnection();
        PreparedStatement ps = connection.prepareStatement("UPDATE Books SET title = ?, authorId = ?, price = ?, quantity = ? WHERE id = ?");
        ps.setString(1, book.getTitle());
        ps.setInt(2, book.getAuthorId());
        ps.setDouble(3, book.getPrice());
        ps.setInt(4, book.getQuantity());
        ps.setInt(5, book.getId());
        ps.executeUpdate();
    }

    public void deleteBook(int id) throws SQLException {
        Connection connection = DBConnection.getConnection();
        PreparedStatement ps = connection.prepareStatement("DELETE FROM Books WHERE id = ?");
        ps.setInt(1, id);
        ps.executeUpdate();
        connection.close();
    }
}
