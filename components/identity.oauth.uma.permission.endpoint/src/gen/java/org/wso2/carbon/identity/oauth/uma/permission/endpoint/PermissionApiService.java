package org.wso2.carbon.identity.oauth.uma.permission.endpoint;

import org.wso2.carbon.identity.oauth.uma.permission.endpoint.dto.ResourceModelDTO;

import javax.ws.rs.core.Response;

public abstract class PermissionApiService {
    public abstract Response requestPermission(ResourceModelDTO requestedPermission);
}

