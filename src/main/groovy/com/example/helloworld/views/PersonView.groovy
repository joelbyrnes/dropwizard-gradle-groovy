package com.example.helloworld.views

import com.example.helloworld.core.Person

import io.dropwizard.views.View

public class PersonView extends View {
    final Person person
    public enum Template{
    	FREEMARKER("freemarker/person.ftl"),
    	MUSTACHE("mustache/person.mustache")
    	
    	String templateName
    	private Template(String templateName){
    		this.templateName = templateName
    	}
    }

    public PersonView(PersonView.Template template, Person person) {
        super(template.templateName)
        this.person = person
    }
}