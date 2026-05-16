package org.example.Task_Manager.details;

import org.example.Task_Manager.Model.Worker;
import org.example.Task_Manager.Model.WorkerStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class WorkerDetails implements UserDetails {

    private final Worker worker;

    public WorkerDetails(Worker worker) {
        this.worker = worker;
    }

    public Worker getWorker() {
        return worker;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + worker.getUserRole().name()));
    }


    @Override
    public String getPassword() {
        return worker.getPassword();
    }

    @Override
    public String getUsername() {
        return worker.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return worker.getWorkerStatus() != WorkerStatus.FIRED;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return worker.getWorkerStatus() == WorkerStatus.WORKS;
    }
}
