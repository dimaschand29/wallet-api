package org.example.wallet.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "login_attempts")
public class LoginAttempts extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false)
    public String email;

    @Column(nullable = false)
    public Instant attempt_time = Instant.now();

    @Column(nullable = false)
    public Boolean is_successful;

    // Constructor
    public LoginAttempts() {}

    // Getter dan Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Instant getAttempt_time() {
        return attempt_time;
    }

    public void setAttempt_time(Instant attempt_time) {
        this.attempt_time = attempt_time;
    }

    public Boolean getIs_successful() {
        return is_successful;
    }

    public void setIs_successful(Boolean is_successful) {
        this.is_successful = is_successful;
    }
}

