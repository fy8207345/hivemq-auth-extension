package com.hivemq.plugin.fileauthentication.authentication;

import com.hivemq.extension.sdk.api.annotations.NotNull;
import com.hivemq.extension.sdk.api.annotations.Nullable;
import com.hivemq.extension.sdk.api.auth.Authenticator;
import com.hivemq.extension.sdk.api.auth.Authorizer;
import com.hivemq.extension.sdk.api.auth.EnhancedAuthenticator;
import com.hivemq.extension.sdk.api.auth.parameter.AuthenticatorProviderInput;
import com.hivemq.extension.sdk.api.auth.parameter.AuthorizerProviderInput;
import com.hivemq.extension.sdk.api.services.auth.provider.AuthenticatorProvider;
import com.hivemq.extension.sdk.api.services.auth.provider.AuthorizerProvider;
import com.hivemq.extension.sdk.api.services.auth.provider.EnhancedAuthenticatorProvider;
import com.hivemq.plugin.fileauthentication.configuration.ConfigurationReader;

public class SimpleAuthProvider implements AuthenticatorProvider, EnhancedAuthenticatorProvider, AuthorizerProvider {

    private MySimpleAuthenticator mySimpleAuthenticator;

    public SimpleAuthProvider(ConfigurationReader configurationReader){
        this.mySimpleAuthenticator = new MySimpleAuthenticator(configurationReader);
    }

    @Override
    public @Nullable Authenticator getAuthenticator(@NotNull AuthenticatorProviderInput authenticatorProviderInput) {
        return mySimpleAuthenticator;
    }

    @Override
    public @Nullable EnhancedAuthenticator getEnhancedAuthenticator(@NotNull AuthenticatorProviderInput authenticatorProviderInput) {
        return mySimpleAuthenticator;
    }

    @Override
    public @Nullable Authorizer getAuthorizer(@NotNull AuthorizerProviderInput authorizerProviderInput) {
        return mySimpleAuthenticator;
    }
}
