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

package br.com.transpetro.caprovider;

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
public class TestCA4UserStorageProviderFactory {

    public static final String PARAM_2 = "PARAM_2";
    public static final String USER = "USER";

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void testStatic() {
        CA4UserStorageProviderFactory testCA4UserStorageProviderFactory = new CA4UserStorageProviderFactory();
        Assert.assertEquals(PARAM_2,testCA4UserStorageProviderFactory.configMetadata.get(1).getLabel());
        Assert.assertEquals(testCA4UserStorageProviderFactory.configMetadata,testCA4UserStorageProviderFactory.getConfigProperties());
        Assert.assertEquals(testCA4UserStorageProviderFactory.PROVIDER_NAME,testCA4UserStorageProviderFactory.getId());
        try {
            testCA4UserStorageProviderFactory.init(null);
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

        CA4UserStorageProviderFactory testCA4UserStorageProviderFactory = new CA4UserStorageProviderFactory();

        Assert.assertEquals(testCA4UserStorageProviderFactory.create(session,model).model,model);

    }


    private ComponentModel getKeycloakModel() {
        return new ComponentModel();
    }
}
