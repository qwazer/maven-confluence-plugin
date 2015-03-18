# Introduction #

Below the snippet that must be added in your pom  within plugin's section


# MAVEN 3.1.0 #

**From release 3.1.0 of maven we have to use the release 3.3 of maven-site plugin**

```
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-site-plugin</artifactId>
    <version>3.3</version>
    <configuration>
     .....
     .....

```

# MAVEN 3 #

```
<!--
===========================================
MAVEN 3
===========================================
-->
<plugin>
<groupId>org.apache.maven.plugins</groupId>
<artifactId>maven-site-plugin</artifactId>
<version>3.0</version>
<configuration>
	<reportPlugins>
		<plugin>
		<groupId>org.bsc.maven</groupId>
		<artifactId>maven-confluence-reporting-plugin</artifactId>
		<version>3.3.0-beta3</version>
		<reportSets>
		<reportSet>
			<id>confluence</id>
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
			<reports>
				<report>confluence-summary</report>				
			</reports>
		</reportSet>
		</reportSets>			
		</plugin>
	</reportPlugins>
</configuration>
</plugin>
```

# MAVEN 2 #

```
 <!--
===========================================
MAVEN 2
===========================================
--> 
  <reporting>
    <plugins>
        <plugin>
            <groupId>org.bsc.maven</groupId>
            <artifactId>maven-confluence-reporting-plugin</artifactId>
            <version>1.3.2</version>
            <reportSets>
                <reportSet>
                    <id>confluence</id>
                    <configuration>
                        <endPoint>${confluence.home}/rpc/xmlrpc</endPoint>
  						
                        <spaceKey>DEV</spaceKey>
                        <parentPageTitle>TEST</parentPageTitle>
						 
                        <children>
                            <child>
                                <name>child1</name>
                                <source>${basedir}/src/site/confluence/child1.wiki</source>
                            </child>
                            <child>
                                <name>child2</name>
                            </child>
                        </children>
                    </configuration>
                    <reports>
                        <report>confluence-summary</report>				
                    </reports>
                </reportSet>
            </reportSets>			
  
        </plugin>
    </plugins>
</reporting>


```