package com.eying.pcss.workflow.service;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

/**
 * 调用company获取员工信息
 */
@Service
@Log
public class FindStaffService {

    @Value("${rest.company.staffId}")
    private String staffUrl;


    public Boolean findStaffIsExist(String staffId) {
        Response response = null;
        Client client = ClientBuilder.newClient ();
        try {
            response = client.target (staffUrl).path (staffId).request ().accept (MediaType.APPLICATION_JSON).get ();
        } catch (Exception e) {
            log.severe (e.getMessage ());
            throw e;
        }
        if (response.getStatus () == 200) {
            Map<String, Object> map = response.readEntity (Map.class);
            if (map == null) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
}
