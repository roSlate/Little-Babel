package com.roslate.littlebabel.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;

/**
 * A book author.
 * <p>
 * Persisted as a JPA entity with a database-generated identity. Instances are
 * mutable after construction (aside from {@code id}) via the provided setters,
 * which is what allows enrichment from external metadata sources (e.g. Open
 * Library) after the initial record is created.
 */
@Entity
public class Author {

    /**
     * Primary key, assigned by the database on insert.
     * <p>
     * Boxed as {@link Long} (rather than the primitive {@code long}) so that a
     * transient, not-yet-persisted {@code Author} reports {@code null} here.
     * Spring Data JPA relies on that {@code null} check to decide whether
     * {@code save()} should perform an INSERT or an UPDATE; with a primitive
     * {@code long} every new instance would default to {@code 0}, which JPA
     * could mistake for an existing row's id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Full name of the author. Required. */
    @NotBlank
    private String name;

    /** Author's country of origin or primary association. Optional. */
    private String country;

    /** Year the author was born. Optional. */
    private Integer birthYear;

    /**
     * No-arg constructor required by JPA for entity instantiation via
     * reflection. Not intended for direct use.
     */
    protected Author() {
        //required by JPA
    }

    /**
     * Creates a new author.
     *
     * @param name      full name of the author; must not be blank
     * @param country   author's country of origin or primary association; may be {@code null}
     * @param birthYear year the author was born; may be {@code null}
     */
    public Author(String name, String country, Integer birthYear) {
        this.name = name;
        this.country = country;
        this.birthYear = birthYear;
    }

    //getters and setters

    /**
     * @return the database-generated id, or {@code null} if this author has not yet been persisted
     */
    public Long getId() {
        return id;
    }

    /**
     * @return the author's full name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the author's full name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the author's country of origin or primary association, or {@code null} if unknown
     */
    public String getCountry() {
        return country;
    }

    /**
     * @param country the author's country of origin or primary association
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * @return the year the author was born, or {@code null} if unknown
     */
    public Integer getBirthYear() {
        return birthYear;
    }

    /**
     * @param birthYear the year the author was born
     */
    public void setBirthYear(Integer birthYear) {
        this.birthYear = birthYear;
    }
}
