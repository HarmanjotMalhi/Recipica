package com.example.recipica.Listeners;

import com.example.recipica.Models.InstructionsResponse;

import java.util.List;

public interface InstructionsListener {
        void didFetch(List<InstructionsResponse> response, String message );
        void didError(String message);
    }
