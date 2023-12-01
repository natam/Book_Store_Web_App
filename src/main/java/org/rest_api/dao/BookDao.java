package org.rest_api.dao;

import org.rest_api.db_connection.DBConnection;
import org.rest_api.entity.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDao {
    public List<Book> getBooks(int offset, int limit) {
        List<Book> bookstList = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection()) {
            String sql = "SELECT * FROM Books LIMIT ? OFFSET ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, limit);
            stmt.setInt(2, offset);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Book book = new Book(rs.getInt("id"), rs.getString("title"), rs.getString("author"), rs.getDouble("price"), rs.getInt("quantity"));
                bookstList.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookstList;
    }

    public int getBooksCount() {
        int rows = 0;
        try (Connection connection = DBConnection.getConnection()) {
            String sql = "SELECT COUNT(*) FROM Books";
            Statement st = connection.createStatement();
            ResultSet resultSet = st.executeQuery(sql);
            resultSet.next();
            rows = resultSet.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows;
    }

//    public List<Book> getOrdersByCustomer(int offset, int limit, String customer) {
//        List<Order> orderstList = new ArrayList<>();
//        try (Connection connection = DBConnection.getConnection()) {
//            String sql = "SELECT * FROM Orders LIMIT ? OFFSET ? WHERE customer = ?";
//            PreparedStatement stmt = connection.prepareStatement(sql);
//            stmt.setInt(1, limit);
//            stmt.setInt(2, offset);
//            stmt.setString(3, customer);
//            ResultSet rs = stmt.executeQuery();
//            while (rs.next()) {
//                Order order = new Order(rs.getInt("id"), rs.getInt("productId"), rs.getInt("quantity"), rs.getString("customer"));
//                orderstList.add(order);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return orderstList;
//    }

    public Book getBook(int id) {
        try (Connection connection = DBConnection.getConnection()) {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM Books WHERE id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Book(rs.getInt("id"), rs.getString("title"), rs.getString("author"), rs.getDouble("price"), rs.getInt("quantity"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //Transaction for order creation
    public void addBook(Book book) {
        try (Connection connection = DBConnection.getConnection()) {
            connection.setAutoCommit(false);
            String addBookSql = "INSERT INTO Books (title, author, price, quantity) VALUES (?, ?, ?, ?)";

            PreparedStatement insertBook = connection.prepareStatement(addBookSql);
            insertBook.setString(1, book.getTitle());
            insertBook.setString(2, book.getAuthor());
            insertBook.setDouble(3, book.getPrice());
            insertBook.setInt(4, book.getQuantity());
            insertBook.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateBook(Book book) {
        try (Connection connection = DBConnection.getConnection()) {
            PreparedStatement ps = connection.prepareStatement("UPDATE Books SET title = ?, author = ?, price = ?, quantity = ? WHERE id = ?");
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setDouble(3, book.getPrice());
            ps.setInt(4, book.getQuantity());
            ps.setInt(5, book.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteBook(int id) {
        try (Connection connection = DBConnection.getConnection()) {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM Books WHERE id = ?");
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
