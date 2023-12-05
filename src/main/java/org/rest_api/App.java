package org.rest_api;

import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.rest_api.rest_resources.AuthorResource;
import org.rest_api.rest_resources.BookResource;

import java.net.URI;
import java.util.HashSet;
import java.util.List;

public class App {
    public static void main(String[] args) {
        ResourceConfig resourceConfig = new ResourceConfig(new HashSet<>(List.of(BookResource.class, AuthorResource.class)));
        GrizzlyHttpServerFactory.createHttpServer(URI.create("http://localhost:8080/bookstore/"),resourceConfig);
    }
}
