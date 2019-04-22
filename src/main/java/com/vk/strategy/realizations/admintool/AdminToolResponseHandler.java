package com.vk.strategy.realizations.admintool;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import java.io.IOException;

public interface AdminToolResponseHandler {

    void handle() throws ClientException, IOException, ApiException, InterruptedException;
}
