package org.rest_api.dao;

import org.rest_api.db_connection.DBConnection;
import org.rest_api.entity.Author;
import org.rest_api.entity.Book;

import javax.ws.rs.Path;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuthorDao {
    public List<Author> getAuthors(int offset, int limit) {
        List<Author> authorList = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection()) {
            String sql = "SELECT * FROM Authors LIMIT ? OFFSET ?;";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, limit);
            stmt.setInt(2, offset);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Author author = new Author(rs.getInt("id"), rs.getString("name"), rs.getString("country"));
                authorList.add(author);
                System.out.println(authorList.size());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return authorList;
    }

    public int getAuthorsCount() {
        int rows = 0;
        try (Connection connection = DBConnection.getConnection()) {
            String sql = "SELECT COUNT(*) FROM Authors;";
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

    public Author getAuthor(int id) {
        try (Connection connection = DBConnection.getConnection()) {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM Authors WHERE id = ?;");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Author(rs.getInt("id"), rs.getString("name"), rs.getString("country"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Book> getAuthorBooks(int authorId) {
        List<Book> books = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection()) {
            String sql = "SELECT Books.id, title, price, quantity, authorId, name AS authorName, country " +
                    "FROM Books " +
                    "JOIN Authors ON Authors.id = Books.authorId " +
                    "WHERE authorId = ?;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, authorId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Book book = new Book(rs.getInt("id"), rs.getString("title"), rs.getInt("authorId"), rs.getDouble("price"), rs.getInt("quantity"));
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    public void addAuthor(Author author) {
        try (Connection connection = DBConnection.getConnection()) {
            connection.setAutoCommit(false);
            String addBookSql = "INSERT INTO Authors (name, country) VALUES (?, ?);";

            PreparedStatement insertBook = connection.prepareStatement(addBookSql);
            insertBook.setString(1, author.getName());
            insertBook.setString(2, author.getCountry());
            insertBook.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateAuthor(Author author) {
        try (Connection connection = DBConnection.getConnection()) {
            PreparedStatement ps = connection.prepareStatement("UPDATE Authors SET name = ?, country = ? WHERE id = ?;");
            ps.setString(1, author.getName());
            ps.setString(2, author.getCountry());
            ps.setInt(3, author.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteAuthor(int id) {
        try (Connection connection = DBConnection.getConnection()) {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM Authors WHERE id = ?;");
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
