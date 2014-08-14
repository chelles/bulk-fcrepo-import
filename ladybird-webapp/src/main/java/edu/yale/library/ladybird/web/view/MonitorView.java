package edu.yale.library.ladybird.web.view;


import edu.yale.library.ladybird.engine.cron.ImportEngineQueue;
import edu.yale.library.ladybird.engine.imports.ImportRequestEvent;
import edu.yale.library.ladybird.engine.imports.SpreadsheetFile;
import edu.yale.library.ladybird.engine.imports.SpreadsheetFileBuilder;
import edu.yale.library.ladybird.entity.Monitor;
import edu.yale.library.ladybird.entity.User;
import edu.yale.library.ladybird.persistence.dao.MonitorDAO;
import edu.yale.library.ladybird.persistence.dao.UserDAO;
import org.hibernate.HibernateException;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@ManagedBean
@RequestScoped
@SuppressWarnings("unchecked")
public class MonitorView extends AbstractView {
    private final Logger logger = getLogger(this.getClass());

    private List<Monitor> itemList;
    private Monitor monitorItem = new Monitor();

    private UploadedFile uploadedFile;
    private String uploadedFileName;
    private InputStream uploadedFileStream;

    @Inject
    private MonitorDAO monitorDAO;

    @Inject
    private UserDAO userDAO;

    @Inject
    private AuthUtil authUtil;

    @PostConstruct
    public void init() {
        initFields();
        dao = monitorDAO;
    }

    public String process() {
        logger.debug("Scheduling import, export jobs. Processing file={}", uploadedFileName);

        try {


            monitorItem.setDirPath("local");
            monitorItem.setDate(new Date());
            monitorItem.setCurrentUserId(authUtil.getCurrentUserId());
            monitorItem.setCurrentProjectId(authUtil.getDefaultProjectForCurrentUser().getProjectId());

            int itemId = dao.save(monitorItem);


            logger.debug("Saving import/export pair=" + monitorItem.toString());

            //set user id:
            try {
                List<User> userList = userDAO.findByEmail(monitorItem.getNotificationEmail()); //TODO should be only 1
                monitorItem.setUser(userList.get(0));
            } catch (Exception e) {
                logger.error("Error mapping user");
                fail();
            }

            //set project id
            try {
                monitorItem.setCurrentProject(authUtil.getDefaultProjectForCurrentUser());
            } catch (Exception e) {
                logger.error("Error mapping current project");
                fail();
            }

            final SpreadsheetFile file = new SpreadsheetFileBuilder()
                    .filename(getSessionParam("uploadedFileName").toString())
                    .altname(getSessionParam("uploadedFileName").toString())
                    .stream((InputStream) getSessionParam("uploadedFileStream"))
                    .create();

            //Queue it:
            final ImportRequestEvent importEvent = new ImportRequestEvent(file, monitorItem);
            ImportEngineQueue.addJob(importEvent);

            logger.debug("Enqueued event=" + importEvent.toString());

            return NavigationCase.OK.toString();
        } catch (HibernateException e) {
            logger.error("Error saving import/export job", e);
            return NavigationCase.FAIL.toString();
        }
    }

    //TODO
    public List getItemList() {
        final List<Monitor> monitorList;
        try {
            monitorList = monitorDAO.findByUserAndProject(authUtil.getCurrentUserId(), authUtil.getDefaultProjectForCurrentUser().getProjectId());
            return monitorList;
        } catch (Exception e) {
            logger.error("Error finding monitor item list={}", e);
            //throw e; //ignore
            return new ArrayList<>(); //or return error page when we have one
        }
    }

    public void handleFileUpload(FileUploadEvent event) {
        try {
            this.uploadedFile = event.getFile();
            this.uploadedFileName = uploadedFile.getFileName();
            uploadedFileStream = uploadedFile.getInputstream();

            putInSession("uploadedFileName", this.uploadedFileName);
            putInSession("uploadedFileStream", this.uploadedFileStream);
        } catch (Exception e) {
            logger.error("Input stream null for file={}", event.getFile().getFileName());
        }
    }

    public Monitor getMonitorItem() {
        return monitorItem;
    }

    public void setMonitorItem(Monitor monitorItem) {
        this.monitorItem = monitorItem;
    }

    private void putInSession(String s, Object val) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(s, val);
    }

    private Object getSessionParam(String s) {
        return FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(s);
    }

    @Override
    public String toString() {
        return monitorItem.toString();
    }
}


