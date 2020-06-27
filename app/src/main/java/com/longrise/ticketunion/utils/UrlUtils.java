package com.longrise.ticketunion.utils;

public class UrlUtils {

    public static String createHomePagerUrl(int materialId, int page) {
        return "discovery/" + materialId + "/" + page;
    }

    public static String getPhotoPath(String pathUrl) {
        if (pathUrl.startsWith("http") || pathUrl.startsWith("https")) {
            return pathUrl;
        }
        return "https:" + pathUrl;
    }

    public static String getSizePhotoPath(String pathUrl, int size) {
        if (pathUrl.startsWith("http") || pathUrl.startsWith("https")) {
            return pathUrl + "_" + size + "x" + size + ".jpg";
        }
        return "https:" + pathUrl + "_" + size + "x" + size + ".jpg";
    }

    public static String getTicketUrl(String ticketUrl) {
        if (ticketUrl.startsWith("http") || ticketUrl.startsWith("https")) {
            return ticketUrl;
        }
        return "https:" + ticketUrl;
    }

    public static String getSelectedPageContentUrl(int categoryId) {
        return "recommend/" + categoryId;
    }

    public static String getSellContentUrl(int page) {
        return "onSell/" + page;
    }
}
