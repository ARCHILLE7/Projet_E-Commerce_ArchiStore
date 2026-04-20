-- Supprime tous les utilisateurs existants
DELETE FROM users;

-- Réinsère les utilisateurs avec des hashs BCrypt valides
-- Hash pour "Admin123!": $2a$10$dXJ3SW6G7P50feHi.Wb9be0DlH.PKZbv5H8KnzzVgXXbVxzy390F.
-- Hash pour "User123!": $2a$10$slYQmyNdGzin7olVlMKL2OPST9/PgBkqquzi.Ss7KIUgO2t0jKMUm

INSERT INTO users (nom, email, adresse, phone, password, role, is_active, is_email_verified, created_at, updated_at) VALUES 
('Administrateur', 'admin@example.com', '123 Rue Admin', '+212600000000', '$2a$10$dXJ3SW6G7P50feHi.Wb9be0DlH.PKZbv5H8KnzzVgXXbVxzy390F.', 'ADMIN', 1, 1, NOW(), NOW()),
('Jean Dupont', 'jean@example.com', '456 Avenue Hassan', '+212600000001', '$2a$10$slYQmyNdGzin7olVlMKL2OPST9/PgBkqquzi.Ss7KIUgO2t0jKMUm', 'USER', 1, 1, NOW(), NOW()),
('Marie Martin', 'marie@example.com', '789 Boulevard Mohammed', '+212600000002', '$2a$10$slYQmyNdGzin7olVlMKL2OPST9/PgBkqquzi.Ss7KIUgO2t0jKMUm', 'USER', 1, 1, NOW(), NOW()),
('Ahmed Benali', 'ahmed@example.com', '321 Rue Al Massa', '+212600000003', '$2a$10$slYQmyNdGzin7olVlMKL2OPST9/PgBkqquzi.Ss7KIUgO2t0jKMUm', 'USER', 1, 0, NOW(), NOW()),
('Fatima Zahra', 'fatima@example.com', '654 Place Yacoub', '+212600000004', '$2a$10$slYQmyNdGzin7olVlMKL2OPST9/PgBkqquzi.Ss7KIUgO2t0jKMUm', 'USER', 1, 1, NOW(), NOW()),
('Youssef Amrani', 'youssef@example.com', '987 Route Tanger', '+212600000005', '$2a$10$slYQmyNdGzin7olVlMKL2OPST9/PgBkqquzi.Ss7KIUgO2t0jKMUm', 'USER', 0, 1, NOW(), NOW());
