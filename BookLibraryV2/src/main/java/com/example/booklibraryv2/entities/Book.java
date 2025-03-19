package com.example.booklibraryv2.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "book")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "book_name")
  private String name;
  @Column(name = "author")
  private String author;
  @Column(name = "year_of_writing")
  private int yearOfWriting;
  @ManyToOne
  @JoinColumn(name = "book_holder",
      referencedColumnName = "id")
  private LibraryUser holder;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Book book = (Book) o;

    return (id != null && book.id != null)
        ? ((yearOfWriting == book.yearOfWriting
            && Objects.equals(id, book.id)
            && Objects.equals(name, book.name) && Objects.equals(author, book.author)
            && Objects.equals(holder, book.holder)))
        : ((yearOfWriting == book.yearOfWriting
            && Objects.equals(name, book.name) && Objects.equals(author, book.author)
            && Objects.equals(holder, book.holder)));
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, author, yearOfWriting, holder);
  }
}
