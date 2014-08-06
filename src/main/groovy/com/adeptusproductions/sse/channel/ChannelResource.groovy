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

@Path("/channel/{channel}")
public class ChannelResource {

    @Path("message")
    @POST
    public Response message(@PathParam("channel") String channel, @FormParam("msg") String msg) {
//        EventPublisher.message(msg)
        Channels.channels.get(channel).message(msg)
        return Response.ok().build()
    }

    @Path("play")
    @POST
    public Response play(@PathParam("channel") String channel, @FormParam("sound") String sound) {
//        EventPublisher.sound(sound)
        Channels.channels.get(channel).sound(sound)
        return Response.ok().build()
    }

    @GET
    @Path("userCount")
    @Produces(MediaType.APPLICATION_JSON)
    public Map userCount(@PathParam("channel") String channelName) {
        def channel = Channels.channels.get(channelName)
        return [userCount: channel.listenerCount()]
    }

    @GET
    @Path("info")
    @Produces(MediaType.APPLICATION_JSON)
    public Map info(@PathParam("channel") String channelName) {
        def channel = Channels.channels.get(channelName)
        return [name: channelName,
                userCount: channel.listenerCount()]
    }

    @GET
    @Path("/")
    @Produces("text/html;charset=UTF-8")
    public View defaultChannelPage(@PathParam("channel") String channelName) {
        return new View("/views/ftl/${channelName}.ftl", Charsets.UTF_8) {
            def channel = "pig"
        }
    }

//    @GET
//    @Path("/")
//    @Produces("text/html;charset=UTF-8")
//    public View getChannel(@PathParam("channel") String channelName) {
//        // TODO 302 redirect to /channel/events?channel=channel
//        return new View("/views/ftl/pig.ftl", Charsets.UTF_8) {
//        }
//    }

    @GET
    @Path("log")
    @Produces("text/html;charset=UTF-8")
    public View logPage(@PathParam("channel") String channelName) {
        return new View("/views/ftl/log.ftl", Charsets.UTF_8) {
        }
    }
}
