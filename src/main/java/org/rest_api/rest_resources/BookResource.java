package org.rest_api.rest_resources;

import com.google.gson.*;
import org.rest_api.dao.BookAuthorDao;
import org.rest_api.dao.BookDao;
import org.rest_api.entity.Author;
import org.rest_api.entity.Book;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Path("/books")
public class BookResource {
    private final BookDao bookDao = new BookDao();
    private final BookAuthorDao bookAuthorDao = new BookAuthorDao();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBooks(@QueryParam("offset") int offset,
                             @QueryParam("limit") int limit) {
        try {
            int booksCount = bookDao.getBooksCount();
            Gson gson = new Gson();
            JsonObject response = new JsonObject();
            List<Book> books = bookDao.getBooks(offset, limit);
            response.add("items", gson.toJsonTree(books));
            response.addProperty("total", booksCount);
            response.addProperty("offset", offset);
            response.addProperty("limit", limit);
            return Response.ok(response.toString()).build();
        } catch (Exception e) {
            return Response.serverError().build();
        }
    }

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllBooks(@QueryParam("offset") int offset,
                                @QueryParam("limit") int limit,
                                @QueryParam("bookTitle") String bookTitle,
                                @QueryParam("priceFrom") double priceFrom,
                                @QueryParam("priceTo") double priceTo,
                                @QueryParam("authorName") String authorName,
                                @QueryParam("country") String authorCountry
    ) {
        try {
            int booksCount = bookAuthorDao.getRowsCount(bookTitle, priceFrom, priceTo, authorName, authorCountry);
            Gson gson = new Gson();
            JsonObject response = new JsonObject();
            Map<Book, Author> books = bookAuthorDao.getBookAndAuthor(bookTitle, priceFrom, priceTo, authorName, authorCountry, limit, offset);
            JsonArray booksArray = new JsonArray();
            for (Map.Entry entry : books.entrySet()) {
                JsonObject book = JsonParser.parseString(gson.toJson(entry.getKey())).getAsJsonObject();
                book.add("author", JsonParser.parseString(gson.toJson(entry.getValue())).getAsJsonObject());
                booksArray.add(book);
            }
            response.add("items", booksArray);
            response.addProperty("total", booksCount);
            response.addProperty("offset", offset);
            response.addProperty("limit", limit);
            return Response.ok(response.toString()).build();
        } catch (Exception e) {
            return Response.status(500, e.getMessage()).build();
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBookById(@PathParam("id") int id) {
        try {
             Book book = bookDao.getBook(id);
            return Response.ok(book).build();
        } catch (Exception e) {
            return Response.status(500, e.getMessage()).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addBook(Book book) {
        try {
            bookDao.addBook(book);
            return Response.status(201).build();
        } catch (Exception e) {
            return Response.status(500, e.getMessage()).build();
        }
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateBook(@PathParam("id") int id, Book book) {
        book.setId(id);
        try {
            bookDao.updateBook(book);
            return Response.ok().build();
        } catch (Exception e) {
            return Response.status(500, e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public void deleteBook(@PathParam("id") int id) {
        try {
            bookDao.deleteBook(id);
        } catch (Exception e) {
            Response.serverError();
        }
    }
}
