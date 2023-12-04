package org.rest_api.rest_resources;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.rest_api.dao.AuthorDao;
import org.rest_api.entity.Author;
import org.rest_api.entity.Book;
import org.rest_api.error_handling.ErrorResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/authors")
public class AuthorResource {
    private final AuthorDao authorDao = new AuthorDao();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAuthors(@QueryParam("offset") int offset,
                             @QueryParam("limit") int limit) {
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
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAuthorById(@PathParam("id") int id){
        Author author = authorDao.getAuthor(id);
        if(author != null){
            return Response.ok(author).build();
        }else {
            return Response.status(404)
                    .entity(new ErrorResponse(Response.Status.NOT_FOUND.name(),
                            "Author with id " + id + " not found"))
                    .build();
        }
    }

    @GET
    @Path("/{id}/books")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAuthorBooks(@PathParam("id") int id){
        List<Book> books = authorDao.getAuthorBooks(id);
        return Response.ok(new Gson().toJson(books)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addAuthor(Author author) {
        authorDao.addAuthor(author);
        return Response.status(201).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void updateAuthor(@PathParam("id") int id, Author author) {
        author.setId(id);
        authorDao.updateAuthor(author);
    }

    @DELETE
    @Path("/{id}")
    public void deleteAuthor(@PathParam("id") int id) {
        authorDao.deleteAuthor(id);
    }
}
