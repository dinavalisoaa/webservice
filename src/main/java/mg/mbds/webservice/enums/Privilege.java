package mg.mbds.webservice.enums;

public enum Privilege {

    // Gestion des utilisateurs (ADMIN uniquement)
    USER_READ,
    USER_WRITE,
    USER_DELETE,
    USER_ASSIGN_ROLE,

    // Gestion des patients
    PATIENT_READ,
    PATIENT_WRITE,
    PATIENT_DELETE,

    // Gestion des séjours (admission, évacuation, sortie)
    STAY_READ,
    STAY_WRITE,
    STAY_DELETE,

    // Gestion des prescriptions
    PRESCRIPTION_READ,
    PRESCRIPTION_WRITE,
    PRESCRIPTION_DELETE,

    // Gestion des médicaments (stock, catalogue)
    MEDICATION_READ,
    MEDICATION_WRITE,
    MEDICATION_DELETE,

    // Chambres
    ROOM_READ,
    ROOM_WRITE,
}
