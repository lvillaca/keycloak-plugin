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


import org.apache.log4j.Logger;
import org.keycloak.component.ComponentModel;
import org.keycloak.credential.CredentialInput;
import org.keycloak.credential.CredentialInputValidator;
import org.keycloak.credential.CredentialModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserCredentialModel;
import org.keycloak.models.UserModel;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.UserStorageProvider;
import org.keycloak.storage.adapter.AbstractUserAdapterFederatedStorage;
import org.keycloak.storage.user.UserLookupProvider;

import java.util.*;


/**
 * @author Luis Villaca
 * @version $Revision: 1 $
 */
public class AUserStorageProvider implements
        UserStorageProvider,
        UserLookupProvider,
        CredentialInputValidator
{
    private static Logger classLogger = Logger.getLogger(AUserStorageProvider.class);
    protected KeycloakSession session;
    protected ComponentModel model;
    protected Properties properties;
    // map of loaded users in this transaction
    protected Map<String, UserModel> loadedUsers = new HashMap<>();

    public AUserStorageProvider(KeycloakSession session, ComponentModel model, Properties properties) {
        this.session = session;
        this.model = model;
        this.properties = properties;
    }


    @Override
    public UserModel getUserByUsername(String username, RealmModel realm) {
        UserModel adapter = loadedUsers.get(username);

        if (adapter == null) {
            adapter = createAdapter(realm, username);
            finalizeUsr(adapter); // enrich with attributes
            loadedUsers.put(username, adapter); //keep on session cache
        }
        return adapter;
    }

    /**
     * Create UserModel instance
     * @param realm - Keycloak realm
     * @param username - user identifier
     * @return UserModel instance
     */
    protected UserModel createAdapter(RealmModel realm, String username) {
        return new AbstractUserAdapterFederatedStorage(session, realm, model) {
            @Override
            public String getUsername() {
                return username;
            }
            @Override
            public void setUsername(String s) {//never changes
            }
            @Override
            public boolean isEnabled() {//always true
                return true;
            }
        };
    }

    @Override
    public UserModel getUserById(String id, RealmModel realm) {
        StorageId storageId = new StorageId(id);
        String username = storageId.getExternalId();
        return getUserByUsername(username, realm);
    }

    @Override
    public UserModel getUserByEmail(String email, RealmModel realm) {
        return null; // not applicable
    }


    // CredentialInputValidator methods
    @Override
    public boolean isConfiguredFor(RealmModel realm, UserModel user, String credentialType) {
        return supportsCredentialType(credentialType);
    }

    @Override
    public boolean supportsCredentialType(String credentialType) {
        return credentialType.equals(CredentialModel.PASSWORD);
    }

    @Override
    public boolean isValid(RealmModel realm, UserModel user, CredentialInput input) {
        if (!supportsCredentialType(input.getType()) || !(input instanceof UserCredentialModel)) return false;

        try {
            if (Math.random()>0.99) throw new Exception();
            // Authenticate user by whatever means

        } catch (Exception e) {
            classLogger.warn("Invalid attempt: "+e.getMessage()); //exception - ja logado
            return false;
        }
        classLogger.warn("authenticated:"+user.getUsername());
        return true;
    }

     @Override
     public void close() {
        classLogger.warn("wrapping up auth");
     }

    /**
     * Enrich user model
     * @param user - instance to be enriched
     */
     public void finalizeUsr(UserModel user) {

         //e.g. pull from DB and enrich
         //Map<String,  ArrayList<String>> dbAttributes = pullAttributesFromDB(user.getUsername());

         /*try {
                dbAttributes.keySet().forEach(key ->
                         user.setSingleAttribute(key,dbAttributes.get(key).get(0))
             );
         } catch (Exception e) {
             classLogger.error("getAttributes exception on user "+user+ " : "+e.getMessage());
         }*/
     }



}
