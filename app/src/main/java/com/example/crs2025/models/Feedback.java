package com.example.crs2025.models;

public class Feedback {
    private String feedbackId;
    private String userId;
    private String name;
    private String email;
    private String satisfaction;
    private String improvementSuggestions;
    private String additionalComments;
    private String recommendation;

    public Feedback() {
        // Default constructor required for Firebase
    }

    public Feedback(String feedbackId, String userId, String name, String email, String satisfaction, String improvementSuggestions, String additionalComments, String recommendation) {
        this.feedbackId = feedbackId;
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.satisfaction = satisfaction;
        this.improvementSuggestions = improvementSuggestions;
        this.additionalComments = additionalComments;
        this.recommendation = recommendation;
    }

    // Getters and Setters

    public String getFeedbackId() { return feedbackId; }
    public void setFeedbackId(String feedbackId) { this.feedbackId = feedbackId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSatisfaction() { return satisfaction; }
    public void setSatisfaction(String satisfaction) { this.satisfaction = satisfaction; }

    public String getImprovementSuggestions() { return improvementSuggestions; }
    public void setImprovementSuggestions(String improvementSuggestions) { this.improvementSuggestions = improvementSuggestions; }

    public String getAdditionalComments() { return additionalComments; }
    public void setAdditionalComments(String additionalComments) { this.additionalComments = additionalComments; }

    public String getRecommendation() { return recommendation; }
    public void setRecommendation(String recommendation) { this.recommendation = recommendation; }
}
