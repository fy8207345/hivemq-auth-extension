package com.hivemq.plugin.fileauthentication.authentication;

import com.hivemq.extension.sdk.api.annotations.NotNull;
import com.hivemq.extension.sdk.api.auth.EnhancedAuthenticator;
import com.hivemq.extension.sdk.api.auth.PublishAuthorizer;
import com.hivemq.extension.sdk.api.auth.SimpleAuthenticator;
import com.hivemq.extension.sdk.api.auth.SubscriptionAuthorizer;
import com.hivemq.extension.sdk.api.auth.parameter.*;
import com.hivemq.extension.sdk.api.packets.connect.ConnectPacket;
import com.hivemq.plugin.fileauthentication.configuration.ConfigurationReader;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.Optional;

public class MySimpleAuthenticator implements SimpleAuthenticator, EnhancedAuthenticator, SubscriptionAuthorizer, PublishAuthorizer {

    private static Logger logger = LoggerFactory.getLogger(MySimpleAuthenticator.class);

    private ConfigurationReader configurationReader;

    public MySimpleAuthenticator(ConfigurationReader configurationReader) {
        this.configurationReader = configurationReader;
    }

    @Override
    public void onConnect(@NotNull EnhancedAuthConnectInput enhancedAuthConnectInput, @NotNull EnhancedAuthOutput enhancedAuthOutput) {
        ConnectPacket connectPacket = enhancedAuthConnectInput.getConnectPacket();
        if(auth(connectPacket)){
            enhancedAuthOutput.authenticateSuccessfully();
        }else {
            enhancedAuthOutput.failAuthentication("Unknown username/password");
        }
    }

    @Override
    public void onAuth(@NotNull EnhancedAuthInput enhancedAuthInput, @NotNull EnhancedAuthOutput enhancedAuthOutput) {
        enhancedAuthOutput.authenticateSuccessfully();
    }

    @Override
    public void onConnect(@NotNull SimpleAuthInput simpleAuthInput, @NotNull SimpleAuthOutput simpleAuthOutput) {
        ConnectPacket connectPacket = simpleAuthInput.getConnectPacket();
        if(auth(connectPacket)){
            simpleAuthOutput.authenticateSuccessfully();
        }else {
            simpleAuthOutput.failAuthentication("Unknown username/password");
        }
    }

    @Override
    public void authorizePublish(@NotNull PublishAuthorizerInput publishAuthorizerInput, @NotNull PublishAuthorizerOutput publishAuthorizerOutput) {
        publishAuthorizerOutput.authorizeSuccessfully();
    }

    @Override
    public void authorizeSubscribe(@NotNull SubscriptionAuthorizerInput subscriptionAuthorizerInput, @NotNull SubscriptionAuthorizerOutput subscriptionAuthorizerOutput) {
        subscriptionAuthorizerOutput.authorizeSuccessfully();
    }

    private boolean auth(ConnectPacket connectPacket){
        Optional<ByteBuffer> optionalByteBuffer = connectPacket.getPassword();
        Optional<String> stringOptional = connectPacket.getUserName();
        if(optionalByteBuffer.isPresent() && stringOptional.isPresent()){
            String userName = stringOptional.get();
            ByteBuffer byteBuffer = optionalByteBuffer.get();
            byte[] bytes = new byte[byteBuffer.capacity()];
            byteBuffer.get(bytes);
            String password = new String(bytes);
            logger.info("connect : {} - {}", userName, password);
            if(StringUtils.isNotEmpty(userName) && StringUtils.isNotEmpty(password)){
                String expectedPassword = configurationReader.get(userName);
                boolean result = password.equals(expectedPassword);
                logger.info("{} - {} : {}", password, expectedPassword, result);
                return result;
            }
        }
        return false;
    }
}
