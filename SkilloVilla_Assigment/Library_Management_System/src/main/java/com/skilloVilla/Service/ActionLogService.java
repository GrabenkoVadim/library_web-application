package com.skilloVilla.Service;

import com.skilloVilla.Dto.ActionLogDto;
import com.skilloVilla.Entity.ActionLog;
import com.skilloVilla.Entity.AppUser;
import com.skilloVilla.Repository.ActionLogRepository;
import com.skilloVilla.Repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActionLogService {

    private final ActionLogRepository actionLogRepository;
    private final AppUserRepository appUserRepository;

    public void log(String action, String entityType, Integer entityId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        AppUser user = null;
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
            user = appUserRepository.findByUsername(auth.getName()).orElse(null);
        }

        ActionLog log = new ActionLog();
        log.setUser(user);
        log.setAction(action);
        log.setEntityType(entityType);
        log.setEntityId(entityId);
        log.setTimestamp(LocalDateTime.now());

        actionLogRepository.save(log);
    }

    public List<ActionLogDto> getAll() {
        return actionLogRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    public List<ActionLogDto> getLastLogs(int limit) {
        return actionLogRepository.findTop50ByOrderByTimestampDesc().stream()
                .map(this::toDto)
                .toList();
    }

    private ActionLogDto toDto(ActionLog log) {
        ActionLogDto dto = new ActionLogDto();

        dto.setId(log.getId() != null ? log.getId().intValue() : null);

        dto.setCreatedAt(log.getTimestamp());

        dto.setAction(log.getAction());
        dto.setEntityType(log.getEntityType());
        dto.setEntityId(log.getEntityId());

        dto.setUsername(
                log.getUser() != null ? log.getUser().getUsername() : null
        );

        return dto;
    }

}
