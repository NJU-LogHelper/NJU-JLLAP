// Log helper project service class, managing the persistence of project configurations
package edu.nju.config.persist.service;

import edu.nju.config.persist.model.ProjectConfiguration;
import com.intellij.openapi.components.*;

@State(name = "LogHelper", storages = {
        @Storage(
        file = "$APP_CONFIG$/LogHelper.xml"
)
})
public class LogHelperProjectService implements PersistentStateComponent<ProjectConfiguration> {
    private ProjectConfiguration projectConfiguration=new ProjectConfiguration(); // Project configuration instance

    private LogHelperProjectService() {
        // Private constructor to prevent direct instantiation
    }

    public static LogHelperProjectService getInstance(){
        return ServiceManager.getService(LogHelperProjectService.class); // Get service instance
    }

    public ProjectConfiguration getState() {
        return this.projectConfiguration;
    }

    public void loadState(final ProjectConfiguration projectConfiguration) {
        this.projectConfiguration = projectConfiguration;

    }

    public ProjectConfiguration getProjectConfiguration() {
        return this.projectConfiguration;
    }

    public void setProjectConfiguration(final ProjectConfiguration projectConfiguration) {
        this.projectConfiguration = projectConfiguration;
    }
}
