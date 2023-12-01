package org.rest_api.rest_resources;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.rest_api.dao.BookDao;
import org.rest_api.entity.Book;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/books")
public class BookResource {
    private final BookDao bookDao = new BookDao();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Book> getOrders(@QueryParam("offset") int offset,
                                @QueryParam("limit") int limit) {
        int booksCount = bookDao.getBooksCount();
        Gson gson = new Gson();
        JsonObject response = new JsonObject();
        response.add("items", new JsonObject());
        return bookDao.getBooks(offset, limit);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Book getBookById(@PathParam("id") int id) {
        return bookDao.getBook(id);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void addBook(Book book) {
        bookDao.addBook(book);
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void updateBook(@PathParam("id") int id, Book book) {
        book.setId(id);
        bookDao.updateBook(book);
    }

    @DELETE
    @Path("/{id}")
    public void deleteBook(@PathParam("id") int id) {
        bookDao.deleteBook(id);
    }
}
