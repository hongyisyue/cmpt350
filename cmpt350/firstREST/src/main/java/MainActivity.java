package main.java;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by homeyxue on 2018-03-19.
 */
@Path("/hello")
public class MainActivity {
    @GET
    @Produces(MediaType.TEXT_HTML)
    public String getMessage(){return "hello";}
}
