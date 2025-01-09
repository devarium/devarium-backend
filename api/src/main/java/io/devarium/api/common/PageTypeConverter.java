package io.devarium.api.common;

import java.util.List;
import java.util.function.Function;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

public class PageTypeConverter {

    public static <T, R> Page<R> convert(Page<T> page, Function<T, R> converter) {
        List<R> convertedContent = page.getContent().stream()
            .map(converter)
            .toList();

        return new PageImpl<>(convertedContent, page.getPageable(), page.getTotalElements());
    }
}
