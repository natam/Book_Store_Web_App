package org.rest_api.rest_resources;

import com.google.gson.*;
import org.rest_api.dao.BookDao;
import org.rest_api.entity.Book;
import org.rest_api.error_handling.ErrorResponse;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/books")
public class BookResource {
    private final BookDao bookDao = new BookDao();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBooks(@QueryParam("offset") int offset,
                                @QueryParam("limit") int limit) {
        int booksCount = bookDao.getBooksCount();
        Gson gson = new Gson();
        JsonObject response = new JsonObject();
        List<Book> books = bookDao.getBooks(offset, limit);
        response.add("items", gson.toJsonTree(books));
        response.addProperty("total", booksCount);
        response.addProperty("offset", offset);
        response.addProperty("limit", limit);
        return Response.ok(response.toString()).build();
    }

    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchOrders(@QueryParam("offset") int offset,
                                 @QueryParam("limit") int limit) {
        int booksCount = bookDao.getBooksCount();
        Gson gson = new Gson();
        JsonObject response = new JsonObject();
        List<Book> books = bookDao.getBooks(offset, limit);
        response.add("items", gson.toJsonTree(books));
        response.addProperty("total", booksCount);
        response.addProperty("offset", offset);
        response.addProperty("limit", limit);
        return Response.ok(response.toString()).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBookById(@PathParam("id") int id){
        Book book = bookDao.getBook(id);
        if(book != null){
           return Response.ok(book).build();
        }else {
            return Response.status(404)
                    .entity(new ErrorResponse(Response.Status.NOT_FOUND.name(),
                            "Book with id " + id + " not found"))
                    .build();
        }
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
