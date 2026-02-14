package com.harborspace.spacecraft.sphere.engine.response;

import java.util.List;

public class ListResponse<T> {
    List<T> list;
    private Boolean hasMore;

    public ListResponse() {
    }

    public ListResponse(List<T> list, Boolean hasMore) {
        this.list = list;
        this.hasMore = hasMore;
    }

    public static <T> ListResponse<T> empty() {
        return new ListResponse(List.of(), false);
    }

    public static <T> ListResponse<T> fromListUsingLimit(List<T> list, int requiredLimit) {
        return fromListUsingOffsetLimit(list, 0, requiredLimit);
    }

    public static <T> ListResponse<T> fromListUsingOffsetLimit(List<T> list, int offset, int limit) {
        boolean hasNext = list.size() > offset + limit;
        int fromIndex = Math.min(list.size(), offset);
        int toIndex = Math.min(offset + limit, list.size());
        return new ListResponse(list.subList(fromIndex, toIndex), hasNext);
    }

    public static <T> ListResponse<T> notPageable(List<T> list) {
        return new ListResponse(list, (Boolean) null);
    }

    public List<T> getList() {
        return this.list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public Boolean getHasMore() {
        return this.hasMore;
    }

    public void setHasMore(Boolean hasMore) {
        this.hasMore = hasMore;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof ListResponse)) {
            return false;
        } else {
            ListResponse<?> other = (ListResponse) o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object this$list = this.getList();
                Object other$list = other.getList();
                if (this$list == null) {
                    if (other$list != null) {
                        return false;
                    }
                } else if (!this$list.equals(other$list)) {
                    return false;
                }

                Object this$hasMore = this.getHasMore();
                Object other$hasMore = other.getHasMore();
                if (this$hasMore == null) {
                    if (other$hasMore != null) {
                        return false;
                    }
                } else if (!this$hasMore.equals(other$hasMore)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof ListResponse;
    }

    public int hashCode() {
        int result = 1;
        Object $list = this.getList();
        result = result * 59 + ($list == null ? 43 : $list.hashCode());
        Object $hasMore = this.getHasMore();
        result = result * 59 + ($hasMore == null ? 43 : $hasMore.hashCode());
        return result;
    }

    public String toString() {
        List var10000 = this.getList();
        return "ListResponse(list=" + var10000 + ", hasMore=" + this.getHasMore() + ")";
    }
}
