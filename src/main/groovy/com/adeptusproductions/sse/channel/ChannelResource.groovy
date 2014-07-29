package com.adeptusproductions.sse.channel

import com.fasterxml.jackson.annotation.JsonProperty
import com.google.common.base.Charsets
import groovy.json.JsonBuilder
import io.dropwizard.views.View
import org.hibernate.validator.constraints.Length

import javax.ws.rs.FormParam
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/channel")
public class ChannelResource {

    @Path("publish")
    @POST
    public Response publish(@FormParam("msg") String msg) {
        EventPublisher.pub(msg)
        return Response.ok().build()
    }

    @Path("message")
    @POST
    public Response message(@FormParam("msg") String msg) {
        EventPublisher.message(msg)
        return Response.ok().build()
    }

    @Path("play")
    @POST
    public Response play(@FormParam("sound") String sound) {
        EventPublisher.sound(sound)
        return Response.ok().build()
    }

    @GET
    @Path("userCount")
    @Produces(MediaType.APPLICATION_JSON)
    public Map userCount() {
        return [userCount: EventPublisher.listenerCount()]
    }

    @GET
    @Path("{name}/info")
    @Produces(MediaType.APPLICATION_JSON)
    public Map info(@PathParam("name") String channelName) {
//        Channels.channels.get(channelName).listenerCount()
        return [name: channelName,
                userCount: EventPublisher.listenerCount()]
    }

    @GET
    @Path("/")
    @Produces("text/html;charset=UTF-8")
    public View defaultChannelPage() {
        return new View("/views/ftl/pig.ftl", Charsets.UTF_8) {
        }
    }

    @GET
    @Path("log")
    @Produces("text/html;charset=UTF-8")
    public View logPage() {
        return new View("/views/ftl/log.ftl", Charsets.UTF_8) {
        }
    }
}
