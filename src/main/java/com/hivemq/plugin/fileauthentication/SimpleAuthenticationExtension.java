/*
 * Copyright 2015 dc-square GmbH
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.hivemq.plugin.fileauthentication;

import com.hivemq.extension.sdk.api.ExtensionMain;
import com.hivemq.extension.sdk.api.annotations.NotNull;
import com.hivemq.extension.sdk.api.parameter.ExtensionStartInput;
import com.hivemq.extension.sdk.api.parameter.ExtensionStartOutput;
import com.hivemq.extension.sdk.api.parameter.ExtensionStopInput;
import com.hivemq.extension.sdk.api.parameter.ExtensionStopOutput;
import com.hivemq.extension.sdk.api.services.Services;
import com.hivemq.plugin.fileauthentication.authentication.SimpleAuthProvider;
import com.hivemq.plugin.fileauthentication.configuration.ConfigurationReader;
import com.hivemq.spi.plugin.meta.Information;

/**
 * Plugin Configuration Class
 *
 * @author Christian Goetz
 */
@Information(name = "Simple Authentication Plugin", version = "3.1.1", author = "dc-square GmbH")
public class SimpleAuthenticationExtension implements ExtensionMain {

    private SimpleAuthProvider provider;

    @Override
    public void extensionStart(@NotNull ExtensionStartInput extensionStartInput, @NotNull ExtensionStartOutput extensionStartOutput) {
        provider = new SimpleAuthProvider(new ConfigurationReader(extensionStartInput.getExtensionInformation()));
        Services.securityRegistry().setAuthorizerProvider(provider);
        Services.securityRegistry().setAuthenticatorProvider(provider);
        Services.securityRegistry().setEnhancedAuthenticatorProvider(provider);
    }

    @Override
    public void extensionStop(@NotNull ExtensionStopInput extensionStopInput, @NotNull ExtensionStopOutput extensionStopOutput) {
        provider = null;
    }
}
