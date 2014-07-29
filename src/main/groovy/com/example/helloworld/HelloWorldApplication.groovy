package com.example.helloworld

import com.adeptusproductions.sse.channel.ChannelEventSourceServlet
import com.adeptusproductions.sse.channel.ChannelResource
import com.adeptusproductions.sse.random.RandomNumberEventSourceServlet
import com.example.helloworld.cli.RenderCommand
import com.example.helloworld.core.Template
import com.example.helloworld.health.TemplateHealthCheck
import com.example.helloworld.resources.HelloWorldResource
import com.example.helloworld.resources.SoundResource
import com.example.helloworld.resources.ViewResource
import io.dropwizard.Application
import io.dropwizard.assets.AssetsBundle
import io.dropwizard.setup.Bootstrap
import io.dropwizard.setup.Environment
import io.dropwizard.views.ViewBundle

public class HelloWorldApplication extends Application<HelloWorldConfiguration> {
    public static void main(String[] args) throws Exception {
        new HelloWorldApplication().run(args)
    }

    @Override
    String getName() {
        return "hello-world"
    }

    @Override
    public void initialize(Bootstrap<HelloWorldConfiguration> bootstrap) {
        bootstrap.addCommand(new RenderCommand())
        bootstrap.addBundle(new AssetsBundle())
        bootstrap.addBundle(new ViewBundle())
    }

    @Override
    public void run(HelloWorldConfiguration configuration,
                    Environment environment) throws ClassNotFoundException {
        final Template template = configuration.buildTemplate()

        environment.healthChecks().register("template", new TemplateHealthCheck(template))
        environment.jersey().register(new HelloWorldResource(template))
        environment.jersey().register(new ViewResource())
        environment.jersey().register(new SoundResource())

        environment.servlets().addServlet("random", new RandomNumberEventSourceServlet()).addMapping("/randomNumber")

        environment.jersey().register(new ChannelResource());
        environment.servlets().addServlet("channel", new ChannelEventSourceServlet()).addMapping("/channel/events")

//        environment.servlets().addServlet("chat-channel", new ChatEventSourceServlet()).addMapping("/chat/channel")
//        // TODO should be replaced with resource
//        environment.servlets().addServlet("chat-send", new SendServlet()).addMapping("/chat/send")
    }
}
