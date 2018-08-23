package org.zkoss.mvvm.syntax;

import org.zkoss.bind.annotation.*;

public class ScopeParamVM {
    private String sysConfig;
    private String userCredential;

    @Init
    public void init(
            @ScopeParam(scopes = Scope.APPLICATION, value = "config") String sysConfig,
            @ScopeParam(scopes = Scope.SESSION, value = "user") String userCredential) {
        this.sysConfig = sysConfig;
        this.userCredential = userCredential;
    }

    public String getSysConfig() {
        return sysConfig;
    }

    public String getUserCredential() {
        return userCredential;
    }
}
