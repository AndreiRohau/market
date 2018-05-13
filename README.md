# market

1) If needed, settings could be changed in [application.properties] (at ...\Market\Web\src\main\resources\application.properties).
2) Application runs with [App.java] (at ...\Market\Web\src\main\java\by\ras\App.java).
3) Used java v. 1.8.0——121.
4) Apache-Tomcat 8.5.15 preinstalled at your device.
5) Default admin (login: [admin], password: [q1Q]) - will be created during first run.
6) Default user (login: [user], password: [q1Q]) - will be created during first run.
7) You can choose what database to use, here we have two steps:
    A) First, in [application.properties] (at ...\Market\Web\src\main\resources\application.properties) delete '#' before preferable database properties.
    B) Secondly, comment unpropriate dependency in [pom.xml] (at ...\Market\pom.xml).
    C) Remember application requires h2 database only for testing in any case.
    
    I) For ex., if you have choosen MySQL database:
    [application.properties]
        #Choose MySQL or H2db
        #MYSQL
        spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL5Dialect
        spring.datasource.username=root
        spring.datasource.password=root
        spring.datasource.url=jdbc:mysql://localhost:3306/marketdb?createDatabaseIfNotExist=true&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC
        ####H2db
        ###spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_ON_EXIT=FALSE
        ###spring.datasource.username=sa
        ###spring.datasource.password=
        ###spring.datasource.driverClassName=org.h2.Driver
        ###spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect
    [pom.xml]
                <dependency>
                    <groupId>mysql</groupId>
                    <artifactId>mysql-connector-java</artifactId>
                    <version>5.1.45</version>
                </dependency>
                <dependency>
                    <groupId>com.h2database</groupId>
                    <artifactId>h2</artifactId>
                    <scope>test</scope>
                </dependency>
    II) Otherwise, if you have choosen H2 database:
        [application.properties]
            #Choose MySQL or H2db
            ######MYSQL
            #####spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL5Dialect
            #####spring.datasource.username=root
            #####spring.datasource.password=root
            #####spring.datasource.url=jdbc:mysql://localhost:3306/marketdb?createDatabaseIfNotExist=true&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC
            #H2db
            spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_ON_EXIT=FALSE
            spring.datasource.username=sa
            spring.datasource.password=
            spring.datasource.driverClassName=org.h2.Driver
            spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect
        [pom.xml]
            <!--<dependency>-->
                <!--<groupId>mysql</groupId>-->
                <!--<artifactId>mysql-connector-java</artifactId>-->
                <!--<version>5.1.45</version>-->
            <!--</dependency>-->
            <dependency>
                <groupId>com.h2database</groupId>
                <artifactId>h2</artifactId>
                    <!--<scope>test</scope>-->
            </dependency>
