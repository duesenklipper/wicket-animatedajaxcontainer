**Current version**: 1.0.0.wicket7 for Wicket 7.x

# Automatic Animation for Wicket Containers

This project provides a Wicket Behavior named ```AjaxAutoAnimatingBehavior```. You can simply add
this to any Wicket component to have the appearance and disappearance of that component animated
when its visibility changes during an ajax request.


    WebMarkupContainer wmc = new WebMarkupContainer("container");
    this.add(wmc);
    wmc.add(new AjaxAutoAnimatingBehavior());

Without any further code, this component will now fade in or out if its visibility is changed 
during an ajax request. See the ```animatedajaxcontainer-examples``` subdirectory for a running 
example


For a different animation, override ```AjaxAutoAnimatingBehavior#getOpenAnimation``` and/or
```#getCloseAnimation```. 
  
## Maven coordinates

    <dependency>
        <groupId>de.wicketbuch.extensions</groupId>
        <artifactId>appendablerepeater</artifactId>
        <version>1.0.0.wicket7</version>
    </dependency>

This project uses [Semantic Versioning](http://semver.org/), so you can rely on
things not breaking within a major version.
