# Report Assignment 2

## Code Steps
This code consists of 3 different components: DNS server, DHCP server and Client
Each component is run by running the main in the Driver class for each component
respectively. Both the DNS and DHCP Server has no configuration needed just run
the main. Client has a two runtime options One being `url` and the other being
`repl`. When making a new `ClientRunner` if `repl` is set to true after the
inital IP is gotten you will be thrown in to a Read Eval Print Loop where you
can manually Renew Release and Get an Ip and query the DNS server. If repl is
false and url is set the Client will get an IP the query the DNS and print the
packet the end this is best used for testing. If only url isnt set then it will
just assign an IP and spit you in to the REPL. 

Though the client can renew manually it is not necessary as the client does this
automatically as long as an IP is set.

## Difficulties and Scalability
the only true difficulty faced was I originally started this assignment in rust
so when the requirements change I had to restart

Generally I think my system is well designed the way the automatic renewing is
not perfect rather than having a timer for each connection I run a timer that
checks over the list of given out IP to see if any are expired and frees any
that are. In theory this means that a IP could be assigned for max 9 seconds
over its expiry time.

The Way I designed the System everything is ran by a Driver class but
immediately thrown in to a new thread when any level of parallelism would be
better and not hold up main thread operation.

## Improvement
The one think I would like to improve was I have no data reliability scheme so if
a packet is lost the Client will just hang as its waiting for an ack that will
never come

##General Schema
In order to run the System in much be in a very specific File scheme as
described below

as well the `Gson` and `inet.ipaddr` jars are needed links to those are
found in the README.md and install according to your IDE
```
src
├── Client
│   ├── ClientDriver.java
│   ├── ClientRenewer.java
│   ├── ClientRunner.java
│   └── ClientTest.java
├── DHCP
│   ├── DHCPDriver.java
│   ├── DHCPReleser.java
│   ├── DHCPRenewer.java
│   ├── DHCPRunner.java
│   ├── IP.java
│   ├── IPList.java
│   ├── IPRenew.java
│   └── IPRenewer.java
├── DNS
│   ├── DNSDriver.java
│   └── DNSRunner.java
├── models
│   ├── ClientPacket.java
│   ├── DHCPConfig.java
│   └── DNSIP.java
└── utils
    └── Utils.java
```

