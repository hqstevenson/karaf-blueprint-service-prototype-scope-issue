# karaf-blueprint-service-prototype-scope-issue

According to the book "Enterprise OSGi In Action" (section 6.3.3), when a prototype-scoped bean is exposed
as a service using Blueprint, a new instance of the service should get created for each consuming bundle.

Services exposed in this manner in Karaf are not behaving this way - a single service instance is created and
all consuming bundles use it.  It appears the prototype scope is being ignored since the obseved behaviour is 
what is expected for singleton-scoped beans exposed as services.

Steps to reproduce on Karaf 2.4.3
# Install Camel
karaf@root> features:chooseurl camel 2.16.1
Adding feature url mvn:org.apache.camel.karaf/apache-camel/2.16.1/xml/features
karaf@root> features:install camel

# Install sample bundles
karaf@root> install -s mvn:com.pronoia.karaf/service-interface/1.0.0-SNAPSHOT  mvn:com.pronoia.karaf/service-implementation/1.0.0-SNAPSHOT mvn:com.pronoia.karaf/service-consumer-one/1.0.0-SNAPSHOT mvn:com.pronoia.karaf/service-consumer-two/1.0.0-SNAPSHOT mvn:com.pronoia.karaf/service-consumer-three/1.0.0-SNAPSHOT

# Verify running
karaf@root> list
START LEVEL 100 , List Threshold: 50
   ID   State         Blueprint      Spring    Level  Name
[  58] [Active     ] [            ] [       ] [   50] camel-core (2.16.1)
[  59] [Active     ] [            ] [       ] [   50] camel-catalog (2.16.1)
[  73] [Active     ] [            ] [       ] [   50] geronimo-jta_1.1_spec (1.1.1)
[  74] [Active     ] [            ] [       ] [   50] camel-spring (2.16.1)
[  75] [Active     ] [Created     ] [       ] [   50] camel-blueprint (2.16.1)
[  76] [Active     ] [            ] [       ] [   50] camel-commands-core (2.16.1)
[  77] [Active     ] [Created     ] [       ] [   50] camel-karaf-commands (2.16.1)
[  78] [Active     ] [            ] [       ] [   80] Karaf Issue :: Service Interface (1.0.0.SNAPSHOT)
[  79] [Active     ] [Created     ] [       ] [   80] Karaf Issue :: Service Implementation (1.0.0.SNAPSHOT)
[  80] [Installed  ] [            ] [       ] [   80] Karaf Issue :: Service Consumer One (1.0.0.SNAPSHOT)
[  81] [Installed  ] [            ] [       ] [   80] Karaf Issue :: Service Consumer Two (1.0.0.SNAPSHOT)
[  82] [Installed  ] [            ] [       ] [   80] Karaf Issue :: Service Consumer Three (1.0.0.SNAPSHOT)


# Tail the log and observe the hashCodes for each bundle's service instance are the same
karaf@root> log:tail
2016-01-20 13:42:32,833 | INFO  | r://consumer-one | consumer-one-route               | 58 - org.apache.camel.camel-core - 2.16.1 | Timer Fired
2016-01-20 13:42:32,834 | INFO  | r://consumer-one | consumer-one-route               | 58 - org.apache.camel.camel-core - 2.16.1 | Consumer One Result: TestServiceImplementation.execute(): hashCode=1694897032
2016-01-20 13:42:32,888 | INFO  | r://consumer-two | consumer-two-route               | 58 - org.apache.camel.camel-core - 2.16.1 | Timer Fired
2016-01-20 13:42:32,888 | INFO  | r://consumer-two | consumer-two-route               | 58 - org.apache.camel.camel-core - 2.16.1 | Consumer Two Result: TestServiceImplementation.execute(): hashCode=1694897032
2016-01-20 13:42:32,999 | INFO  | //consumer-three | consumer-three-route             | 58 - org.apache.camel.camel-core - 2.16.1 | Timer Fired
2016-01-20 13:42:32,999 | INFO  | //consumer-three | consumer-three-route             | 58 - org.apache.camel.camel-core - 2.16.1 | Consumer Three Result: TestServiceImplementation.execute(): hashCode=1694897032


Steps to reproduce on Karaf 3.0.5
# Install Camel
karaf@root()> feature:repo-add camel 2.16.1
Adding feature url mvn:org.apache.camel.karaf/apache-camel/2.16.1/xml/features
karaf@root()> feature:install camel

# Install sample bundles
karaf@root> install -s mvn:com.pronoia.karaf/service-interface/1.0.0-SNAPSHOT  mvn:com.pronoia.karaf/service-implementation/1.0.0-SNAPSHOT mvn:com.pronoia.karaf/service-consumer-one/1.0.0-SNAPSHOT mvn:com.pronoia.karaf/service-consumer-two/1.0.0-SNAPSHOT mvn:com.pronoia.karaf/service-consumer-three/1.0.0-SNAPSHOT

