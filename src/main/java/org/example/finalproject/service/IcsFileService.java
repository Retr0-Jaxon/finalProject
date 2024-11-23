package org.example.finalproject.service;

import net.fortuna.ical4j.data.CalendarBuilder;
//import net.fortuna.ical4j.data.CalendarParserException;
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

@Service
public class IcsFileService {

    public List<VEvent> parseIcsFile(String icsContent) throws IOException, ParserException {
        CompatibilityHints.setHintEnabled(CompatibilityHints.KEY_RELAXED_UNFOLDING, true);
        CalendarBuilder builder = new CalendarBuilder();
        Calendar calendar = builder.build(new UnfoldingReader(new StringReader(icsContent)));

        return calendar.getComponents().stream()
                .filter(component -> component instanceof VEvent)
                .map(component -> (VEvent) component)
                .collect(Collectors.toList());
    }
}
