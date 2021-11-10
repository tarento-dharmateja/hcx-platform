package org.swasth.hcx.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
public class HealthController extends BaseController {

    @RequestMapping(value = "/service/health", method = RequestMethod.GET)
    public ResponseEntity<Object> serviceHealth() {
        Map<String,Object> response = new LinkedHashMap<>();
        response.put("timestamp", String.valueOf(System.currentTimeMillis()));
        response.put("health",true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/health", method = RequestMethod.GET)
    public ResponseEntity<Object> health() {
        Map<String,Object> response = new LinkedHashMap<>();
        response.put("timestamp", String.valueOf(System.currentTimeMillis()));
        List<Map<String,Object>> allChecks = new ArrayList<>();
        List<Boolean> getAllHealth = new ArrayList<>();
        allChecks.add(checkKafkaHealth());
        allChecks.forEach(check -> getAllHealth.add((Boolean) check.get("healthy")));
        response.put("checks", allChecks);
        response.put("overall health", !getAllHealth.contains(false));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public Map checkKafkaHealth(){
        String serviceName = "kafka";
        if(kafkaClient.health()){
            return generateCheck(serviceName, true);
        } else {
            return generateCheck(serviceName, false);
        }
    }

    public Map generateCheck(String serviceName, Boolean health){
        return new LinkedHashMap(){{
           put("name", serviceName);
           put("healthy", health);
        }};
    }

}
