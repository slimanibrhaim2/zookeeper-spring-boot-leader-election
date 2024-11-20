package com.example.leader_election;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LeaderElectionConfig {

    private final RoleService roleService;

    public LeaderElectionConfig(RoleService roleService) {
        this.roleService = roleService;
    }

    @Bean(initMethod = "start")
    public LeaderSelector leaderSelector(CuratorFramework curatorFramework) {
        String leaderPath = "/leader-election";
        LeaderSelector leaderSelector = new LeaderSelector(curatorFramework, leaderPath, new LeaderSelectorListenerAdapter() {
            @Override
            public void takeLeadership(CuratorFramework client) throws Exception {
                roleService.setRole("leader");
                System.out.println("I am the leader now!");
                try {
                    Thread.sleep(Long.MAX_VALUE); // Keep leadership indefinitely
                } catch (InterruptedException e) {
                    System.out.println("Leadership interrupted");
                    roleService.setRole("worker");
                }
            }
        });
        leaderSelector.autoRequeue(); // Ensure the instance rejoins the election after losing leadership
        return leaderSelector;
    }
}
