/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.luis.sample.provider;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.keycloak.component.ComponentModel;
import org.keycloak.credential.CredentialInput;
import org.keycloak.credential.CredentialModel;
import org.keycloak.models.*;
import org.keycloak.storage.adapter.AbstractUserAdapterFederatedStorage;
import org.mockito.Mockito;

import java.util.*;

import static br.luis.sample.provider.AUserStorageProviderFactory.*;
import static org.mockito.Mockito.verify;


/**
 * @author Luis Villaca
 * @version $Revision: 1 $
 */
public class TestAUserStorageProvider
{
    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    public static final String USERNAME = "USERNAME";

    @Test
    public void testInitAUserStorageProvider() {
        KeycloakSession session = Mockito.mock(KeycloakSession.class);
        ComponentModel model =  Mockito.mock(ComponentModel.class);
        Properties props = Mockito.mock(Properties.class);

        AUserStorageProvider testAUserStorageProvider = new AUserStorageProvider(session, model, props);
        Assert.assertEquals(testAUserStorageProvider.model, model);
        Assert.assertEquals(testAUserStorageProvider.session, session);
    }


    // UserLookupProvider methods

    @Test
    public void testUserByUsername() {
        KeycloakSession session = Mockito.mock(KeycloakSession.class);
        RealmModel realmModel = Mockito.mock(RealmModel.class);
        ComponentModel componentModel =  Mockito.mock(ComponentModel.class);
        Properties props = Mockito.mock(Properties.class);
        AUserStorageProvider testAUserStorageProvider = new AUserStorageProvider(session, componentModel, props);
        AUserStorageProviderFactory testAUserStorageProviderFactory = new AUserStorageProviderFactory();
        testAUserStorageProviderFactory.init(null);
        testAUserStorageProviderFactory.currentModel = getKeycloakModel();

        Assert.assertEquals(USERNAME,testAUserStorageProvider.getUserByUsername(USERNAME,realmModel).getUsername());
    }

    @Test
    public void testGetUserById() {
        KeycloakSession session = Mockito.mock(KeycloakSession.class);
        ComponentModel model =  Mockito.mock(ComponentModel.class);
        RealmModel realmModel = Mockito.mock(RealmModel.class);
        Properties props = Mockito.mock(Properties.class);

        AUserStorageProvider testAUserStorageProvider = new AUserStorageProvider(session, model, props);
        AUserStorageProviderFactory testAUserStorageProviderFactory = new AUserStorageProviderFactory();
        testAUserStorageProviderFactory.init(null);
        testAUserStorageProviderFactory.currentModel = getKeycloakModel();

        Assert.assertEquals(testAUserStorageProvider.getUserById(USERNAME+":"+USERNAME,realmModel).getUsername(),USERNAME+":"+USERNAME);
    }

    @Test
    public void testGetUserByEmail() {
        KeycloakSession session = Mockito.mock(KeycloakSession.class);
        ComponentModel model =  Mockito.mock(ComponentModel.class);
        RealmModel realmModel = Mockito.mock(RealmModel.class);
        Properties props = Mockito.mock(Properties.class);

        AUserStorageProvider testAUserStorageProvider = new AUserStorageProvider(session, model, props);
        Assert.assertNull(testAUserStorageProvider.getUserByEmail(USERNAME,realmModel));
    }


    @Test
    public void testIsConfiguredFor() {
        UserModel umodel =  Mockito.mock(UserModel.class);
        KeycloakSession session = Mockito.mock(KeycloakSession.class);
        ComponentModel model =  Mockito.mock(ComponentModel.class);
        RealmModel realmModel = Mockito.mock(RealmModel.class);
        Properties props = Mockito.mock(Properties.class);

        AUserStorageProvider testAUserStorageProvider = new AUserStorageProvider(session, model, props);
        Assert.assertTrue(testAUserStorageProvider.isConfiguredFor(realmModel, umodel, CredentialModel.PASSWORD));
    }

    @Test
    public void testSupportsCredentialType() {
        KeycloakSession session = Mockito.mock(KeycloakSession.class);
        ComponentModel model =  Mockito.mock(ComponentModel.class);
        Properties props = Mockito.mock(Properties.class);

        AUserStorageProvider testAUserStorageProvider = new AUserStorageProvider(session, model, props);
        Assert.assertTrue(testAUserStorageProvider.supportsCredentialType(CredentialModel.PASSWORD));
    }

    @Test
    public void testIsValid() {
        UserModel umodel =  Mockito.mock(UserModel.class);
        Mockito.when(umodel.getUsername()).thenReturn(USERNAME);

        CredentialInput input = new UserCredentialModel() {
            @Override
            public String getType() {
                return CredentialModel.PASSWORD;
            }
        };

        KeycloakSession session = Mockito.mock(KeycloakSession.class);
        KeycloakContext context = Mockito.mock(KeycloakContext.class);
        ClientModel climodel =  Mockito.mock(ClientModel.class);
        Mockito.when(session.getContext()).thenReturn(context);
        Mockito.when(context.getClient()).thenReturn(climodel);
        Mockito.when(climodel.getClientId()).thenReturn(USERNAME);

        ComponentModel model =  Mockito.mock(ComponentModel.class);
        Properties props = Mockito.mock(Properties.class);
        RealmModel realmModel = Mockito.mock(RealmModel.class);

        AUserStorageProvider testAUserStorageProvider = new AUserStorageProvider(session, model, props);

        Assert.assertEquals(true,testAUserStorageProvider.isValid(realmModel,umodel,input));
    }

    @Test
    public void testClose() {
        AUserStorageProvider testAUserStorageProvider = new AUserStorageProvider(null, null, null);
        try {
            testAUserStorageProvider.close();
        } catch (Exception e) {
            Assert.fail("No exception should be raised");
        }
    }


    private ComponentModel getKeycloakModel() {
        return new ComponentModel();
    }

}
