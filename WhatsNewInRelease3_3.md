#What's new in 3.3.0 release

# Introduction #

The new 3.3.0 release introduces possibility to describe site layout using an xml file. Put your **site.xml** in **${basedir}/src/site/confluence** folder following the [site schema](http://maven-confluence-plugin.googlecode.com/files/site-schema-3.4.0.xsd) available in download section. To simplify understanding, below there is a simple site descriptor template

## Template ##
```

<?xml version="1.0" encoding="UTF-8"?>

<bsc:site
    xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'
    xmlns:bsc='http://code.google.com/p/maven-confluence-plugin'
    xsi:schemaLocation='http://code.google.com/p/maven-confluence-plugin http://maven-confluence-plugin.googlecode.com/files/site-schema-3.4.0.xsd'>
    <home name="" uri="">
       
        <attachment name="" uri="" comment="" contentType="" version=""></attachment>
        <attachment name="" uri="" comment="" contentType="" version=""></attachment>

        <child name="" uri="">

          <attachment name="" uri="" comment="" contentType="" version=""></attachment>
          <attachment name="" uri="" comment="" contentType="" version=""></attachment>

            <child name="" uri="">
              <attachment name="" uri="" comment="" contentType="" version=""></attachment>
              <attachment name="" uri="" comment="" contentType="" version=""></attachment>
            </child>

            <child name="" uri=""></child>

       </child>
       
        <child name="" uri=""></child>

    </home>

    <label>label1</label>
    <label>label2</label>
    <label>label3</label>

</bsc:site>

```

## Tags description ##

### home ###

| **Attribute**| **Description** | **mandatory** |
|:-------------|:----------------|:--------------|
| name | Title of page | yes |
| uri | Content's source |

### child ###

| **Attribute**| **Description** | **mandatory** |
|:-------------|:----------------|:--------------|
| name | Title of page |
| uri | Content's source |

### attachment ###

| **Attribute**| **Description** | **mandatory** |
|:-------------|:----------------|:--------------|
| name | Name of attachment |
| uri | Content's source |
| comment |  |
| contentType |  |
| version |  |

## URI format ##

The **uri** attribute could refer to
  * **File** resource
> > We can refer to file in **absolute** way using **file** scheme (e.g. ` file:///Documents/page.confluence `) or in **relative** way not using any scheme. In that case the path wiil be resolved stating from **site home**
  * **Classpath** resource
> > We can refer to resource using **classpath** schema. (e.g. ` classpath:page.confluence `)
  * Network resource
> > We can refer to resource using **http** scheme. (e.g. ` http://www.thesite.com/page.confluence `)



