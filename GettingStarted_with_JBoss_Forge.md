**THIS DOCUMENTATION HAS BEEN UPDATED TO SUPPORT JBOSS FORGE 2.X
[>>  here](https://github.com/bsorrentino/maven-confluence-reporting-plugin/tree/v4/confluence-forge-plugin)**


---


# Introduction #

[JBoss Forge](http://forge.jboss.org/index.html)  is a powerful java shell that provides facilities for developers.
From version **3.4.2** of confluence plugin, is available a **[JBoss Forge](http://forge.jboss.org/index.html) add on** that provides an easy way to integrate the **confluence plugin** in your pom

# Install plugin #

After starting Forge console type the command shown below
```

$ forge git-plugin https://code.google.com/p/maven-confluence-plugin.forge/ --ref 1.2

```

# Use Forge Add On #

## Setup ##

From Forge console set the current folder at your project and run command `confluence-reporting setup`
```

$ cd my_project
$ [my_project] confluence-reporting setup

```

Forge add on will **setup, configure and/or update** the maven confluence plugin int your pom.

## Download Page ##

From Forge console set the current folder at your project and run command `confluence-reporting downloadPage`
```

$ cd my_project
$ forge -e "confluence-reporting downloadPage username password"

```

Forge read information from project's pom and  will write content output to console

# Forge Version #

The add on has been developed & tested upon version **1.4.1.Final** of  **JBoss Forge distribution**