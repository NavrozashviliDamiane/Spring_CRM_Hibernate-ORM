package org.damiane.service;

public interface AuthenticateService {

    boolean matchUserCredentials(String username, String password);
}
