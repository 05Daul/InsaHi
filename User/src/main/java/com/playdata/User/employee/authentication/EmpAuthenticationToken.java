package com.playdata.User.employee.authentication;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;

public class EmpAuthenticationToken extends UsernamePasswordAuthenticationToken {
    private final String companyCode;

    public EmpAuthenticationToken(Object principal, Object credentials, String companyCode) {
        super(principal, credentials);
        this.companyCode = companyCode;
    }

    public EmpAuthenticationToken(Object principal, Object credentials,
                                  String companyCode, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
        this.companyCode = companyCode;
    }

    public String getCompanyCode() {
        return companyCode;
    }
}

