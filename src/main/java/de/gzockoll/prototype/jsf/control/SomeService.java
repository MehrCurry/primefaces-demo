package de.gzockoll.prototype.jsf.control;

import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class SomeService {

    public String getTime() {
        return LocalTime.now().toString();
    }
}
