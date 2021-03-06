package com.bslota.optimisticapi.holding.infrastructure.rest;

import com.bslota.optimisticapi.holding.aggregate.Version;
import com.bslota.optimisticapi.holding.domain.BookId;
import com.bslota.optimisticapi.holding.query.FindingBook;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/books")
class BookFindingController {

    private final FindingBook findingBook;

    public BookFindingController(FindingBook findingBook) {
        this.findingBook = findingBook;
    }

    @GetMapping("/{bookId}")
    ResponseEntity<?> findBookWith(@PathVariable("bookId") UUID bookIdValue) {
        return findingBook
                .by(BookId.of(bookIdValue))
                .map(it -> ResponseEntity.ok().eTag(ETag.of(Version.from(it.getVersion())).getValue()).body(it))
                .orElse(ResponseEntity.notFound().build());
    }
}
