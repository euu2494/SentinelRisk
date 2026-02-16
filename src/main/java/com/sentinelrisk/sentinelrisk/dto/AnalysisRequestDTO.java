package com.sentinelrisk.sentinelrisk.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class AnalysisRequestDTO {

    @NotBlank(message = "Risk description is mandatory")
    @Size(min = 20, max = 1000, message = "Description must be between 20 and 1000 characters")
    private String riskDescription;

    @NotBlank(message = "Risk category is required")
    private String category;

    @NotBlank(message = "Priority is required")
    @Pattern(regexp = "LOW|MEDIUM|HIGH|CRITICAL", message = "Priority must be: LOW, MEDIUM, HIGH, or CRITICAL")
    private String priority;

    // Getters and Setters
    public String getRiskDescription() { return riskDescription; }
    public void setRiskDescription(String riskDescription) { this.riskDescription = riskDescription; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
}