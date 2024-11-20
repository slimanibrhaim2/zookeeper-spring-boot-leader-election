package com.example.leader_election;

import org.springframework.stereotype.Service;

@Service
public class RoleService {
    private volatile String role = "worker";

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
