package com.skilloVilla.Controller;

import com.skilloVilla.Dto.ActionLogDto;
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

    // GET /api/logs?limit=50
    @GetMapping
    public List<ActionLogDto> getLogs(@RequestParam(defaultValue = "50") Integer limit) {
        return actionLogService.getLastLogs(limit);
    }
}

