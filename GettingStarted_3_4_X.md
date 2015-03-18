# Introduction #

From release 3.4.x, there is the possibility to use **direct goal** to deploy project's documentation to confluence, so, no longer needed to use **maven-site-plugin**


# Direct Deploy #

Direct deployment of  the documentation is pretty simple. It's enough declare plugin within plugins section (as usual) and configure it. **_Know that plugin's configuration has not been changed from last 3.3.x release_**.

## Declare Plugin ##

```
<plugin>
 <groupId>org.bsc.maven</groupId>
 <artifactId>maven-confluence-reporting-plugin</artifactId>
 <configuration>
	<endPoint>${confluence.home}/rpc/xmlrpc</endPoint>
	<spaceKey>TEST</spaceKey>
	<parentPageTitle>Home</parentPageTitle>
	<title>my-site-generation-test</title>
	<labels>
		<label>test</label>
		<label>confluence</label>                                           
	</labels> 
	<templateWiki>${basedir}/src/site/confluence/template.confluence</templateWiki>
	<wikiFilesExt>.confluence</wikiFilesExt>
	<serverId>confluence-server-id</serverId>
	<properties>
		<prop>the_text_value</prop><!-- SIMPLE TEXT -->
		<prop1>classpath:plugin-report.properties</prop1><!-- FROM CLASSPATH -->
		<prop2>file://${basedir}/confluence.html</prop2><!-- FROM FILE SYSTEM -->
	</properties>
 </configuration>
</plugin>
```

## Use plugin ##

After declaration it's enough to call **deploy** goal as shown below

```

> mvn confluence-reporting:deploy

```

## Delete Page(s) ##

After declaration it's enough to call **delete** goal as shown below.

```

> mvn confluence-reporting:delete -Drecursive=false
> mvn confluence-reporting:delete -Drecursive=true

```

By default **recursive**=_true_

## Export Page ##

After declaration it's enough to call **export** goal as shown below.

```

> mvn confluence-reporting:export -DoutputType=pdf -DoutputFile=export.pdf
> mvn confluence-reporting:export -DoutputType=doc -DoutputFile=export.doc

```

By default **outputType**=_pdf_ and **outputFile**=_${project.build.directory}/${title}.${outputType}_