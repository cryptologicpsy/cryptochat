package com.example.crypto.repositories;

import com.example.crypto.models.SignalSession;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SignalSessionRepository extends CrudRepository<SignalSession, String> {
    
    Optional<SignalSession> findByLocalUserIdAndRemoteUserIdAndDeviceId(
            String localUserId, String remoteUserId, int deviceId);
    
    Iterable<SignalSession> findByLocalUserId(String localUserId);
}