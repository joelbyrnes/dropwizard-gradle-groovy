package com.adeptusproductions.sse.channel

/**
 * Created with IntelliJ IDEA.
 * User: joel
 * Date: 18/07/14
 * Time: 5:08 PM
 * To change this template use File | Settings | File Templates.
 */
class Channels {
    // TODO methods should be synchronized
    static Map<String, Channel> channels = [:].asSynchronized().withDefault { String name -> newChannel(name) }

    static Channel newChannel(String name) {
        // TODO check if existing... shouldn't happen, but did that one time.
        def c = new Channel(name)
        println "new channel created: ${name}"
        c
    }

    static void userParted(Listener listener) {
        channels.values().each { it.userParted(listener) }
    }

    static void userDropped(Listener listener) {
        channels.values().each { it.userDropped(listener) }
    }

    static void userQuit(Listener listener) {
        channels.values().each { it.userDropped(listener) }
    }
}
