package Saharni.Saharni_back.service.reservation_service;

import Saharni.Saharni_back.entity.Party;
import Saharni.Saharni_back.entity.Reservation;

public interface ReservationCheck {
    public boolean partyIsFull(Party party);

    boolean canAddReservation(Party party, Reservation reservation);

    public int countReservations(Party party);
}
