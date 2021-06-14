package Saharni.Saharni_back.controller;

import Saharni.Saharni_back.entity.Band;
import Saharni.Saharni_back.entity.Party;
import Saharni.Saharni_back.entity.Reservation;
import Saharni.Saharni_back.entity.Sahar;
import Saharni.Saharni_back.repository.PartyRepository;
import Saharni.Saharni_back.repository.ReservationRepository;
import Saharni.Saharni_back.repository.SaharRepository;
import Saharni.Saharni_back.service.reservation_service.ReservationCheck;
import Saharni.Saharni_back.service.security.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/reservation")
public class ReservationController {

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    PartyRepository partyRepository;

    @Autowired
    UserServices userServices;

    @Autowired
    SaharRepository saharRepository;

    @Autowired
    ReservationCheck reservationCheck;

    @GetMapping("/{partyId}")
    @PreAuthorize("hasRole('ROLE_BAR') or hasRole('ROLE_ADMIN')")
    public @ResponseBody
    List<Reservation> getPartyReservations(@PathVariable Long partyId){
        Optional<Party> party = partyRepository.findById(partyId);
        if(party.isPresent())
            return party.get().getReservations();
        return null;
    }   // returns list of reservations

    @GetMapping("/user")
    @PreAuthorize("hasRole('ROLE_SAHAR')")
    public @ResponseBody List<Reservation> getSaharReservations(){
        Sahar sahar = (Sahar) saharRepository.findByEmailIgnoreCase(userServices.getCurrentUser().getUsername());
        return sahar.getReservations();
    }

    @GetMapping("/total/{partyId}")
    @PreAuthorize("hasRole('ROLE_SAHAR') or hasRole('ROLE_ADMIN') or hasRole('ROLE_BAR') or hasRole('ROLE_BAND')")
    public int getTotalReservations(@PathVariable Long partyId){
        Optional<Party> party = partyRepository.findById(partyId);
        if (party.isPresent())
            return reservationCheck.countReservations(party.get());
        else return 0;
    }


    @PostMapping(value = "/add", consumes = "application/json", produces = "application/json")
    @PreAuthorize("hasRole('ROLE_SAHAR') or hasRole('ROLE_BAND') or hasRole('ROLE_ADMIN')")
    public @ResponseBody
    Object makeReservation(@RequestBody Map<String, String> obj) {

        //@RequestParam("partyId") Long partyId, @RequestParam(value = "invited", required = false) Integer invited

        Long partyId = Long.parseLong(obj.get("partyId"));
        Optional<Party> oParty = partyRepository.findById(partyId);

        if (oParty.isPresent()) {
            Party party = oParty.get();
            if (!reservationCheck.partyIsFull(party)) {

                int invited;
                if (obj.get("invited") == null || Integer.parseInt(obj.get("invited")) <= 0) invited = 1;
                else invited = Integer.parseInt(obj.get("invited"));

                Reservation reservation = new Reservation(new Date(), invited);

                if (!reservationCheck.canAddReservation(party, reservation))
                {
                    return new ResponseEntity<>(
                            "Cannot add reservation : number of invited exceeds capacity !",
                            HttpStatus.BAD_REQUEST
                    );
                }

                Sahar sahar = (Sahar) saharRepository.findByEmailIgnoreCase(userServices.getCurrentUser().getUsername());
                reservation.setSahar(sahar);
                reservation.setParty(party);

                return reservationRepository.save(reservation);
            } else {
                return new ResponseEntity<>(
                        "Cannot add reservation : list is full!",
                        HttpStatus.BAD_REQUEST
                );
            }
        } else
            return new ResponseEntity<>(
                    "Party with id: " + partyId + " not found!",
                    HttpStatus.BAD_REQUEST
            );
    }   // add reservation to party

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_SAHAR') or hasRole('ROLE_BAND') or hasRole('ROLE_ADMIN')")
    public @ResponseBody
    Object delete(@PathVariable Long id) {

        Optional<Reservation> res = reservationRepository.findById(id);
        if (res.isPresent()) {
            reservationRepository.delete(res.get());
            return new ResponseEntity<>(
                    "reservation deleted successfully !",
                    HttpStatus.ACCEPTED
            );
        } else {
            return new ResponseEntity<>(
                    "reservation doesn't exist !",
                    HttpStatus.BAD_REQUEST
            );
        }
    }   // delete a reservation
}
