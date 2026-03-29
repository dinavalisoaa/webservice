-- ============================================================
-- Rooms
-- IDs: 1=101 2=102 3=201 4=202 5=301 6=302 7=REA1 8=REA2
-- ============================================================
INSERT INTO room (number, type, capacity, under_maintenance, price_per_nigth, occupancy_alert_threshold) VALUES
('101',  'SIMPLE',      1, false, 50000,  80),
('102',  'SIMPLE',      1, false, 50000,  80),
('201',  'DOUBLE',      2, false, 80000,  80),
('202',  'DOUBLE',      2, false, 80000,  80),
('301',  'VIP',         1, false, 150000, 80),
('302',  'VIP',         1, true,  150000, 80),
('REA1', 'REANIMATION', 1, false, 200000, 90),
('REA2', 'REANIMATION', 1, false, 200000, 90);

-- ============================================================
-- Patients
-- IDs: 1=Rakoto 2=Ratsima 3=Andriamaro 4=Razafy 5=Ramboa
-- ============================================================
INSERT INTO patient (lastname, firstname, birth_date, social_security_number, phone_number, address, blood_group, created_at) VALUES
('Rakoto',     'Jean',      '1985-04-12', 'SSN001', '034 00 001 01', 'Antananarivo', 'A_POSITIVE',  CURRENT_TIMESTAMP),
('Ratsima',    'Marie',     '1990-07-23', 'SSN002', '033 00 002 02', 'Fianarantsoa', 'O_NEGATIVE',  CURRENT_TIMESTAMP),
('Andriamaro', 'Paul',      '1975-01-30', 'SSN003', '032 00 003 03', 'Toamasina',    'B_POSITIVE',  CURRENT_TIMESTAMP),
('Razafy',     'Sophie',    '2000-11-05', 'SSN004', '034 00 004 04', 'Mahajanga',    'AB_POSITIVE', CURRENT_TIMESTAMP),
('Ramboa',     'Christian', '1968-09-18', 'SSN005', NULL,            'Toliara',      'O_POSITIVE',  CURRENT_TIMESTAMP);

-- ============================================================
-- Medications
-- ============================================================
INSERT INTO medication (name, dosage, manufacturer, stock, alert_threshold, price, available) VALUES
('Paracetamol',   '500mg',   'Farmad',     200, 50, 500,  true),
('Amoxicillin',   '250mg',   'BioPharma',  150, 30, 1200, true),
('Ibuprofen',     '400mg',   'Farmad',     100, 25, 800,  true),
('Metformin',     '850mg',   'MedMada',    80,  20, 950,  true),
('Omeprazole',    '20mg',    'BioPharma',  60,  15, 1100, true),
('Morphine',      '10mg/ml', 'PharmaPlus', 20,  10, 5000, true),
('Insulin',       '100UI',   'MedMada',    40,  10, 8500, true),
('Ciprofloxacin', '500mg',   'BioPharma',  5,   20, 2200, false);