# Verify running
karaf@root()> list
START LEVEL 100 , List Threshold: 50
ID | State  | Lvl | Version        | Name                                 
--------------------------------------------------------------------------
70 | Active |  50 | 2.16.1         | camel-core                           
71 | Active |  50 | 2.16.1         | camel-catalog                        
86 | Active |  50 | 1.1.1          | geronimo-jta_1.1_spec                
87 | Active |  50 | 2.16.1         | camel-spring                         
88 | Active |  50 | 2.16.1         | camel-blueprint                      
92 | Active |  80 | 1.0.0.SNAPSHOT | Karaf Issue :: Service Interface     
93 | Active |  80 | 1.0.0.SNAPSHOT | Karaf Issue :: Service Implementation
94 | Active |  80 | 1.0.0.SNAPSHOT | Karaf Issue :: Service Consumer One  
95 | Active |  80 | 1.0.0.SNAPSHOT | Karaf Issue :: Service Consumer Two  
96 | Active |  80 | 1.0.0.SNAPSHOT | Karaf Issue :: Service Consumer Three

# Tail the log and observe the hashCodes for each bundle's service instance are the same
karaf@root> log:tail
2016-01-20 13:48:37,350 | INFO  | r://consumer-one | consumer-one-route               | 70 - org.apache.camel.camel-core - 2.16.1 | Timer Fired
2016-01-20 13:48:37,350 | INFO  | r://consumer-one | consumer-one-route               | 70 - org.apache.camel.camel-core - 2.16.1 | Consumer One Result: TestServiceImplementation.execute(): hashCode=2033130779
2016-01-20 13:48:37,452 | INFO  | r://consumer-two | consumer-two-route               | 70 - org.apache.camel.camel-core - 2.16.1 | Timer Fired
2016-01-20 13:48:37,453 | INFO  | r://consumer-two | consumer-two-route               | 70 - org.apache.camel.camel-core - 2.16.1 | Consumer Two Result: TestServiceImplementation.execute(): hashCode=2033130779
2016-01-20 13:48:37,577 | INFO  | //consumer-three | consumer-three-route             | 70 - org.apache.camel.camel-core - 2.16.1 | Timer Fired
2016-01-20 13:48:37,578 | INFO  | //consumer-three | consumer-three-route             | 70 - org.apache.camel.camel-core - 2.16.1 | Consumer Three Result: TestServiceImplementation.execute(): hashCode=2033130779


Steps to reproduce on Karaf 4.0.4

# Install Camel
karaf@root()> feature:repo-add camel 2.16.1
Adding feature url mvn:org.apache.camel.karaf/apache-camel/2.16.1/xml/features
karaf@root()> feature:install camel

# Install sample bundles
karaf@root> install -s mvn:com.pronoia.karaf/service-interface/1.0.0-SNAPSHOT  mvn:com.pronoia.karaf/service-implementation/1.0.0-SNAPSHOT mvn:com.pronoia.karaf/service-consumer-one/1.0.0-SNAPSHOT mvn:com.pronoia.karaf/service-consumer-two/1.0.0-SNAPSHOT mvn:com.pronoia.karaf/service-consumer-three/1.0.0-SNAPSHOT

# Verify running
karaf@root()> list
START LEVEL 100 , List Threshold: 50
ID | State  | Lvl | Version        | Name
--------------------------------------------------------------------------
52 | Active |  50 | 2.16.1         | camel-blueprint
53 | Active |  50 | 2.16.1         | camel-catalog
54 | Active |  80 | 2.16.1         | camel-commands-core
55 | Active |  50 | 2.16.1         | camel-core
56 | Active |  50 | 2.16.1         | camel-spring
57 | Active |  80 | 2.16.1         | camel-karaf-commands
58 | Active |  50 | 1.1.1          | geronimo-jta_1.1_spec
80 | Active |  80 | 1.0.0.SNAPSHOT | Karaf Issue :: Service Interface
81 | Active |  80 | 1.0.0.SNAPSHOT | Karaf Issue :: Service Implementation
82 | Active |  80 | 1.0.0.SNAPSHOT | Karaf Issue :: Service Consumer One
83 | Active |  80 | 1.0.0.SNAPSHOT | Karaf Issue :: Service Consumer Two
84 | Active |  80 | 1.0.0.SNAPSHOT | Karaf Issue :: Service Consumer Three

# Tail the log and observe the hashCodes for each bundle's service instance are the same
karaf@root> log:tail
2016-01-20 13:51:22,373 | INFO  | r://consumer-one | consumer-one-route               | 55 - org.apache.camel.camel-core - 2.16.1 | Timer Fired
2016-01-20 13:51:22,374 | INFO  | r://consumer-one | consumer-one-route               | 55 - org.apache.camel.camel-core - 2.16.1 | Consumer One Result: TestServiceImplementation.execute(): hashCode=1116989237
2016-01-20 13:51:22,484 | INFO  | r://consumer-two | consumer-two-route               | 55 - org.apache.camel.camel-core - 2.16.1 | Timer Fired
2016-01-20 13:51:22,484 | INFO  | r://consumer-two | consumer-two-route               | 55 - org.apache.camel.camel-core - 2.16.1 | Consumer Two Result: TestServiceImplementation.execute(): hashCode=1116989237
2016-01-20 13:51:22,592 | INFO  | //consumer-three | consumer-three-route             | 55 - org.apache.camel.camel-core - 2.16.1 | Timer Fired
2016-01-20 13:51:22,592 | INFO  | //consumer-three | consumer-three-route             | 55 - org.apache.camel.camel-core - 2.16.1 | Consumer Three Result: TestServiceImplementation.execute(): hashCode=1116989237
