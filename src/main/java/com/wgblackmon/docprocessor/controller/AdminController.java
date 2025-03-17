package com.wgblackmon.docprocessor.controller;

import com.wgblackmon.docprocessor.service.ChromaDBService;
import com.wgblackmon.docprocessor.model.DocumentChunk;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * AdminController
 * 
 * Controller handling administrative operations for the document processing system.
 * Provides secured endpoints for system management and monitoring.
 *
 * Key responsibilities:
 * - Handle administrative operations
 * - Manage document visualization
 * - Process system monitoring requests
 * - Control access to admin features
 *
 * @author William Blackmon
 * @version 1.0
 * @since 2024-03-01
 */
@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private static final Logger logger = LogManager.getLogger(AdminController.class);
    
    private final ChromaDBService chromaDBService;

    @Autowired
    public AdminController(ChromaDBService chromaDBService) {
        this.chromaDBService = chromaDBService;
        logger.info("Initializing AdminController");
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        logger.debug("Accessing admin dashboard");
        try {
            model.addAttribute("documents", chromaDBService.getAllDocuments());
            logger.info("Successfully loaded admin dashboard");
            return "admin/dashboard";
        } catch (Exception e) {
            logger.error("Error loading admin dashboard", e);
            model.addAttribute("error", "Failed to load dashboard");
            return "admin/error";
        }
    }
}