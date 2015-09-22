package monto.service.error;

import monto.service.region.Region;

public class Error extends Region {

    private int offset;
    private int length;
    private String level;
    private String category;
    private String description;

    public Error(int offset, int length, String level, String category, String description) {
        super(offset, length);
        this.offset = offset;
        this.length = length;
        this.level = level;
        this.category = category;
        this.description = description;
    }

    public int getOffset() {
        return offset;
    }

    @Override
    public int getLength() {
        return length;
    }

    public String getCategory() {
        return category;
    }

    public String getLevel() {
        return level;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return String.format("%s", description);
    }
}
