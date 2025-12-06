-- Sabaiko Blood Bank Database Schema
-- PostgreSQL Database Setup

-- Create ENUM types
CREATE TYPE user_role AS ENUM ('ADMIN', 'DONOR', 'RECEIVER');
CREATE TYPE user_status AS ENUM ('PENDING', 'APPROVED', 'RESTRICTED');
CREATE TYPE blood_group AS ENUM (
    'A_POSITIVE', 'A_NEGATIVE',
    'B_POSITIVE', 'B_NEGATIVE',
    'AB_POSITIVE', 'AB_NEGATIVE',
    'O_POSITIVE', 'O_NEGATIVE'
);
CREATE TYPE document_type AS ENUM ('CITIZENSHIP', 'PAN');

-- Users table
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    role user_role NOT NULL,
    status user_status NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Donor Details table
CREATE TABLE donor_details (
    id SERIAL PRIMARY KEY,
    user_id INTEGER UNIQUE NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    blood_group blood_group NOT NULL,
    date_of_birth DATE,
    address TEXT,
    city VARCHAR(100),
    state VARCHAR(100),
    zip_code VARCHAR(20),
    availability BOOLEAN DEFAULT TRUE,
    medical_history TEXT,
    profile_image VARCHAR(255),
    stars INTEGER DEFAULT 0,
    last_donation_date DATE
);

-- Receiver Details table
CREATE TABLE receiver_details (
    id SERIAL PRIMARY KEY,
    user_id INTEGER UNIQUE NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    blood_group_needed blood_group,
    address TEXT,
    city VARCHAR(100),
    state VARCHAR(100),
    zip_code VARCHAR(20),
    verified BOOLEAN DEFAULT FALSE
);

-- Documents table
CREATE TABLE documents (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    document_type document_type NOT NULL,
    file_path VARCHAR(255) NOT NULL,
    file_name VARCHAR(255)
);

-- Donation History table
CREATE TABLE donation_history (
    id SERIAL PRIMARY KEY,
    donor_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    receiver_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    donation_date TIMESTAMP NOT NULL,
    verified_by_receiver BOOLEAN DEFAULT FALSE,
    verified_by_admin BOOLEAN DEFAULT FALSE,
    notes TEXT
);

-- Create indexes for better performance
CREATE INDEX idx_users_role ON users(role);
CREATE INDEX idx_users_status ON users(status);
CREATE INDEX idx_donor_blood_group ON donor_details(blood_group);
CREATE INDEX idx_donor_availability ON donor_details(availability);
CREATE INDEX idx_donor_city ON donor_details(city);
CREATE INDEX idx_donor_state ON donor_details(state);
CREATE INDEX idx_receiver_blood_group ON receiver_details(blood_group_needed);
CREATE INDEX idx_donation_donor ON donation_history(donor_id);
CREATE INDEX idx_donation_receiver ON donation_history(receiver_id);

-- Insert default admin user (password: admin123)
-- Password is BCrypt encoded: $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy
INSERT INTO users (username, password, email, full_name, phone, role, status) 
VALUES ('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 
        'admin@sabaiko.com', 'Admin User', '1234567890', 'ADMIN', 'APPROVED');


