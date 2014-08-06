package com.adeptusproductions.sse.channel

/**
 * Created with IntelliJ IDEA.
 * User: joel
 * Date: 18/07/14
 * Time: 5:08 PM
 * To change this template use File | Settings | File Templates.
 */
class Channels {
    static Map<String, EventPublisher> channels = [:].withDefault { new EventPublisher() }

    static void userParted(ChannelEventSource channelEventSource) {
        channels.values().each { it.userParted(channelEventSource) }
    }

    static void userDropped(ChannelEventSource channelEventSource) {
        channels.values().each { it.userDropped(channelEventSource) }
    }
}
