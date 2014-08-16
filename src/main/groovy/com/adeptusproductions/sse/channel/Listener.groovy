package com.adeptusproductions.sse.channel

import groovy.json.JsonBuilder
import org.eclipse.jetty.servlets.EventSource
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/* see also
http://html5doctor.com/server-sent-events/
http://today.java.net/article/2010/03/31/html5-server-push-technologies-part-1
http://www.html5rocks.com/en/tutorials/eventsource/basics/
http://www.w3.org/TR/eventsource/
 */

class Listener implements EventSource {
    private static final Logger LOG = LoggerFactory.getLogger(Listener.class)

    private EventSource.Emitter emitter
    String id
    String name

    public Listener(String name) {
        this.name = name
        this.id = UUID.randomUUID().toString()
    }

    @Override
    public void onOpen(EventSource.Emitter emitter) throws IOException {
        LOG.info("onOpen")
        this.emitter = emitter
        event("server-message", [message: "eventSource onOpen"])
    }

    @Override
    public void onClose() {
        // TODO how to differentiate between user left and connection severed?
        // TODO maybe users have to send a part command and will then be removed from a channel.
        LOG.info("onClose")
        // this probably causes exceptions because the stream is closed
//        event("server-message", [message: "eventSource onClose"])

        // remove from any channel they might have been in
        Channels.userDropped(this)
    }

    public void data(String dataToSend) throws IOException {
        LOG.info("emitEvent data: " + dataToSend)
        this.emitter?.data(dataToSend)
    }

    public void event(String eventName, Map data) throws IOException {
        JsonBuilder jb = new JsonBuilder()
        jb(data)
        def str = jb.toString()
        event(eventName, str)
    }

    public void event(String eventName, String dataToSend) throws IOException {
        LOG.info("emitEvent event '${eventName}', data '$dataToSend'")
        this.emitter?.event(eventName, dataToSend)
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.id)
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Listener) {
            Listener that = (Listener)obj
            return Objects.equals(this.id, that.id)
        }
        return false
    }
}