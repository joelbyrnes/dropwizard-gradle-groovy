package com.adeptusproductions.sse.channel

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

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

}
