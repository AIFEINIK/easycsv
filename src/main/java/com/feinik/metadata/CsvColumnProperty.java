package com.feinik.metadata;

/**
 * @author Feinik
 * @discription
 * @date 2019/7/10
 * @since 1.0.0
 */
public class CsvColumnProperty implements Comparable<CsvColumnProperty> {

    private String content;

    private String head;

    private int index = 999;

    private String format;

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    @Override
    public int compareTo(CsvColumnProperty o) {
        int x = this.index;
        int y = o.getIndex();
        return (x < y) ? -1 : ((x == y) ? 0 : 1);
    }
}
