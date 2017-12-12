[![Build Status](https://travis-ci.org/bliblidotcom/command-plugin.svg?branch=master)](https://travis-ci.org/bliblidotcom/command-plugin)

Spring Boot Command Plugin
--------------------------

Add this dependency in your ```pom.xml```

```xml
<repositories>
    ...
    <repository>
      <snapshots>
        <enabled>false</enabled>
      </snapshots>
      <id>bintray-bliblidotcom-maven</id>
      <name>bintray</name>
      <url>https://dl.bintray.com/bliblidotcom/maven</url>
    </repository>
    ...
</repositories>
```

```xml
<dependencies>
  ...
  <dependency>
   <groupId>com.blibli.oss</groupId>
   <artifactId>command-plugin</artifactId>
   <version>...</version>
 </dependency>
 ...
</dependencies>
```