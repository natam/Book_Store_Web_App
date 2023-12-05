package org.rest_api.rest_resources;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.rest_api.dao.AuthorDao;
import org.rest_api.dao.BookAuthorDao;
import org.rest_api.entity.Author;
import org.rest_api.entity.Book;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Path("/authors")
public class AuthorResource {
    private final AuthorDao authorDao = new AuthorDao();
    private final BookAuthorDao bookAuthorDao = new BookAuthorDao();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAuthors(@QueryParam("offset") int offset,
                               @QueryParam("limit") int limit) {
        try {
            int authorsCount = authorDao.getAuthorsCount();
            Gson gson = new Gson();
            JsonObject response = new JsonObject();
            List<Author> authors = authorDao.getAuthors(offset, limit);
            System.out.println(authors.size());
            response.add("items", gson.toJsonTree(authors));
            response.addProperty("total", authorsCount);
            response.addProperty("offset", offset);
            response.addProperty("limit", limit);
            return Response.ok(response.toString()).build();
        } catch (SQLException e) {
            return Response.status(500, e.getMessage()).build();
        }
    }

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAuthors(@QueryParam("offset") int offset,
                               @QueryParam("limit") int limit,
                               @QueryParam("bookTitle") String bookTitle,
                               @QueryParam("priceFrom") double priceFrom,
                               @QueryParam("priceTo") double priceTo,
                               @QueryParam("authorName") String authorName,
                               @QueryParam("authorCountry") String authorCountry
    ) {
        int authorsCount = 0;
        try {
            authorsCount = bookAuthorDao.getRowsCount(bookTitle, priceFrom, priceTo, authorName, authorCountry);
            Gson gson = new Gson();
            JsonObject response = new JsonObject();
            Map<Author, List<Book>> authors = bookAuthorDao.getBooksByAuthor(bookTitle, priceFrom, priceTo, authorName, authorCountry, limit, offset);
            JsonArray authorsArray = new JsonArray();
            for (Map.Entry entry : authors.entrySet()) {
                JsonObject author = new JsonObject();
                author = JsonParser.parseString(gson.toJson(entry.getKey())).getAsJsonObject();
                author.add("books", gson.toJsonTree(entry.getValue()));
                authorsArray.add(author);
            }
            response.add("items", authorsArray);
            response.addProperty("total", authorsCount);
            response.addProperty("offset", offset);
            response.addProperty("limit", limit);
            return Response.ok(response.toString()).build();
        } catch (SQLException e) {
            return Response.status(500, e.getMessage()).build();
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAuthorById(@PathParam("id") int id) {
        try {
            Author author = authorDao.getAuthor(id);
            return Response.ok(author).build();
        } catch (SQLException e) {
            return Response.status(500, e.getMessage()).build();
        }
    }

    @GET
    @Path("/{id}/books")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAuthorBooks(@PathParam("id") int id) {
        try {
            List<Book> books = authorDao.getAuthorBooks(id);
            return Response.ok(new Gson().toJson(books)).build();
        } catch (SQLException e) {
            return Response.status(500, e.getMessage()).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addAuthor(Author author) {
        try{
            authorDao.addAuthor(author);
            return Response.status(201).build();
        } catch (SQLException e) {
            return Response.status(500, e.getMessage()).build();
        }

    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateAuthor(@PathParam("id") int id, Author author) {
        try {
            author.setId(id);
            authorDao.updateAuthor(author);
            return Response.ok().build();
        } catch (SQLException e) {
            return Response.status(500, e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteAuthor(@PathParam("id") int id) {
        try {
            authorDao.deleteAuthor(id);
            return Response.ok().build();
        } catch (SQLException e) {
            return Response.status(500, e.getMessage()).build();
        }
    }
}
