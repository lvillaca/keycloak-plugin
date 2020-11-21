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
import org.keycloak.common.util.MultivaluedHashMap;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.*;
import org.mockito.Mockito;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.core.StringStartsWith.startsWith;

/**
 * @author Luis Villaca
 * @version $Revision: 1 $
 */
public class TestAUserStorageProviderFactory {

    public static final String PARAM_2 = "PARAM_2";
    public static final String USER = "USER";

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void testStatic() {
        AUserStorageProviderFactory testAUserStorageProviderFactory = new AUserStorageProviderFactory();
        Assert.assertEquals(PARAM_2,testAUserStorageProviderFactory.configMetadata.get(1).getLabel());
        Assert.assertEquals(testAUserStorageProviderFactory.configMetadata,testAUserStorageProviderFactory.getConfigProperties());
        Assert.assertEquals(testAUserStorageProviderFactory.PROVIDER_NAME,testAUserStorageProviderFactory.getId());
        try {
            testAUserStorageProviderFactory.init(null);
        } catch (Exception e) {
            Assert.fail("exception on null config");
        }
    }



    @Test
    public void testCreateNoChange()  {
        ComponentModel model =  Mockito.mock(ComponentModel.class);
        KeycloakSession session = Mockito.mock(KeycloakSession.class);
        MultivaluedHashMap multivaluedHashMap = Mockito.mock(MultivaluedHashMap.class);

        Mockito.when(multivaluedHashMap.getFirst(Mockito.anyString())).thenReturn("");
        Mockito.when(model.getConfig()).thenReturn(multivaluedHashMap);

        AUserStorageProviderFactory testAUserStorageProviderFactory = new AUserStorageProviderFactory();

        Assert.assertEquals(testAUserStorageProviderFactory.create(session,model).model,model);

    }


    private ComponentModel getKeycloakModel() {
        return new ComponentModel();
    }
}
