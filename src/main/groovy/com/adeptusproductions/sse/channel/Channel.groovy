package com.adeptusproductions.sse.channel

import groovy.json.JsonBuilder
import org.slf4j.Logger
import org.slf4j.LoggerFactory

public class Channel {
    private final Logger LOG = LoggerFactory.getLogger(Channel.class)
    private final List<Listener> listeners = Collections.synchronizedList([])

    String name

    def Channel(String name) {
        this.name = name
    }

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
                listeners.each { Listener listener ->
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
    
    public void addListener(Listener l) {
        synchronized(listeners) {
            listeners.add(l)
        }
        pub('channel-message', [message: "User added. User count now: " + listenerCount(),
                name: name,
                userCount: listenerCount()])
    }

    public void userParted(Listener l) {
        removeListener(l)
        pub('server-message', [message: "User left. User count now: " + listenerCount(),
                name: name,
                userCount: listenerCount()])
    }

    public void userDropped(Listener l) {
        removeListener(l)
        pub('server-message', [message: "User dropped. User count now: " + listenerCount(),
                name: name,
                userCount: listenerCount()])
    }
    
    public void removeListener(Listener l) {
        synchronized(listeners) {
            listeners.remove(l)
        }
//        pub('channel-message', [message: "User removed. User count now: " + listenerCount()])
    }

    public Long listenerCount() {
        // do we need to synchronize for a get?
        synchronized(listeners) {
            return listeners.size()
        }
    }
}
