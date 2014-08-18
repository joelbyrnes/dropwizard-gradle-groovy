package com.adeptusproductions.sse.channel

import com.google.common.base.Charsets
import io.dropwizard.views.View

import javax.ws.rs.Consumes
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

    @Path("{eventName}")
    @POST
    @Consumes("application/x-www-form-urlencoded")
    public Response receiveData(@PathParam("channel") String channel,
                                @PathParam("eventName") String eventName,
                                MultivaluedMap<String, String> formParams) {
//        println "generic event method received this data: "
//        formParams.each { println "${it.key}: ${it.value}"}

        Channels.channels.get(channel).pub(eventName, formParams)
        return Response.ok().build()
    }

    @GET
    @Path("info")
    @Produces(MediaType.APPLICATION_JSON)
    public Map info(@PathParam("channel") String channelName) {
        def channel = Channels.channels.get(channelName)
        return [name: channel.name,
                userCount: channel.listenerCount()]
    }

    @GET
    @Path("/")
    @Produces("text/html;charset=UTF-8")
    public View defaultChannelPage(@PathParam("channel") String channelName) {
        getViewForChannel(channelName, channelName)
    }

    private View getViewForChannel(String templateName, String channelName) {
        def view = new View("/views/ftl/${templateName}.ftl", Charsets.UTF_8) {
            String channel
        }
        view.channel = channelName
        view
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
        getViewForChannel("log", channelName)
    }

    @GET
    @Path("test")
    @Produces("text/html;charset=UTF-8")
    public View test(@PathParam("channel") String channelName) {
        getViewForChannel("test", channelName)
    }

}
