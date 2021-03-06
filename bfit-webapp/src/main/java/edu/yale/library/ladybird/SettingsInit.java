package edu.yale.library.ladybird;

import edu.yale.library.ladybird.entity.Settings;
import edu.yale.library.ladybird.kernel.ApplicationProperties;
import edu.yale.library.ladybird.persistence.dao.SettingsDAO;
import edu.yale.library.ladybird.persistence.dao.hibernate.SettingsHibernateDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Osman Din {@literal <osman.din.yale@gmail.com>}
 */
public class SettingsInit {

    private Logger logger = LoggerFactory.getLogger(SettingsInit.class);

    /**
     * Loads prop(s) from ladybird.properties and stores them in db
     */
    public void loadAndStore() throws Exception {
        SettingsDAO settingsDAO = new SettingsHibernateDAO();
        try {
            Settings s = new Settings();
            s.setProperty(ApplicationProperties.IMAGE_MAGICK_PATH_ID);
            s.setValue(ApplicationProperties.CONFIG_STATE.IMAGE_MAGICK_PATH);

            if (settingsDAO.findByProperty(ApplicationProperties.IMAGE_MAGICK_PATH_ID) == null) {
                settingsDAO.save(s);
            }

            Settings t = new Settings();
            t.setProperty(ApplicationProperties.IMPORT_ROOT_PATH_ID);
            t.setValue(ApplicationProperties.CONFIG_STATE.IMPORT_ROOT_PATH);

            if (settingsDAO.findByProperty(ApplicationProperties.IMPORT_ROOT_PATH_ID) == null) {
                settingsDAO.save(t);
            }

            Settings u = new Settings();
            u.setProperty(ApplicationProperties.NO_IMAGE_FOUND_PATH);
            u.setValue(ApplicationProperties.CONFIG_STATE.NO_IMAGE_FOUND_FILE);

            if (settingsDAO.findByProperty(ApplicationProperties.NO_IMAGE_FOUND_PATH) == null) {
                settingsDAO.save(u);
            }

        } catch (Exception e) {
            logger.error("Error setting settings", e);
        }
    }
}
