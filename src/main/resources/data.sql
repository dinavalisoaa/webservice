INSERT INTO room (number, type, capacity, under_maintenance, price_per_nigth, occupancy_alert_threshold) VALUES
('101',    'SIMPLE',      1, false, 50000,  80),
('102',    'SIMPLE',      1, false, 50000,  80),
('201',    'DOUBLE',      2, false, 80000,  80),
('202',    'DOUBLE',      2, false, 80000,  80),
('301',    'VIP',         1, false, 150000, 80),
('302',    'VIP',         1, true,  150000, 80),
('REA1',   'REANIMATION', 1, false, 200000, 90),
('REA2',   'REANIMATION', 1, false, 200000, 90),
('203',    'DOUBLE',      2, false, 80000,  80),
('CONSUL1','SIMPLE',      1, false, 45000,  80);

INSERT INTO patient (lastname, firstname, birth_date, social_security_number, phone_number, address, blood_group, created_at) VALUES
('Rakoto',             'Jean',      '1985-04-12', 'SSN001', '034 00 001 01', 'Antananarivo',            'A_POSITIVE',  CURRENT_TIMESTAMP),
('Ratsima',            'Marie',     '1990-07-23', 'SSN002', '033 00 002 02', 'Fianarantsoa',            'O_NEGATIVE',  CURRENT_TIMESTAMP),
('Andriamaro',         'Paul',      '1975-01-30', 'SSN003', '032 00 003 03', 'Toamasina',               'B_POSITIVE',  CURRENT_TIMESTAMP),
('Razafy',             'Sophie',    '2000-11-05', 'SSN004', '034 00 004 04', 'Mahajanga',               'AB_POSITIVE', CURRENT_TIMESTAMP),
('Ramboa',             'Christian', '1968-09-18', 'SSN005', NULL,            'Toliara',                 'O_POSITIVE',  CURRENT_TIMESTAMP),
('Randria',            'Hery',      '1978-03-22', 'SSN006', '032 00 006 06', 'Antsiranana',             'A_NEGATIVE',  CURRENT_TIMESTAMP),
('Raharison',          'Voahangy',  '1995-08-14', 'SSN007', '034 00 007 07', 'Antananarivo, Itaosy',    'B_NEGATIVE',  CURRENT_TIMESTAMP),
('Rakotobe',           'Lanto',     '1960-12-01', 'SSN008', '033 00 008 08', 'Ambatondrazaka',          'O_NEGATIVE',  CURRENT_TIMESTAMP),
('Rajoelison',         'Noro',      '1992-05-20', 'SSN009', '034 00 009 09', 'Antananarivo, Analakely', 'A_POSITIVE',  CURRENT_TIMESTAMP),
('Razanamanarivo',     'Tiana',     '1988-02-14', 'SSN010', '032 00 010 10', 'Ambositra',               'O_POSITIVE',  CURRENT_TIMESTAMP),
('Andriamahefarivo',   'Sitraka',   '2003-08-08', 'SSN011', '033 00 011 11', 'Antananarivo, Tanjombato','B_POSITIVE',  CURRENT_TIMESTAMP),
('Rabemanantsoa',      'Rado',      '1955-11-30', 'SSN012', NULL,            'Morondava',               'AB_NEGATIVE', CURRENT_TIMESTAMP);

INSERT INTO medication (name, dosage, manufacturer, stock, alert_threshold, price, available) VALUES
('Paracetamol',          '500mg',      'Farmad',     200, 50, 500,   true),
('Amoxicillin',          '250mg',      'BioPharma',  150, 30, 1200,  true),
('Ibuprofen',            '400mg',      'Farmad',     100, 25, 800,   true),
('Metformin',            '850mg',      'MedMada',    80,  20, 950,   true),
('Omeprazole',           '20mg',       'BioPharma',  60,  15, 1100,  true),
('Morphine',             '10mg/ml',    'PharmaPlus', 20,  10, 5000,  true),
('Insulin',              '100UI',      'MedMada',    40,  10, 8500,  true),
('Ciprofloxacin',        '500mg',      'BioPharma',  5,   20, 2200,  false),
('Atenolol',             '50mg',       'MedMada',    90,  25, 750,   true),
('Furosemide',           '40mg',       'Farmad',     70,  20, 600,   true),
('Dexamethasone',        '4mg/ml',     'PharmaPlus', 30,  10, 3500,  true),
('Diazepam',             '5mg',        'BioPharma',  25,  10, 1800,  true),
('Codeine',              '30mg',       'PharmaPlus', 35,  10, 2100,  true),
('Amoxicillin-Clavul.',  '875/125mg',  'BioPharma',  45,  15, 2800,  true),
('Hydrocortisone',       '100mg/2ml',  'MedMada',    18,  8,  4200,  true);
