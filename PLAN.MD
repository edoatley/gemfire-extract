# Objectives

Create an app that can allow the user to download the gemfire db contents as a zip file and replace the contents from a
zip file

# Steps

- [x] Create a basic rest API using gemfire
- [ ] Create a simple integration test using dockerised geode
- [ ] Try selecting just a few fields  
- [ ] Create the zip unload
- [ ] Create the zip upload
- [ ] Change the backend to mongo and upload data from a gemfire unload

# References

1. This [repo](https://github.com/edoatley/gemfire-extract)
1. Local Region with some good advice from John Blum [here](https://stackoverflow.com/questions/51929922/how-to-define-persistence-on-local-pivotal-gemfire-region)
1. Great docs on `@EnableClusterAware` that allows you to minimise changes going local to remote gemfire server [here](https://docs.spring.io/spring-boot-data-geode-build/current/reference/html5/guides/getting-started.html#spring-geode-samples-getting-started-run-app-locally)
1. Baeldung page on [gemfire](https://www.baeldung.com/spring-data-gemfire)
1. Page on using docker integration testing [here](https://info.michael-simons.eu/2017/11/20/integration-testing-with-docker-compose-gradle-and-spring-boot/)

## Starting gemfire

run following:

```shell
docker run -it apachegeode/geode   
```

Then in gfsh:

```shell
start locator --name=locator1
start server --name=server1 --locators=localhost[10334]
```

Then after to stop

```shell
stop server --name=server1
stop locator --name=locator1
```