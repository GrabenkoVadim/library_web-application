package com.skilloVilla.Controller;

import com.skilloVilla.Entity.ActionLog;
import com.skilloVilla.Service.ActionLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/logs")
public class ActionLogController {

    private final ActionLogService actionLogService;

    // GET /api/logs  — тільки ADMIN (обмежено в SecurityConfig)
    @GetMapping
    public List<ActionLog> getLogs() {
        return actionLogService.getAll();
    }
}
