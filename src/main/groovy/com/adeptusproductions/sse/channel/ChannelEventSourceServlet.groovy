package com.adeptusproductions.sse.channel

import org.eclipse.jetty.servlets.EventSource
import org.eclipse.jetty.servlets.EventSourceServlet
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.servlet.http.HttpServletRequest

public class ChannelEventSourceServlet extends EventSourceServlet {
    private static final Logger LOG = LoggerFactory.getLogger(ChannelEventSourceServlet.class)

	@Override
	protected EventSource newEventSource(HttpServletRequest request) {
        LOG.info("ChannelEventSourceServlet")
        def l = new Listener()
        println "eventsource request for channel ${request.getParameter("channel")}"
        def channel = Channels.channels.get(request.getParameter("channel"))
        channel.addListener(l)
        return l
	}
}