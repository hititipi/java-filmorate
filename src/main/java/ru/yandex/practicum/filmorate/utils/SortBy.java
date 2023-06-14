package ru.yandex.practicum.filmorate.utils;

public enum SortBy {
    YEAR("year"),
    LIKES("likes");

    private final String sortBy;

    SortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getSortBy() {
        return sortBy;
    }

    public static SortBy getSortBy(String sortBy) {
        for (SortBy elem : values()) {
            if (elem.getSortBy().equals(sortBy)) {
                return elem;
            }
        }
        throw new IllegalArgumentException("No enum found with url: [" + sortBy + "]");
    }
}
