package com.adeptusproductions.sse.random

import org.eclipse.jetty.servlets.EventSource
import org.eclipse.jetty.servlets.EventSourceServlet

import javax.servlet.http.HttpServletRequest

public class RandomNumberEventSourceServlet extends EventSourceServlet {
	@Override
	protected EventSource newEventSource(HttpServletRequest request) {
		return new RandomNumberEventSource()
	}
}