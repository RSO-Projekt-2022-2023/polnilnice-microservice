package si.fri.rso.polnilnice.api.v1.resources;

import com.kumuluz.ee.logs.cdi.Log;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.headers.Header;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import si.fri.rso.polnilnice.lib.Polnilnice;
import si.fri.rso.polnilnice.services.beans.PolnilniceBean;
import com.kumuluz.ee.cors.annotations.CrossOrigin;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;


@Log
@ApplicationScoped
@Path("/polnilnice")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@CrossOrigin(supportedMethods = "GET, POST, HEAD, OPTIONS, PUT, DELETE")
public class PolnilniceResource {

    @Inject
    private PolnilniceBean polnilniceBean;


    @Context
    protected UriInfo uriInfo;

    @Operation(description = "Get all polnilnice metadata.", summary = "Get all metadata")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "List of image metadata",
                    content = @Content(schema = @Schema(implementation = Polnilnice.class, type = SchemaType.ARRAY)),
                    headers = {@Header(name = "X-Total-Count", description = "Number of objects in list")}
            )})
    @GET
    public Response getPolnilnice() {

        List<Polnilnice> polnilnice = polnilniceBean.getPolnilniceFilter(uriInfo);

        return Response.status(Response.Status.OK).entity(polnilnice).build();
    }


    @Operation(description = "Get metadata for an image.", summary = "Get metadata for an image")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "Image metadata",
                    content = @Content(
                            schema = @Schema(implementation = Polnilnice.class))
            )})
    @GET
    @Path("/{polnilniceId}")
    public Response getPolnilnice(@Parameter(description = "Polnilnice ID.", required = true)
                                     @PathParam("polnilniceId") Integer polnilniceId) {

        Polnilnice polnilnice = polnilniceBean.getPolnilnice(polnilniceId);

        if (polnilnice == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.OK).entity(polnilnice).build();
    }

    @Operation(description = "Add image metadata.", summary = "Add metadata")
    @APIResponses({
            @APIResponse(responseCode = "201",
                    description = "Metadata successfully added."
            ),
            @APIResponse(responseCode = "405", description = "Validation error .")
    })
    @POST
    public Response createPolnilnice(@RequestBody(
            description = "DTO object with image metadata.",
            required = true, content = @Content(
            schema = @Schema(implementation = Polnilnice.class))) Polnilnice polnilnice) {

        if ((polnilnice.getCoord_north() == null || polnilnice.getCoord_east() == null || polnilnice.getChargers() == null)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        else {
            polnilnice = polnilniceBean.createPolnilnice(polnilnice);
        }

        return Response.status(Response.Status.CREATED).entity(polnilnice).build();

    }


    @Operation(description = "Update metadata for an image.", summary = "Update metadata")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Metadata successfully updated."
            )
    })
    @PUT
    @Path("{polnilniceId}")
    public Response putPolnilnice(@Parameter(description = "Metadata ID.", required = true)
                                     @PathParam("polnilniceId") Integer polnilniceId,
                                     @RequestBody(
                                             description = "DTO object with image metadata.",
                                             required = true, content = @Content(
                                             schema = @Schema(implementation = Polnilnice.class)))
                                     Polnilnice polnilnice){

        polnilnice = polnilniceBean.putPolnilnice(polnilniceId, polnilnice);

        if (polnilnice == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.NOT_MODIFIED).build();

    }

    @Operation(description = "Delete metadata for an image.", summary = "Delete metadata")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Metadata successfully deleted."
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Not found."
            )
    })
    @DELETE
    @Path("{polnilniceId}")
    public Response deletePolnilnice(@Parameter(description = "Metadata ID.", required = true)
                                        @PathParam("polnilniceId") Integer polnilniceId){

        boolean deleted = polnilniceBean.deletePolnilnice(polnilniceId);

        if (deleted) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }





}
