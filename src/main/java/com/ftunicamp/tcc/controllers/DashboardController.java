package com.ftunicamp.tcc.controllers;

import com.ftunicamp.tcc.controllers.response.DashboardResponse;
import com.ftunicamp.tcc.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/dados")
    public ResponseEntity<DashboardResponse> getDadosDashboard() {
        return ResponseEntity.ok(dashboardService.popularDashboard());
    }
}
