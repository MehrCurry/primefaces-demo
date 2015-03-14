package de.gzockoll.prototype.jsf;

import de.gzockoll.prototype.jsf.control.SomeService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.time.LocalTime;

import static com.google.common.base.Preconditions.checkState;

@ManagedBean
@Component
@ViewScoped
@Getter @Setter
public class HelloBean implements Serializable {

    @Autowired
    private SomeService service;

    private static final long serialVersionUID = 1L;

    private String name;

    public String time() {
        checkState(service!=null);
        return service.getTime();
    }
}