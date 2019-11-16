package com.eying.pcss.workflow.service;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

/**
 * 调用company获取企业角色信息
 */
@Service
@Log
public class FindRolesService {

    @Value("${rest.company.roles}")
    private String companyUrl;

    public List<Map<String, Object>> findCompanyIsExist(String rolesId) {
        Response response = null;
        Client client = ClientBuilder.newClient ();
        companyUrl = companyUrl + "/" + rolesId + "/staffs";
        try {
            response = client.target (companyUrl).request ().accept (MediaType.APPLICATION_JSON).get ();
        } catch (Exception e) {
            log.severe (e.getMessage ());
            throw e;
        }
        if (response.getStatus () == 200) {
            List<Map<String, Object>> list = response.readEntity (List.class);
            if (list == null) {
                return null;
            } else {
                return list;
            }
        } else {
            return null;
        }
    }
}
