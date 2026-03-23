package mg.mbds.webservice.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

@Service
public class TokenService { 
    private Set<String> tokens = new HashSet<>(); 

    public void add(String token){ 
        tokens.add(token); 
    } 

    public boolean isValid(String token){ 
        return tokens.contains(token); 
    } 
} 