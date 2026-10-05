package com.roslate.littlebabel.domain;

import jakarta.persistence.*;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A book in the user's personal catalog.
 * <p>
 * Deliberately thin: it caches only what's needed to render a list/grid view
 * of the shelf and to survive the external metadata source being unreachable
 * (title, authors, cover, year, page count), plus the user's own data
 * (status, rating, dates, notes). Everything else — description, ISBNs,
 * subjects, edition-specific detail — is fetched live from
 * {@link #externalKey} when a book's detail view is opened, rather than
 * duplicated here.
 */
@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String title;

    @ManyToMany
    @JoinTable(
            name = "book_author",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private Set<Author> authors = new HashSet<>();

    /**
     * Open Library work key (e.g. {@code "/works/OL59863W"}). The reconnect
     * point for re-fetching anything not cached on this entity — description,
     * subjects, ISBNs, etc.
     * <p>
     * Unique, so the same work can't be on the shelf twice. Books added by hand,
     * without a key, are allowed; several of them can have no key at all.
     */
    @Column(unique = true)
    private String externalKey;

    /**
     * Open Library cover id ({@code cover_i}). Build the actual image URL as
     * needed: {@code https://covers.openlibrary.org/b/id/{coverId}-{S|M|L}.jpg}.
     * Kept as an id rather than a baked URL so callers can choose size per
     * context (grid thumbnail vs. detail hero).
     */
    private Integer coverId;

    private Integer publishedYear;

    private Integer pageCount;

    @Enumerated(EnumType.STRING)
    private ReadingStatus status = ReadingStatus.WANT_TO_READ;

    /**
     * User's own rating, 1.0–5.0 in increments of one decimal place (e.g. 3.4).
     * Null until rated.
     */
    @DecimalMin("1.0")
    @DecimalMax("5.0")
    @Digits(integer = 1, fraction = 1)
    private Double rating;

    private LocalDate startedDate;

    private LocalDate finishedDate;

    @Column(length = 2000)
    private String notes;

    protected Book() {
        // required by JPA
    }

    public Book(String title) {
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    public String getExternalKey() {
        return externalKey;
    }

    public void setExternalKey(String externalKey) {
        this.externalKey = externalKey;
    }

    public Integer getCoverId() {
        return coverId;
    }

    public void setCoverId(Integer coverId) {
        this.coverId = coverId;
    }

    public Integer getPublishedYear() {
        return publishedYear;
    }

    public void setPublishedYear(Integer publishedYear) {
        this.publishedYear = publishedYear;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public ReadingStatus getStatus() {
        return status;
    }

    public void setStatus(ReadingStatus status) {
        this.status = status;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public LocalDate getStartedDate() {
        return startedDate;
    }

    public void setStartedDate(LocalDate startedDate) {
        this.startedDate = startedDate;
    }

    public LocalDate getFinishedDate() {
        return finishedDate;
    }

    public void setFinishedDate(LocalDate finishedDate) {
        this.finishedDate = finishedDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}