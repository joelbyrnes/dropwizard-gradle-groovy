package com.adeptusproductions.sse.channel

import com.google.common.base.Charsets
import io.dropwizard.views.View

import javax.ws.rs.Consumes
import javax.ws.rs.FormParam
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.MultivaluedMap
import javax.ws.rs.core.Response

@Path("/channel/{channel}")
public class ChannelResource {

    @Path("message")
    @POST
    public Response message(@PathParam("channel") String channel, @FormParam("msg") String msg) {
        Channels.channels.get(channel).message(msg)
        return Response.ok().build()
    }

    @Path("play")
    @POST
    @Consumes("application/x-www-form-urlencoded")
    public Response play(@PathParam("channel") String channel, MultivaluedMap<String, String> formParams) {
        println "received this data: "
        formParams.each { println "${it.key}: ${it.value}"}

        Channels.channels.get(channel).sound(formParams.sound)
        return Response.ok().build()
    }

    @Path("sound")
    @POST
    @Consumes("application/x-www-form-urlencoded")
    public Response receiveData(@PathParam("channel") String channel, MultivaluedMap<String, String> formParams) {
        println "received this data: "
        formParams.each { println "${it.key}: ${it.value}"}

        Channels.channels.get(channel).pub("sound", formParams)
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
        def view = new View("/views/ftl/${channelName}.ftl", Charsets.UTF_8) {
            String channel
        }
        view.channel = channelName
        return view
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
