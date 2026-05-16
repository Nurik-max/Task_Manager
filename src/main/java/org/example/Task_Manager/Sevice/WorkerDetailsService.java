package org.example.Task_Manager.Sevice;

import org.example.Task_Manager.Model.Worker;
import org.example.Task_Manager.Repoitory.WorkerRepository;
import org.example.Task_Manager.details.WorkerDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class WorkerDetailsService implements UserDetailsService {

    @Autowired
    private WorkerRepository workerRepository;

    public WorkerDetailsService(WorkerRepository workerRepository) {
        this.workerRepository = workerRepository;
    }

//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//
//            Worker worker = workerRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("User not found"));
//
//
//        return org.springframework.security.core.userdetails.User
//                .builder()
//                .username(worker.getUsername())
//                .password(worker.getPassword())
//                .roles("USER")
//                .build();
//    }
    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        Worker worker = workerRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new WorkerDetails(worker);
    }
}
