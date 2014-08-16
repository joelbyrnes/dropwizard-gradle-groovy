package com.adeptusproductions.sse.channel

import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.MultivaluedMap
import javax.ws.rs.core.Response

@Path("/channels")
public class ChannelsResource {

    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)
    public Map list() {
        def channels = Channels.channels.collect {
            [name: it.key, users: it.value.listenerCount()]
        }
        return [channels: channels]
    }

    // experimenting with how to make a user quit immediately instead of waiting for connection to close
    // since after all there isn't a "server" concept like on irc, separate from channels
    // although it could be implemented as a channel itself.
    @Path("quit")
    @POST
    @Consumes("application/x-www-form-urlencoded")
    public Response userQuit(MultivaluedMap<String, String> formParams) {
        // need to be able to associate a message with a listener, which is currently impossible
        // without session tracking or even usernames
//        Channels.userQuit(who)
//        Channels.channels.get(channel).pub("user-quit", formParams)
        return Response.ok().build()
    }

}
