package com.adeptusproductions.sse.channel

import groovy.json.JsonBuilder
import org.slf4j.Logger
import org.slf4j.LoggerFactory

public class EventPublisher {
    private static final Logger LOG = LoggerFactory.getLogger(EventPublisher.class)
    private static List<ChannelEventSource> listeners = Collections.synchronizedList([])

    static def sound(String str) {
        JsonBuilder jb = new JsonBuilder()
        jb([sound: str])
        pub(jb.toString(), "sound")     // needs data newline?
    }

    static def message(String str) {
        JsonBuilder jb = new JsonBuilder()
        jb([message: str])
        pub(jb.toString(), "message")     // needs data newline?
    }

    public static void pub(String message, String eventName = null) {
        synchronized(listeners) {
            if (listeners.size() == 0) {
                LOG.warn("no listeners to send to!")
            } else {
                LOG.info("pushing message: '${message}'")
                listeners.each { ChannelEventSource listener ->
                    try {
                        // TODO ensure correct number of newlines
                        if (eventName) listener.event(eventName, message + "\n")
                        else listener.data(message + "\n")
                    }
                    catch(IOException e) {
                        LOG.error("IOException emitting event to listener: " + e.getMessage())
                        // TODO remove listener? and inform channel?
                    }
                }
            }
        }
    }
    
    public static void addListener(ChannelEventSource l) {
        listeners.add(l)
        message("User added. User count now: " + listenerCount())
    }
    
    public static void removeListener(ChannelEventSource l) {
        listeners.remove(l)
        message("User removed. User count now: " + listenerCount())
    }

    public static Long listenerCount() {
        // do we need to synchronize for a get?
        synchronized(listeners) {
            return listeners.size()
        }
    }
}
