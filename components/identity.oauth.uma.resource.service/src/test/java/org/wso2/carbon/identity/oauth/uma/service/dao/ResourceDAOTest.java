/*
 *  Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.wso2.carbon.identity.oauth.uma.service.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.testng.IObjectFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.ObjectFactory;
import org.testng.annotations.Test;
import org.wso2.carbon.identity.core.util.IdentityDatabaseUtil;
import org.wso2.carbon.identity.oauth.uma.service.dao.util.DAOUtils;
import org.wso2.carbon.identity.oauth.uma.service.exceptions.UMAException;
import org.wso2.carbon.identity.oauth.uma.service.exceptions.UMAServiceException;
import org.wso2.carbon.identity.oauth.uma.service.model.Resource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;


@PrepareForTest(IdentityDatabaseUtil.class)

public class ResourceDAOTest extends DAOUtils {

    private static final Log log = LogFactory.getLog(ResourceDAOTest.class);

    private static final String DB_NAME = "regdb";

    @BeforeClass
    public void setUp() throws Exception {

        initiateH2Base(DB_NAME, getFilePath("resource.sql"));
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        createResourceTable(DB_NAME, "1", "PhotoAlbem", timestamp, "1",
                1234);
        createResourceMetaDataTable(DB_NAME, "icon_uri",
                "http://www.example.com/icons/sharesocial.png", (long) 1);
        createResourceScopeTable(DB_NAME, (long) 1, "view");
    }

    @AfterClass
    public void tearDown() throws Exception {

        closeH2Base(DB_NAME);
    }

    @ObjectFactory
    public IObjectFactory getObjectFactory() {

        return new org.powermock.modules.testng.PowerMockObjectFactory();
    }

    @Test
    public void testRegisterResource() throws Exception {

        mockStatic(IdentityDatabaseUtil.class);
        try (Connection connection = DAOUtils.getConnection(DB_NAME)) {
            when(IdentityDatabaseUtil.getDBConnection()).thenReturn(connection);
            ResourceDAO resourceDAO = new ResourceDAO();
            resourceDAO.registerResource(storeResources());
        }
    }

    @Test(expectedExceptions = UMAException.class)
    public void testRetrieveResource() throws Exception {

        mockStatic(IdentityDatabaseUtil.class);
        try (Connection connection = DAOUtils.getConnection(DB_NAME)) {
            when(IdentityDatabaseUtil.getDBConnection()).thenReturn(connection);
            ResourceDAO resourceDAO = new ResourceDAO();
            resourceDAO.retrieveResource("19");
        }
    }

    @Test
    public void testRetrieveResourceIDs() throws Exception {

        mockStatic(IdentityDatabaseUtil.class);
        try (Connection connection = DAOUtils.getConnection(DB_NAME)) {
            when(IdentityDatabaseUtil.getDBConnection()).thenReturn(connection);
            ResourceDAO resourceDAO = new ResourceDAO();
            resourceDAO.retrieveResourceIDs("7483");
        }
    }

    @Test
    public void testDeleteResource() throws Exception {

        mockStatic(IdentityDatabaseUtil.class);
        try (Connection connection = DAOUtils.getConnection(DB_NAME)) {
            when(IdentityDatabaseUtil.getDBConnection()).thenReturn(connection);
            ResourceDAO resourceDAO = new ResourceDAO();
            resourceDAO.deleteResource("19");
        }
    }

    @Test
    public void testUpdateResource() throws Exception {

        mockStatic(IdentityDatabaseUtil.class);
        try (Connection connection = DAOUtils.getConnection(DB_NAME)) {
            when(IdentityDatabaseUtil.getDBConnection()).thenReturn(connection);
            ResourceDAO resourceDAO = new ResourceDAO();
            resourceDAO.updateResource("19", storeResources());
        }
    }

    private Resource storeResources() {

        Resource resource = new Resource();
        resource.setResourceId("190292");
        resource.setName("photo_albem");
        resource.setDescription("Collection of digital photographs");
        resource.setIconUri("http://www.example.com/icons/sky.png");
        resource.setType("http://www.example.com/rsrcs/photoalbum");
        return resource;
    }

    private void addScopes(ResourceDAO resourceDAO, List<Object> resources) throws SQLException,
            UMAServiceException {

        for (Object resource : resources) {
            try (Connection connection = DAOUtils.getConnection(DB_NAME)) {
                when(IdentityDatabaseUtil.getDBConnection()).thenReturn(connection);
                resourceDAO.registerResource((Resource) resource);

            }
        }
    }
}
