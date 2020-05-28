package com.server.demeter.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;


@Document
public class Role implements GrantedAuthority {
    private static final long serialVersionUID = 1L;
    @Id
    private String id;
    private String name;

    public Role() { }

    public Role(final String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public String getAuthority() {
        return name;
    }
}
