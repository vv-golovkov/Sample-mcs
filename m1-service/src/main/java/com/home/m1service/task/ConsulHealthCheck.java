package com.home.m1service.task;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.Response;
import com.ecwid.consul.v1.agent.model.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;

import java.util.Map;

//http://localhost:<PORT>/actuator/health
//do not forget to enable it in the 'application.yaml' for each service (management.health.consulHealthCheck=true).
//does not affect the start order of Docker containers.
//do not use this approach, if start-containers-order is important! (use it as additional approach)
@Component
@RequiredArgsConstructor
public class ConsulHealthCheck implements HealthIndicator { //ReactiveHealthIndicator (WEBFLUX)
    private final ConsulClient consulClient;

    public Health health() {
        Health.Builder hBuilder = new Health.Builder().withDetail("my-name", "VitaliyConsul");
        try {
            //ok  ->"10.0.0.1:8300"
            //fail->""
            Response<String> leader = consulClient.getStatusLeader();
            String body = leader.getValue();
            boolean ok = body != null && body.length() > 2;
            if (ok) {
                Response<Map<String, Service>> services = consulClient.getAgentServices();
                return hBuilder.withDetail("my-status", "Leader %s".formatted(body)).withDetails(services.getValue()).
                        status(Status.UP).build();
                //OR -> return Health.up().withDetail("my-status", "OK").build();
            }
            return hBuilder.withDetail("my-status", "No leader").status(Status.UNKNOWN).build();
        } catch (Exception e) { //Health.down(e)
            return hBuilder.withException(e).status(Status.DOWN).build();
        }
    }
}
