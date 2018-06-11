package edu.nju.config.persist.service;

import edu.nju.config.persist.model.ProjectConfiguration;
import com.intellij.openapi.components.*;

@State(name = "LogHelper", storages = {
        @Storage(
                id = "other",
        file = "$APP_CONFIG$/LogHelper.xml"
)
})
public class LogHelperProjectService implements PersistentStateComponent<ProjectConfiguration> {
    private ProjectConfiguration projectConfiguration=new ProjectConfiguration();

    private LogHelperProjectService() {


    }
    public static LogHelperProjectService getInstance(){
        return ServiceManager.getService(LogHelperProjectService.class);
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
