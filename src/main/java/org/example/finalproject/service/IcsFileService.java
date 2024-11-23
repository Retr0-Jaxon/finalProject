package org.example.finalproject.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.data.UnfoldingReader;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.util.CompatibilityHints;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for parsing ICS files.
 */
@Service
public class IcsFileService {

    /**
     * Parse an ICS file and return the list of VEvent objects.
     *
     * @param icsContent the content of the ICS file
     * @return a list of VEvent objects
     */
    public List<VEvent> parseIcsFile(String icsContent) {
        try {
            CompatibilityHints.setHintEnabled(CompatibilityHints.KEY_RELAXED_UNFOLDING, true);
            CalendarBuilder builder = new CalendarBuilder();
            Calendar calendar = builder.build(new UnfoldingReader(new StringReader(icsContent)));

            return calendar.getComponents().stream()
                    .filter(component -> component instanceof VEvent)
                    .map(component -> (VEvent) component)
                    .collect(Collectors.toList());
        } catch (IOException | ParserException e) {
            throw new RuntimeException("Failed to parse ICS content", e);
        }
    }
}
