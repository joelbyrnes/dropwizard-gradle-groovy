package com.adeptusproductions.sse.channel

import org.eclipse.jetty.servlets.EventSource
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/* see also
http://html5doctor.com/server-sent-events/
http://today.java.net/article/2010/03/31/html5-server-push-technologies-part-1
http://www.html5rocks.com/en/tutorials/eventsource/basics/
http://www.w3.org/TR/eventsource/
 */

class ChannelEventSource implements EventSource {
    private static final Logger LOG = LoggerFactory.getLogger(ChannelEventSource.class)

    private EventSource.Emitter emitter
    private String id

    public ChannelEventSource() {
        this.id = UUID.randomUUID().toString()
    }

    @Override
    public void onOpen(EventSource.Emitter emitter) throws IOException {
        LOG.info("onOpen")
        this.emitter = emitter
        EventPublisher.message("user ${id} joined (eventSource onOpen)")
    }

    @Override
    public void onClose() {
        LOG.info("onClose")
        EventPublisher.removeListener(this)
        EventPublisher.message("user ${id} left (eventSource onClose)")
    }

    public void data(String dataToSend) throws IOException {
        LOG.info("emitEvent data: " + dataToSend)
        this.emitter?.data(dataToSend)
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
        if (obj instanceof ChannelEventSource) {
            ChannelEventSource that = (ChannelEventSource)obj
            return Objects.equals(this.id, that.id)
        }
        return false
    }
}