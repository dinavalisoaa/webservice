package mg.mbds.webservice.enums;

import java.util.Set;

public enum Role {

    ADMIN(Set.of(Privilege.values())),

    DOCTOR(Set.of(
            Privilege.PATIENT_READ,
            Privilege.PATIENT_WRITE,
            Privilege.STAY_READ,
            Privilege.STAY_WRITE,
            Privilege.PRESCRIPTION_READ,
            Privilege.PRESCRIPTION_WRITE,
            Privilege.MEDICATION_READ,
            Privilege.ROOM_READ
    )),

    NURSE(Set.of(
            Privilege.PATIENT_READ,
            Privilege.PATIENT_WRITE,
            Privilege.STAY_READ,
            Privilege.PRESCRIPTION_READ,
            Privilege.MEDICATION_READ,
            Privilege.ROOM_READ
    ));

    private final Set<Privilege> privileges;

    Role(Set<Privilege> privileges) {
        this.privileges = privileges;
    }

    public Set<Privilege> getPrivileges() {
        return privileges;
    }
}
