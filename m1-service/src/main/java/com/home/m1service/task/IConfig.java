package com.home.m1service.task;

//This interface is required for @RefreshScope(proxyMode = ScopedProxyMode.INTERFACES) to work correctly with RECORD.
//This functionality is implemented ONLY for [pickup GIT changes dynamically, without restart/redeploy].
//Once configuration change is done in GIT, send this HTTP request for each impacted microservice ->
//-> curl -X POST http://localhost:8090/m1/actuator/refresh
public interface IConfig<T> {
    T get();
}
