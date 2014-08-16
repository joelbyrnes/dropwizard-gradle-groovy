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
        def channelParam = request.getParameter("channel")
        def userParam = request.getParameter("user")
        println "eventsource request for channel ${channelParam} for user ${userParam}"
        def l = new Listener(userParam)
        def channel = Channels.channels.get(request.getParameter("channel"))
        channel.addListener(l)
        // TODO how to return user their unique id? also track sessions?
        return l
	}
}