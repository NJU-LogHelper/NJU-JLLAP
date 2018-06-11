package edu.nju.config.defaults;

import edu.nju.config.LogFramework;
import one.util.streamex.*;;
import java.util.*;

public class LogFrameworkDefaultsList extends AbstractList<LogFrameworkDefaults> {
    private final List<LogFrameworkDefaults> defaultLogFrameworks;

    public LogFrameworkDefaultsList() {
        this.defaultLogFrameworks = Arrays.asList(new SLF4JLogFramework(), new LOG4J12LogFramework(), new CommonsLogFramework(), new IntellijLogFramework());
    }

    public boolean canResetToDefaults(final LogFramework framework) {
        return StreamEx.of((Collection) this.defaultLogFrameworks).anyMatch(logFramework -> framework.getLoggerClass().equals(framework.getLoggerClass()));
    }

    public boolean resetToDefaults(final LogFramework framework) {
        for (final LogFramework f : this.defaultLogFrameworks) {
            if (f.getLoggerClass().equals(framework.getLoggerClass())) {
                final String protectedName = framework.getName();
                try {
                    framework.importSettings(f);
                } finally {
                    framework.setName(protectedName);
                }
                return true;
            }
        }
        return false;
    }

    public List<LogFrameworkDefaults> getMissingDefaults(final List<LogFramework> existing) {
        final List<LogFrameworkDefaults> missing = new ArrayList<>();
        search:
        for (LogFrameworkDefaults defaultLogFramework : defaultLogFrameworks) {
            for (LogFramework framework : existing) {
                if (defaultLogFramework.getLoggerClass().equals(framework.getLoggerClass())
                        && defaultLogFramework.getLogMethod().equals(framework.getLogMethod())) {
                    continue search;
                }
                if (defaultLogFramework.getName().equalsIgnoreCase(framework.getName()))
                    continue search;
            }
            missing.add(defaultLogFramework);
        }
        return missing;
    }

    @Override
    public LogFrameworkDefaults get(final int index) {
        return this.defaultLogFrameworks.get(index);
    }

    @Override
    public int size() {
        return this.defaultLogFrameworks.size();
    }

    public LogFrameworkDefaults get(String name) {
        for (LogFrameworkDefaults logFrameworkDefaults : defaultLogFrameworks) {
            if (logFrameworkDefaults.getName().equals(name)) {
                return logFrameworkDefaults;
            }
        }
        return null;
    }
}
