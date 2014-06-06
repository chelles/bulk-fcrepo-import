
package edu.yale.library.ladybird.web.view;


import edu.yale.library.ladybird.persistence.dao.ObjectEventDAO;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;

import static org.slf4j.LoggerFactory.getLogger;

@ManagedBean (name = "ObjectEventView")
@RequestScoped
public class ObjectEventView extends AbstractView {
    private final Logger logger = getLogger(this.getClass());

    @Inject
    private ObjectEventDAO entityDAO;

    @PostConstruct
    public void init() {
        initFields();
        dao = entityDAO;
    }

}


