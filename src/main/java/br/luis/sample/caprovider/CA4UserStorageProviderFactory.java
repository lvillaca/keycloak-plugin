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

package br.luis.sample.caprovider;

import org.apache.log4j.Logger;
import org.keycloak.Config;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.UserModel;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.provider.ProviderConfigurationBuilder;
import org.keycloak.storage.UserStorageProviderFactory;

import java.util.*;

/**
 * @author Luis Villaca
 * @version $Revision: 1 $
 */
public class CA4UserStorageProviderFactory implements UserStorageProviderFactory<CA4UserStorageProvider> {
    private static Logger classLogger = Logger.getLogger(CA4UserStorageProviderFactory.class);
    public static final String PROVIDER_NAME = "CA4SSOProvider";
    

    protected ComponentModel currentModel;


    public static final String PARAM_1 = "PARAM_1";
    public static final String PARAM_2 = "PARAM_2";


    protected static List<ProviderConfigProperty> configMetadata = null;

    public static final String ENDPOINT_WSDL = "endpoint_wsdl";

    static {
        //metadados referentes a tela de configuracao Keycloak
        configMetadata = ProviderConfigurationBuilder.create()
                .property().name(PARAM_1)
                .type(ProviderConfigProperty.STRING_TYPE)
                .label(PARAM_1)
                .defaultValue(PARAM_1)
                .helpText("String Parameter 1")
                .add()
                .property().name(PARAM_2)
                .type(ProviderConfigProperty.STRING_TYPE)
                .label(PARAM_2)
                .defaultValue(PARAM_2)
                .helpText("Confidential Parameter 2")
                .add()
                .build();
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return configMetadata;
    }


    @Override
    public String getId() {
        return PROVIDER_NAME;
    }


    @Override
    public void init(Config.Scope config) {
        classLogger.warn("==============Init factory=====================");
    }



    @Override
    public CA4UserStorageProvider create(KeycloakSession session, ComponentModel model) {
        //retrieve and pass on console information

        Properties properties = new Properties();
        properties.setProperty(PARAM_2,model.getConfig().getFirst(PARAM_2));
        properties.setProperty(PARAM_1,model.getConfig().getFirst(PARAM_1));

        return new CA4UserStorageProvider(session, model, properties);
    }

}
