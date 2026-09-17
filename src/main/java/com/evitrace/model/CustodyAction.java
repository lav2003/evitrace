package com.evitrace.model;

public enum CustodyAction {
    COLLECTED,      // First entry — evidence gathered at scene
    TRANSFERRED,    // Moved to another officer or location
    EXAMINED,       // Forensic analyst accessed it
    RETURNED,       // Sent back to storage after examination
    SUBMITTED       // Sent to court
}