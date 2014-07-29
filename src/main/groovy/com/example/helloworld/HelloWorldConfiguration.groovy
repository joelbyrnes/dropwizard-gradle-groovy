package com.example.helloworld

import com.example.helloworld.core.Template
import io.dropwizard.Configuration
import org.hibernate.validator.constraints.NotEmpty

public class HelloWorldConfiguration extends Configuration {
    @NotEmpty
    String template;

    @NotEmpty
    String defaultName = "Stranger";

    public Template buildTemplate() {
        return new Template(template, defaultName);
    }
}
