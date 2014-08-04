package com.adeptusproductions.sse.channel

import groovy.json.JsonBuilder
import org.slf4j.Logger
import org.slf4j.LoggerFactory

public class EventPublisher {
    private final Logger LOG = LoggerFactory.getLogger(EventPublisher.class)
    private List<ChannelEventSource> listeners = Collections.synchronizedList([])

    def sound(String str) {
        pub("sound", [sound: str])
    }

    def message(String str) {
        pub("message", [message: str])
    }

    def pub(String eventName, Map data) {
        JsonBuilder jb = new JsonBuilder()
        jb(data)
        pub(eventName, jb.toString())
    }

    public void pub(String eventName, String data) {
        synchronized(listeners) {
            if (listeners.size() == 0) {
                LOG.warn("no listeners to send to!")
            } else {
                LOG.info("pushing data: '${data}'")
                listeners.each { ChannelEventSource listener ->
                    try {
                        if (eventName) listener.event(eventName, data + "\n")
                        else listener.data(data + "\n")
                    }
                    catch(IOException e) {
                        LOG.error("IOException emitting event to listener: " + e.getMessage())
                        // TODO remove listener? and inform channel?
                    }
                }
            }
        }
    }
    
    public void addListener(ChannelEventSource l) {
        listeners.add(l)
        message("User added. User count now: " + listenerCount())
    }
    
    public void removeListener(ChannelEventSource l) {
        listeners.remove(l)
        message("User removed. User count now: " + listenerCount())
    }

    public Long listenerCount() {
        // do we need to synchronize for a get?
        synchronized(listeners) {
            return listeners.size()
        }
    }
}
