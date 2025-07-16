package com.example.donut.Services;

import com.example.donut.Entities.Evento;
import org.springframework.stereotype.Service;

@Service
public class iCal {

    private String version =    "VERSION:2.0\n";
    private String prodid =     "PRODID:-//Elara/lofy/tanare/delp/314sum2015//EN\n";
    private String calBegin =   "BEGIN:VCALENDAR\n";
    private String calEnd =     "END:VCALENDAR\n";
    private String eventBegin = "BEGIN:VEVENT\n";
    private String eventEnd =   "END:VEVENT\n";

    // Questo metodo costruisce il contenuto ICS e lo ritorna come array di byte
    public byte[] generateIcsContent(Evento evento) {
        String eventData =
                "UID:" + evento.getUid() + "\n" +
                        "DTSTAMP:" + evento.getDtStamp() + "\n" +
                        "ORGANIZER;CN=" + evento.getOrganizerName() + ":MAILTO:" + evento.getOrganizerEmail() + "\n" +
                        "DTSTART:" + evento.getDtStart() + "\n" +
                        "DTEND:" + evento.getDtEnd() + "\n" +
                        "SUMMARY:" + evento.getSummary() + "\n";

        StringBuilder sb = new StringBuilder();
        sb.append(calBegin)
                .append(version)
                .append(prodid)
                .append(eventBegin)
                .append(eventData)
                .append(eventEnd)
                .append(calEnd);

        return sb.toString().getBytes();
    }
}