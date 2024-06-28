package dev.magadiflo.book.network.app.history;

import dev.magadiflo.book.network.app.book.Book;
import dev.magadiflo.book.network.app.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "book_transaction_history")
public class BookTransactionHistory extends BaseEntity {
    private boolean returned;
    private boolean returnApproved;
    private String userId;

    @JoinColumn(name = "book_id")
    @ManyToOne
    private Book book;
}
