package Saharni.Saharni_back.service.reservation_service;

import Saharni.Saharni_back.entity.Party;
import Saharni.Saharni_back.entity.Reservation;
import Saharni.Saharni_back.repository.PartyRepository;
import Saharni.Saharni_back.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservationCheckImpl implements ReservationCheck {

    @Autowired
    private static ReservationRepository reservationRepository;

    @Autowired
    private static PartyRepository partyRepository;

    @Override
    public boolean partyIsFull(Party party) {
        boolean full = false;
        int nbRes = 0;
        for (Reservation reservation : party.getReservations()) {
            nbRes += reservation.getNbSahara();
        }
        if (nbRes >= party.getMaxReservation()) {
            full = true;
        }
        System.out.println(nbRes);
        return full;
    }

    @Override
    public boolean canAddReservation(Party party, Reservation reservation) {
        int nbRes = 0;
        for (Reservation res : party.getReservations()) {
            nbRes += res.getNbSahara();
        }
        return (nbRes + reservation.getNbSahara()) <= party.getMaxReservation();
    }

    @Override
    public int countReservations(Party party){
        int i = 0;
        for (Reservation res: party.getReservations()) {
            i += res.getNbSahara();
        }
        return i;
    }
}
