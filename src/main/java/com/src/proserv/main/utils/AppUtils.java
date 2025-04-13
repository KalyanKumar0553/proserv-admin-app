package com.src.proserv.main.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.src.proserv.main.response.dto.JSONResponseDTO;

public class AppUtils {

	@SuppressWarnings("unchecked")
	public static  <T> JSONResponseDTO<T> getJSONObject(T data) {
		return (JSONResponseDTO<T>) JSONResponseDTO.builder().statusMsg(data).isError(false).build();
	}

	public static List<String> generateRandomUUID() {
        Set<String> sampleUUID = new HashSet<>();
        int i=0;
        do {
        	sampleUUID.add(UUID.randomUUID().toString());
        	i++;
        } while (i<20);
        return new ArrayList<String>(sampleUUID);
    }

	public static String formatDurationString(String input) {
        long totalSeconds = parseToSeconds(input);
        return formatSecondsToHMString(totalSeconds);
    }

    private static long parseToSeconds(String input) {
        input = input.trim().toLowerCase();
        long seconds = 0;

        String[] parts = input.split("\\s+");
        for (int i = 0; i < parts.length - 1; i += 2) {
            int value = Integer.parseInt(parts[i]);
            String unit = parts[i + 1];
            if (unit.startsWith("hour")) {
                seconds += value * 3600;
            } else if (unit.startsWith("minute")) {
                seconds += value * 60;
            } else if (unit.startsWith("second")) {
                seconds += value;
            }
        }
        return seconds;
    }

    public static String formatSecondsToHMString(long totalSeconds) {
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;

        StringBuilder result = new StringBuilder();

        if (hours > 0) {
            result.append(hours).append(" Hour").append(hours > 1 ? "s" : "");
        }

        if (minutes > 0) {
            if (result.length() > 0) result.append(" ");
            result.append(minutes).append(" Minute").append(minutes > 1 ? "s" : "");
        }

        if (seconds > 0 || result.length() == 0) { // if everything else is 0
            if (result.length() > 0) result.append(" ");
            result.append(seconds).append(" Second").append(seconds > 1 ? "s" : "");
        }

        return result.toString();
    }

}
